package yaujen.bankai.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;
import static yaujen.bankai.myapplication.Utility.aLog;


/**
 * SurfaceView implementation to simulate a surface view for the accelerometer-based mouse to move on
 */
public class MouseView extends SurfaceView implements Runnable, SensorEventListener, Clicker {
    // Tilt configurations
    private int posTiltGain = 35; // step size of position tilt
    private int velTiltGain = 100;
    private final double SAMPLING_RATE = 0.02;
    private final float BEZEL_THRESHHOLD = 50.0f;

    private boolean positionControl;

    private double initialX;
    private double initialY;
    private double currentPitch;
    private double refPitch;

    volatile boolean mousing;
    private Thread mouseThread = null;
    private Mouse mouse;

    //Objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private SensorFusion sensorFusion;
    private SensorManager sensorManager = null;

    private ClickingMethod clickingMethod;
    private View view;
    private BackTapService backTapService;

    // Clicking Floating Button
    private MovableFloatingActionButton buttonClicker;


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

        initialX = context.getResources().getDisplayMetrics().widthPixels/2;
        initialY = (context.getResources().getDisplayMetrics().heightPixels/2);
        mouse = new Mouse(context, initialX, initialY);
        refPitch =0;
        positionControl = false;

        backTapService = new BackTapService((Activity)getContext(), this);
        clickingMethod = ClickingMethod.BACK_TAP;

        setFocusableInTouchMode(true);
        setFocusable(true);
        requestFocus();
    }

    /**
     * The movable button is hidden at the start, so please call the method {@link MouseView#setClickingMethod(ClickingMethod)}
     * @param mFab
     */
    public void setMovableFloatingActionButton(MovableFloatingActionButton mFab){
        buttonClicker = mFab;
        buttonClicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click();
            }
        });
        this.enableOrDisableMovableFloatingActionButton(false);
    }

    /**
     * Hides or shows the movable button
     * @param enable
     */
    public void enableOrDisableMovableFloatingActionButton(boolean enable){
        if(buttonClicker != null){
            if (enable) {
                buttonClicker.show();
                buttonClicker.setAlpha(0.2f);
            } else {
                buttonClicker.hide();
            }
        }
    }

    private void registerSensorManagerListeners() {
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
        currentPitch = sensorFusion.getPitch();
        double pitch =  currentPitch - refPitch; // rotation along y-axis

        double tiltMagnitude = Math.sqrt(roll*roll + pitch*pitch);
        double tiltDirection = Math.asin(roll/tiltMagnitude);
        double velocity = velTiltGain *tiltMagnitude;
        double displacementPOS = tiltMagnitude* posTiltGain;
        double displacementVEL = velocity*SAMPLING_RATE;

        // testing purposes
        mouse.pitch = currentPitch;
        mouse.roll = roll;
        mouse.dir = tiltDirection;

        if(positionControl){
            double xOffSet = displacementPOS*Math.sin(tiltDirection);
            double yOffSet = displacementPOS*Math.cos(tiltDirection);
            if(pitch > 0){
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }

            mouse.update((initialX + xOffSet),
                    (initialY + yOffSet));
        } else {
            double xOffSet = displacementVEL*Math.sin(tiltDirection);
            double yOffSet = displacementVEL*Math.cos(tiltDirection);
            if(pitch > 0){
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }

            mouse.displace(xOffSet,
                    yOffSet);
        }
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
                    (float) mouse.getX(),
                    (float) mouse.getY(),
                    paint);
            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            mouseThread.sleep((long)(SAMPLING_RATE*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to be called in activity's {@link FragmentActivity#onPause()} method
     */
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

    /**
     * Method to be called in activity's {@link FragmentActivity#onResume()} method
     */
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

    /**
     * Gets the mouse object on the mouse view
     *
     * @return Mouse object to query positions
     */
    public Mouse getMouse(){
        return mouse;
    }

    /**
     * Gets reference Value used for calibration of the initial pitch value
     * @return Reference Value used for calibration of the initial pitch value
     */
    public double getRefPitch() {
        return refPitch;
    }

    /**
     * Sets the reference pitch added to the rest pitch of 0, when the phone is laid flat
     *
     * @param refPitch Reference Value used for calibration of the initial pitch value
     */
    public void setRefPitch(double refPitch) {
        this.refPitch = refPitch;
    }

    /**
     * @return Actual pitch value detected by sensors
     */
    public double getCurrentPitch() {
        return currentPitch;
    }

    /**
     * Toggles between position and velocity control of pointer,
     * velocity control is then default
     */
    public void toggleControl(){
        positionControl = !positionControl;
    }

    public ClickingMethod getClickingMethod() {
        return clickingMethod;
    }

    public void setClickingMethod(ClickingMethod clickingMethod) {
        this.clickingMethod = clickingMethod;
        this.enableOrDisableMovableFloatingActionButton(false);
        backTapService.stopService();

        switch(clickingMethod) {
            case BACK_TAP:
                backTapService.startService();
                break;
            case BEZEL_SWIPE:
                break;
            case VOLUME_DOWN:
                break;
            case FLOATING_BUTTON:
                this.enableOrDisableMovableFloatingActionButton(true);
                break;
            default:
                break;
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void click() {
        aLog("dad","CLICK");
        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = (float)mouse.getX();
        float y = (float)mouse.getY();

        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );

        // Dispatch touch event to view
        view.dispatchTouchEvent(motionEvent);

        metaState = 0;
        motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );

        // Dispatch touch event to view
        view.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        aLog("MouseView", "KEY EVENT DETECTED");
        if (clickingMethod == ClickingMethod.VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                click();
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                calibratePitch();
                Toast.makeText(getContext(),"Calibrated pitch to be "+ getRefPitch(),Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickingMethod == ClickingMethod.BEZEL_SWIPE) {

            aLog("Bezel", event.getX() + " " + event.getY());

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            int width = size.x;

            if (event.getX() < BEZEL_THRESHHOLD) {
                click();
                aLog("Bezel", "Touched left");
            } else if (event.getX() > width - BEZEL_THRESHHOLD) {
                click();
                aLog("Bezel", "Touched right");
            } else {
                aLog("Bezel", "Didn't touch bezel");
            }

        }
        return super.onTouchEvent(event);
    }

    /**
     * Sets the tilt gain value for position control
     * @param positionTiltGain Tilt gain value if position control is selected
     */
    public void setPosTiltGain(int positionTiltGain) {
        this.posTiltGain = positionTiltGain;
    }

    /**
     * Sets the tilt gain value for velocity control
     * @param velTiltGain Tilt gain value if position control is selected
     */
    public void setVelTiltGain(int velTiltGain) {
        this.velTiltGain = velTiltGain;
    }

    /**
     * Sets the point of reference for the mouse,
     * Position control: Base point of the mouse {initial point and point when mouse is returned to refence position}
     *
     * @param initialX
     */
    public void setXReference(double initialX) {
        this.initialX = initialX;
    }

    /**
     * Sets the point of reference for the mouse,
     * Position control: Base point of the mouse {initial point and point when mouse is returned to refence position}
     *
     * @param initialY
     */
    public void setYReference(double initialY) {
        this.initialY = initialY;
    }


    /**
     * Calibrate the accelerometer based pointer to use the current pitch as the reference point, resting position
     */
    public void calibratePitch(){
        setRefPitch(currentPitch);
    }
}
