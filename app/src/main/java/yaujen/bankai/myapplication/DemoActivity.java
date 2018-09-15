package yaujen.bankai.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Dropdown
        Spinner dropdownControlMethod = findViewById(R.id.control_method);
        Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
        Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
        Spinner dropdownTask = findViewById(R.id.task);

        // Dropdown Options
        String[] itemsControlMethod = new String[]{"Position", "Velocity"};
        String[] itemsTiltGain = new String[]{"10", "15", "20", "25", "30", "35", "40", "45", "50"};
        //List<ClickingMethod> clickingMethods = Arrays.asList(ClickingMethod.values());
        String[] itemsClickingMethod = new String[]{ "VOLUME_DOWN", "FLOATING_BUTTON", "BACK_TAP", "BEZEL_SWIPE" };
        //String[] itemsClickingMethod = new String[]{ClickingMethod.values().};
        String[] itemsTask = new String[]{"Calculator"};

        // Adapters to describe how it is displayed
        ArrayAdapter<String> adapterControlMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsControlMethod);
        ArrayAdapter<String> adapterTiltGain = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsTiltGain);
        ArrayAdapter<String> adapterClickingMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsClickingMethod);
        ArrayAdapter<String> adapterTask = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsTask);

        //set the spinners adapter to the previously created one.
        dropdownControlMethod.setAdapter(adapterControlMethod);
        dropdownTiltGain.setAdapter(adapterTiltGain);
        dropdownClickingMethod.setAdapter(adapterClickingMethod);
        dropdownTask.setAdapter(adapterTask);

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

                String message = "You chose: "+controlMethod+", "+tiltGain+", "+clickingMethod+", "+task;
                DemoActivity.this.outputMessage(message);

                Intent myIntent = new Intent(DemoActivity.this, NextActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                CurrentActivity.this.startActivity(myIntent);
            }
        });
    }
    public void outputMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
