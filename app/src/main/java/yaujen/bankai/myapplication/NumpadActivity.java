package yaujen.bankai.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;
import yaujen.bankai.pointandclick.Utility;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;

public class NumpadActivity extends AppCompatActivity {
    private MouseView mouseView;
    private TextView numberField;
    private ConstraintLayout constraintLayout;

    private String numberToEnter = "7586423109";
    private int errorCount = 0;

    private boolean start = true;
    private boolean finish = false;
    private long startTime = 0;


    String controlMethod ;
    String clickingMethod;
    int tiltGain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numpad);
        constraintLayout = findViewById(R.id.layout);
        numberField = findViewById(R.id.numField);

        // How to use mouse view
        mouseView = new MouseView(this);
        constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());
        mouseView.setClickingTargetView(findViewById(R.id.layout));


        // How to add fab clicking
        MovableFloatingActionButton movableButtonView = new MovableFloatingActionButton(this);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
        mouseView.setMovableFloatingActionButton(movableButtonView);

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod  = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));

        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        mouseView.enablePositionControl(controlMethod.equals(DemoActivity.CONTROL_METHODS[0]));
        mouseView.setPosTiltGain(tiltGain);
        mouseView.setVelTiltGain(tiltGain);
        mouseView.enableRecalibrationByVolumeUp(true);
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

    public void onStartClicku(View view){
        if(start){
            start = false;
            numberField.setText(" • • • • • • • • • •");
            findViewById(R.id.goBtn).setVisibility(View.INVISIBLE);
            startTime = System.currentTimeMillis();
        } else {
            incrementErrorCount();
        }
    }

    public void onFinishClicku(View view){
        if(finish){
            long timeTaken = System.currentTimeMillis() - startTime;
            Utility.aLog("Time taken",timeTaken+"");

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            intent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
            intent.putExtra(KEY_NAME_ERR_COUNT, errorCount);

            startActivity(intent);
        } else {
            incrementErrorCount();
        }
    }

    public void onClicku(View view){
        if(!start && !finish){
            if(!numberToEnter.equals("")){
                char clickuChar = ((Button)view).getText().charAt(0);
                Utility.aLog("Key clicked",clickuChar+"");
                if(numberToEnter.charAt(0) == clickuChar){
                    if(numberToEnter.length() != 1){
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        numberToEnter = numberToEnter.substring(1);
                    } else {
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        finish = true;
                        findViewById(R.id.endBtn).setVisibility(View.VISIBLE);
                    }
                } else {
                    incrementErrorCount();
                }
            }
        } else if (finish){
            incrementErrorCount();
        }
    }

    public void onBadClicku(View view){
        incrementErrorCount();
    }

    private void incrementErrorCount(){
        if(!start){
            errorCount++;
            Utility.aLog("Err count",errorCount+"");
        }
    }
}
