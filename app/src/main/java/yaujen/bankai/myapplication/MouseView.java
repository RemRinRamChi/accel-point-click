package yaujen.bankai.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;
import static yaujen.bankai.myapplication.Utility.aLog;

public class MouseView extends SurfaceView implements Runnable, SensorEventListener {

    private final int POS_TILT_GAIN = 40; // step size of position tilt

    volatile boolean mousing;

    private Thread mouseThread = null;

    private Mouse mouse;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private SensorFusion sensorFusion;
    private SensorManager sensorManager = null;

    public MouseView(Context context){
        super(context);
        init(context);
    }

    public MouseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MouseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //initializing player object
        mouse = new Mouse(context);

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        // Sensor configs
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        registerSensorManagerListeners();

        sensorFusion = new SensorFusion();
        sensorFusion.setMode(SensorFusion.Mode.FUSION);
    }

    public void registerSensorManagerListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void run() {
        while (mousing){
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }

    private void update() {
        double roll =  sensorFusion.getRoll(); // rotation along x-axis
        double pitch =  sensorFusion.getPitch(); // rotation along y-axis

        double tiltMagnitude = Math.sqrt(roll*roll + pitch*pitch);
        double tiltDirection = Math.asin(roll/tiltMagnitude);

        double displacement = tiltMagnitude*POS_TILT_GAIN;

        // testing purposes
        mouse.pitch = (int)pitch;
        mouse.roll = (int)roll;
        mouse.dir = tiltDirection;

        double xOffSet = displacement*Math.sin(tiltDirection);
        double yOffSet = displacement*Math.cos(tiltDirection);
        if(pitch > 0){
            yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
        }

        mouse.update((int)(mouse.getInitialX() + xOffSet),
                (int)( mouse.getInitialY() + yOffSet));
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //Drawing the player
            canvas.drawBitmap(
                    mouse.getBitmap(),
                    mouse.getX(),
                    mouse.getY(),
                    paint);
            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            mouseThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        mousing = false;
        try {
            //stopping the thread
            mouseThread.join();
        } catch (InterruptedException e) {
        }

        sensorManager.unregisterListener(this);
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        mousing = true;
        mouseThread = new Thread(this);
        mouseThread.start();

        registerSensorManagerListeners();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorFusion.setAccel(event.values);
                sensorFusion.calculateAccMagOrientation();
                break;

            case Sensor.TYPE_GYROSCOPE:
                sensorFusion.gyroFunction(event);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorFusion.setMagnet(event.values);
                break;
        }
    }

    public Mouse getMouse(){
        return mouse;
    }

}
