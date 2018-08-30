package yaujen.bankai.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MouseView extends SurfaceView implements Runnable {

    volatile boolean mousing;

    private Thread mouseThread = null;

    private Mouse mouse;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

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
        mouse.update();
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
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        mousing = true;
        mouseThread = new Thread(this);
        mouseThread.start();
    }

    public Mouse getMouse(){
        return mouse;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("Sawa","sawa");
        // todo NOT EVEN HERE
        return false;
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            // Obtain MotionEvent object
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis() + 100;
            float x = mouse.getX();
            float y = mouse.getY();

            Toast.makeText(getContext(), "Hi hi volume down clicked at "+x+";"+y, Toast.LENGTH_SHORT).show();

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
            this.dispatchTouchEvent(motionEvent);
        }
        return true;
    }
*/

}
