package yaujen.bankai.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    public static final String KEY_NAME_TIME_TAKEN = "TIME_TAKEN";
    public static final String KEY_NAME_ERR_COUNT = "ERR_COUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        String controlMethod = extras.getString(DemoActivity.KEY_NAME_CONTROL_METHOD);
        String clickingMethod = extras.getString(DemoActivity.KEY_NAME_CLICKING_METHOD);
        int tiltGain = extras.getInt(DemoActivity.KEY_NAME_TILT_GAIN);
        String timeTaken = extras.getString(ResultsActivity.KEY_NAME_TIME_TAKEN);
        int errCount = extras.getInt(ResultsActivity.KEY_NAME_ERR_COUNT);

        ((TextView)findViewById(R.id.controlModeView)).setText("Control Mode: "+controlMethod);
        ((TextView)findViewById(R.id.clickingMethodView)).setText("Clicking Method: "+clickingMethod);
        ((TextView)findViewById(R.id.tiltGainView)).setText("Tilt Gain: "+tiltGain);
        ((TextView)findViewById(R.id.timeView)).setText("Time Taken: "+timeTaken);
        ((TextView)findViewById(R.id.errView)).setText("Error Count: "+errCount);
    }

    public void onFinishClicked(View view){
        Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
