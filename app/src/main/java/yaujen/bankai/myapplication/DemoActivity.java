package yaujen.bankai.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import yaujen.bankai.pointandclick.Clicker;
import yaujen.bankai.pointandclick.ClickingMethod;

import static android.widget.Toast.LENGTH_SHORT;

public class DemoActivity extends AppCompatActivity {
    public enum Tasks { Keyboard, Numpad, Wikipedia };

    // Dropdown Options
    public static final String[] CONTROL_METHODS = new String[]{"Position", "Velocity"};
    public static final String[] TILT_GAINS = new String[]{"10","15","20","25","30","35","40","45","50","55","60","65","70","75","80","85","90","95","100","105","110","115","120","125","130","135","140","145","150",};
    public String[] CLICKING_METHODS = new String[]{ClickingMethod.VOLUME_DOWN.name(),ClickingMethod.FLOATING_BUTTON.name(),ClickingMethod.BACK_TAP.name(),ClickingMethod.BEZEL_SWIPE.name() };
    public static final String[] TASKS = new String[]{Tasks.Keyboard.name(), Tasks.Numpad.name(), Tasks.Wikipedia.name()};

    // KEY 
    public static final String KEY_NAME_CONTROL_METHOD = "CONTROL_METHOD";
    public static final String KEY_NAME_TILT_GAIN = "TILT_GAIN";
    public static final String KEY_NAME_CLICKING_METHOD = "CLICKING_METHOD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Accel World");
        setSupportActionBar(toolbar);

        // Dropdown
        Spinner dropdownControlMethod = findViewById(R.id.control_method);
        Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
        Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
        Spinner dropdownTask = findViewById(R.id.task);

        // Adapters to describe how it is displayed
        ArrayAdapter<String> adapterControlMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CONTROL_METHODS);
        ArrayAdapter<String> adapterTiltGain = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TILT_GAINS);
        ArrayAdapter<String> adapterClickingMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CLICKING_METHODS);
        ArrayAdapter<String> adapterTask = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TASKS);

        //set the spinners adapter to the previously created one.
        dropdownControlMethod.setAdapter(adapterControlMethod);
        dropdownTiltGain.setAdapter(adapterTiltGain);
        dropdownClickingMethod.setAdapter(adapterClickingMethod);
        dropdownTask.setAdapter(adapterTask);

        dropdownControlMethod.setOnItemSelectedListener(new changeSelectedTiltGainBasedOnControlMethod());

        Button startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner dropdownControlMethod = findViewById(R.id.control_method);
                Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
                Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
                Spinner dropdownTask = findViewById(R.id.task);

                String controlMethod = dropdownControlMethod.getSelectedItem().toString();
                String tiltGain = dropdownTiltGain.getSelectedItem().toString();
                String clickingMethod = dropdownClickingMethod.getSelectedItem().toString();
                String task = dropdownTask.getSelectedItem().toString();

                // String message = "You chose: "+controlMethod+", "+tiltGain+", "+clickingMethod+", "+task;
                // DemoActivity.this.outputMessage(message);

                // SWITCHING TO DIFFERENT APP
                Intent myIntent = null;

                if(task.equals(Tasks.Keyboard.name())){
                    myIntent = new Intent(DemoActivity.this, KeyboardActivity.class);
                } else if(task.equals(Tasks.Numpad.name())){
                    myIntent = new Intent(DemoActivity.this, NumpadActivity.class);
                } else if(task.equals(Tasks.Wikipedia.name())){
                    myIntent = new Intent(DemoActivity.this, WikipediaActivity.class);
                }

                if(myIntent != null) {
                    myIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
                    myIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
                    myIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);
                    try {
                        DemoActivity.this.startActivity(myIntent);

                    } catch (Exception e){
                        DemoActivity.this.outputMessage(e.getMessage());
                    }
                } else {
                    DemoActivity.this.outputMessage("Intent is null!");
                }
            }
        });
    }

    /**
     * Toast Message being shown
     * @param message
     */
    public void outputMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Changing recommended tilt gain for different control method
     */
    public class changeSelectedTiltGainBasedOnControlMethod implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();

            if(selected != null && !selected.isEmpty()){
                Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);

                if(selected.equals("Position")){
                    dropdownTiltGain.setSelection(5);           // Tilt = 35
                } else if (selected.equals("Velocity")){
                    dropdownTiltGain.setSelection(18);          // Velocity = 100
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}
