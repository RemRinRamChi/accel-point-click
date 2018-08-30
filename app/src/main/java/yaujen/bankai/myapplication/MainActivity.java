package yaujen.bankai.myapplication;

import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MouseView mouseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mouseView = new MouseView(this);
        //setContentView(mouseView);
        setContentView(R.layout.activity_main);
        mouseView = findViewById(R.id.mouseView);

        final long downTime = SystemClock.uptimeMillis();
        final long eventTime = SystemClock.uptimeMillis() + 100;
        final ArrayList<Integer> hihi = new ArrayList<Integer>();
        hihi.add(0);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hihi.set(0,hihi.get(0)+1);
                Toast.makeText(MainActivity.this,"Hi Again "+hihi.get(0),Toast.LENGTH_SHORT).show();
                // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
                int metaState = 0;
                MotionEvent motionEvent = MotionEvent.obtain(
                        downTime,
                        eventTime,
                        MotionEvent.ACTION_UP,
                        100,
                        100,
                        metaState
                );

                // Dispatch touch event to view
                findViewById(R.id.button).dispatchTouchEvent(motionEvent);

            }
        });


    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        mouseView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        mouseView.resume();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //mouseView.dispatchKeyEvent(event);

            Mouse mouse = mouseView.getMouse();

            // Obtain MotionEvent object
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis() + 100;
            float x = mouse.getX();
            float y = mouse.getY();

            Toast.makeText(this, "Down clicked at "+x+", "+y, Toast.LENGTH_SHORT).show();

            // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
            int metaState = 0;
            MotionEvent motionEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_UP,
                    x,
                    y,
                    metaState
            );

            // Dispatch touch event to view
            findViewById(R.id.button).dispatchTouchEvent(motionEvent);

        }
        return true;
    }

 /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
        (findViewById(R.id.alpha)).dispatchTouchEvent(event);

        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                Log.e("Hi","ACTION_DOWN AT COORDS "+"X: "+X+" Y: "+Y);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("Hi","MOVE "+"X: "+X+" Y: "+Y);
                break;

            case MotionEvent.ACTION_UP:
                Log.e("Hi","ACTION_UP "+"X: "+X+" Y: "+Y);
                break;
        }
        return true;
    }*/
}
