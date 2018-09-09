package yaujen.bankai.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements Clicker {
    private MouseView mouseView;
    private TextView someTxt;

    public Handler messageHandler;
    protected ServiceConnection mServerConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mouseView = findViewById(R.id.mouseView);
        someTxt = findViewById(R.id.randoTxt);

        messageHandler = new MessageHandler(this);
        mServerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                System.out.println("service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                System.out.println("service disconnected");
            }
        };

        ComponentName componentName = new ComponentName("com.prhlt.aemus.BoDTapService",
                "com.prhlt.aemus.BoDTapService.BoDTapService");
        Intent intent = new Intent();
        intent.putExtra("MESSENGER", new Messenger(messageHandler));
        intent.setComponent(componentName);

        ComponentName c = this.getApplication().startService(intent);
        if (c == null) {
            System.out.println("Failed to start BoDTapService");
        } else {
            System.out.println("BoDTap Service started");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ComponentName componentName = new ComponentName("com.prhlt.aemus.BoDTapService",
                "com.prhlt.aemus.BoDTapService.BoDTapService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        this.getApplication().stopService(intent);
        this.getApplication().unbindService(mServerConn);
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
            click();
        }
        return true;
    }

    @Override
    public void click() {
        Mouse mouse = mouseView.getMouse();

        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = mouse.getX();
        float y = mouse.getY();

        DecimalFormat df2 = new DecimalFormat(".##");

        someTxt.setText("("+x+", "+y+") p="+mouse.pitch+", r="+mouse.roll+", an°="+df2.format(mouse.dir));

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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            mouseView.setRefPitch(mouseView.getCurrentPitch());

            Toast.makeText(this,"Calibrated pitch to be "+mouseView.getRefPitch(),Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
