package yaujen.bankai.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;


import static yaujen.bankai.pointandclick.Utility.aLog;

public class KeyboardActivity extends AppCompatActivity {

    private MouseView mouseView;
    private ConstraintLayout constraintLayout;
    private MovableFloatingActionButton movableButtonView;
    private TextView text;
    private TextView nextLetter;
    private String originalString = "the quick brown fox jumped over the lazy dog";
    private String textToWrite = "the quick brown fox jumped over the lazy dog";
    private int index = 0;
    private Button startButton;
    private long startTime;
    private int correctClicks;
    private int totalClicks;
    private boolean hasStarted = false;
    private Intent resultsIntent;
    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        text = findViewById(R.id.textToWrite);
        constraintLayout = findViewById(R.id.layout);
        nextLetter = findViewById(R.id.nextLetter);

        // How to use mouse view
        mouseView = new MouseView(this);
        constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());
        mouseView.setClickingTargetView(constraintLayout);

        // How to add fab clicking
        movableButtonView = new MovableFloatingActionButton(this);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
        mouseView.setMovableFloatingActionButton(movableButtonView);

         // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));


        aLog("Wikipedia", controlMethod);
        aLog("Wikipedia", clickingMethod);
        aLog("Wikipedia", tiltGain + "");

        mouseView.enablePositionControl(controlMethod.equals(DemoActivity.CONTROL_METHODS[0]));
        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        mouseView.setPosTiltGain(tiltGain);
        mouseView.setVelTiltGain(tiltGain);

        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasStarted) {
                    hasStarted = true;
                    startTime = System.currentTimeMillis();
                    colorString();
                    startButton.setText("");
                    startButton.setEnabled(false);
                } else if (textToWrite.length() == 0){
                    goToResults();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //pausing the mouse view when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        mouseView.pause();
    }

    //running the mouse view when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        mouseView.resume();
    }

    private void colorString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#000000\">");
        sb.append(originalString.substring(0,index));
        sb.append("</font>");
        sb.append("<font color=\"#A9A9A9\">");
        sb.append(originalString.substring(index));
        sb.append("</font>");

        aLog("Keyboard", sb.toString());
        text.setText(android.text.Html.fromHtml(sb.toString()));
    }

    public void onClicku(View view) {
        String letter = (String)((Button)view).getText();
        if (hasStarted) {
            totalClicks++;
        }

        if (hasStarted && textToWrite.length() > 0 && letter.equals(Character.toString(textToWrite.charAt(0)))) {
            index++;
            colorString();
            correctClicks++;

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
                startButton.setEnabled(true);
                startButton.setText("View results");
                long timeTaken = System.currentTimeMillis() - startTime;

                resultsIntent = new Intent(this, ResultsActivity.class);
                resultsIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
                resultsIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
                resultsIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

                resultsIntent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
                resultsIntent.putExtra(KEY_NAME_ERR_COUNT, totalClicks - correctClicks);
            }
        }

    }

    private void goToResults() {
        aLog("Keyboard", "Task finished: " + correctClicks + "/" + totalClicks);
        startActivity(resultsIntent);
    }
}
