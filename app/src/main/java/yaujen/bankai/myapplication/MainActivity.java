package yaujen.bankai.myapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.Mouse;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;


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

        // How to use mouse view
        mouseView = new MouseView(this);
        constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());
        mouseView.setClickingTargetView(findViewById(R.id.alpha));

        // How to add fab clicking
        movableButtonView = new MovableFloatingActionButton(this);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
        mouseView.setMovableFloatingActionButton(movableButtonView);


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
                    mouseView.setMouseBitmap(Mouse.getMouseBitmap2(MainActivity.this));
                } else {
                    mouseView.setMouseBitmap(Mouse.getMouseBitmap1(MainActivity.this));
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
