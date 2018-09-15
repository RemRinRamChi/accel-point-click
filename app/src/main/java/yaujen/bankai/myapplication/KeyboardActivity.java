package yaujen.bankai.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;
import yaujen.bankai.pointandclick.Utility;

import static yaujen.bankai.pointandclick.Utility.aLog;

public class KeyboardActivity extends AppCompatActivity {

    private MouseView mouseView;
    private ConstraintLayout constraintLayout;
    private MovableFloatingActionButton movableButtonView;
    private TextView text;
    private TextView nextLetter;
    private String textToWrite = "the quick brown fox jumped over the lazy dog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        text = findViewById(R.id.input);
        constraintLayout = findViewById(R.id.layout);
        nextLetter = findViewById(R.id.nextLetter);

        // How to use mouse view
        mouseView = new MouseView(this);
        constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());
        mouseView.setClickingTargetView(findViewById(R.id.alpha));

        // How to add fab clicking
        movableButtonView = new MovableFloatingActionButton(this);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
        mouseView.setMovableFloatingActionButton(movableButtonView);

        Bundle extras = getIntent().getExtras();
        String controlMethod = extras.getString(DemoActivity.KEY_NAME_CONTROL_METHOD);
        String clickingMethod = extras.getString(DemoActivity.KEY_NAME_CLICKING_METHOD);
        String tiltGain = extras.getString(DemoActivity.KEY_NAME_TILT_GAIN);

        aLog("Keyboard", controlMethod);
        aLog("Keyboard", clickingMethod);
        aLog("Keyboard", tiltGain);

        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));
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

    public void onClicku(View view) {
        String letter = (String)((Button)view).getText();

        if (textToWrite.length() > 0 && letter.equals(Character.toString(textToWrite.charAt(0)))) {
            text.append(letter);

            textToWrite = textToWrite.substring(1);

            char nextChar = ' ';

            if(textToWrite.length() > 0) {
                nextChar = textToWrite.charAt(0);
            }

            if (nextChar == ' ') {
                nextChar = '_';
            }

            nextLetter.setText("Next Letter: " + nextChar);

            if (textToWrite.length() == 0) {
                nextLetter.setText("Done!");
            }
        } else {

        }


    }
}
