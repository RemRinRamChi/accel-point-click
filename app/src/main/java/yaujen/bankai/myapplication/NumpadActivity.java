package yaujen.bankai.myapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.Utility;

import static yaujen.bankai.pointandclick.Utility.aLog;

public class NumpadActivity extends AppCompatActivity {
    private MouseView mouseView;
    private TextView numberField;
    private ConstraintLayout constraintLayout;

    private String numberToEnter = "7586423109";
    private int errorCount = 0;

    private boolean start = true;
    private boolean finish = false;
    private long startTime = 0;


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

        Bundle extras = getIntent().getExtras();
        String controlMethod = extras.getString(DemoActivity.KEY_NAME_CONTROL_METHOD);
        String clickingMethod = extras.getString(DemoActivity.KEY_NAME_CLICKING_METHOD);
        String tiltGain = extras.getString(DemoActivity.KEY_NAME_TILT_GAIN);

        aLog("Numpad", controlMethod);
        aLog("Numpad", clickingMethod);
        aLog("Numpad", tiltGain);

        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));
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

    public void onStartClicku(View view){
        if(start){
            start = false;
            numberField.setText(" • • • • • • • • • •");
            startTime = System.currentTimeMillis();
        } else {
            incrementErrorCount();
        }
    }

    public void onFinishClicku(View view){
        if(finish){
            long timeTaken = System.currentTimeMillis() - startTime;
            Toast.makeText(this,"Time: "+timeTaken+", err: "+errorCount,Toast.LENGTH_SHORT).show();
        } else {
            incrementErrorCount();
        }
    }

    public void onClicku(View view){
        if(!start && !finish){
            if(!numberToEnter.equals("")){
                char clickuChar = ((Button)view).getText().charAt(0);
                Utility.aLog("hmm",clickuChar+"");
                if(numberToEnter.charAt(0) == clickuChar){
                    if(numberToEnter.length() != 1){
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        numberToEnter = numberToEnter.substring(1);
                    } else {
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        finish = true;
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
            Utility.aLog("mag",errorCount+"");
        }
    }
}
