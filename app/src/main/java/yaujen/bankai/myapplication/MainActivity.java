package yaujen.bankai.myapplication;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static yaujen.bankai.myapplication.Utility.dF2;

public class MainActivity extends AppCompatActivity {

    private MouseView mouseView;
    private TextView someTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mouseView = findViewById(R.id.mouseView);
        someTxt = findViewById(R.id.randoTxt);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mouseView.toggleControl();
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
            Mouse mouse = mouseView.getMouse();

            // Obtain MotionEvent object
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis() + 100;
            float x = (float)mouse.getX();
            float y = (float)mouse.getY();

            someTxt.setText("("+dF2(x)+", "+dF2(y)+") p="+dF2(mouse.pitch)+", r="+dF2(mouse.roll)+", an°="+dF2(mouse.dir));

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
            findViewById(R.id.alpha).dispatchTouchEvent(motionEvent);

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
            findViewById(R.id.alpha).dispatchTouchEvent(motionEvent);

        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            mouseView.setRefPitch(mouseView.getCurrentPitch());

            Toast.makeText(this,"Calibrated pitch to be "+mouseView.getRefPitch(),Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
