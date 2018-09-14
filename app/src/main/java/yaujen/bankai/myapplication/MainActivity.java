package yaujen.bankai.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static yaujen.bankai.myapplication.Utility.aLog;
import static yaujen.bankai.myapplication.Utility.dF2;


public class MainActivity extends AppCompatActivity {
    private MouseView mouseView;
    private TextView someTxt;
    private ConstraintLayout constraintLayout;

    private MovableFloatingActionButton movableButtonView;

    private boolean cursorToggler = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.layout);

        mouseView = new MouseView(this);
        movableButtonView = new MovableFloatingActionButton(this);

        ConstraintLayout.LayoutParams fullScreenParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);

        fullScreenParams.leftToLeft = 0;
        fullScreenParams.topToTop = 0;
        fullScreenParams.leftMargin = 0;
        fullScreenParams.topMargin = 0;

        ConstraintLayout.LayoutParams fabParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        fabParams.rightToRight = 0;
        fabParams.topToTop = 100;

        constraintLayout.addView(mouseView, -1, fullScreenParams);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),fabParams);

        mouseView.setMovableFloatingActionButton(movableButtonView);

        mouseView.setClickingMethod(ClickingMethod.VOLUME_DOWN);
        mouseView.setView(findViewById(R.id.alpha));
        mouseView.setFocusable(true);

        someTxt = findViewById(R.id.randoTxt);
        someTxt.setText("Current clicking method: Volume Down");

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
                    someTxt.setText("Current clicking method: Volume Down");
                    Toast.makeText(view.getContext(),"Clicking method switched to Volume Down", Toast.LENGTH_SHORT).show();
                } else if (mouseView.getClickingMethod() == ClickingMethod.VOLUME_DOWN){
                    mouseView.setClickingMethod(ClickingMethod.BEZEL_SWIPE);
                    someTxt.setText("Current clicking method: Bezel Swipe");
                    Toast.makeText(view.getContext(),"Clicking method switched to Bezel Swipe", Toast.LENGTH_SHORT).show();
                } else if (mouseView.getClickingMethod() == ClickingMethod.BEZEL_SWIPE){
                    mouseView.setClickingMethod(ClickingMethod.FLOATING_BUTTON);
                    someTxt.setText("Current clicking method: Floating Button");
                    Toast.makeText(view.getContext(),"Clicking method switched to Floating Button", Toast.LENGTH_SHORT).show();
                } else {
                    mouseView.setClickingMethod(ClickingMethod.BACK_TAP);
                    someTxt.setText("Current clicking method: Back Tap");
                    Toast.makeText(view.getContext(),"Clicking method switched to Back Tap", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((Button)findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursorToggler){
                    mouseView.setMouseBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.seibacur));
                } else {
                    mouseView.setMouseBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cursor));
                }
                cursorToggler = !cursorToggler;

            }
        });



        // MovableFloatingActionButton mFab = findViewById(R.id.mFab);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
