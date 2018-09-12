package yaujen.bankai.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static yaujen.bankai.myapplication.Utility.dF2;


public class MainActivity extends AppCompatActivity {
    private MouseView mouseView;
    private TextView someTxt;
    private BackTapService backTapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mouseView = findViewById(R.id.mouseView);
        mouseView.setClickingMethod(ClickingMethod.VOLUME_DOWN);
        mouseView.setView(findViewById(R.id.alpha));
        mouseView.setFocusable(true);

        someTxt = findViewById(R.id.randoTxt);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mouseView.toggleControl();
            }
        });

        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mouseView.getClickingMethod() == ClickingMethod.BACK_TAP) {
                    mouseView.setClickingMethod(ClickingMethod.VOLUME_DOWN);
                    Toast.makeText(view.getContext(),"Clicking method switched to Volume Down", Toast.LENGTH_SHORT).show();
                } else {
                    mouseView.setClickingMethod(ClickingMethod.BACK_TAP);
                    Toast.makeText(view.getContext(),"Clicking method switched to Back Tap", Toast.LENGTH_SHORT).show();

                }
            }
        });
        
        
        MovableFloatingActionButton myFab = findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //click();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backTapService.stopService();
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



}
