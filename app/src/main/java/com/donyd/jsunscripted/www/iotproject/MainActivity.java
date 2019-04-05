package com.donyd.jsunscripted.www.iotproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Database";
    TextView tvTempDisplay, tvHumidityDisplay, tvRangeDisplay;
    SeekBar sbServoController;
    ToggleButton tgOverride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase references
        FirebaseDatabase FirebaseDB = FirebaseDatabase.getInstance();
        final DatabaseReference ManualOverride = FirebaseDB.getReference("ManualOverride");
        final DatabaseReference ServoAngle = FirebaseDB.getReference("ServoAngle");
        DatabaseReference SensorReads = FirebaseDB.getReference("sensor_data");

        // UI references
        tvTempDisplay = (TextView) findViewById(R.id.tvTempDisplay);
        tvHumidityDisplay = (TextView) findViewById(R.id.tvHumidityDisplay);
        tvRangeDisplay = (TextView) findViewById(R.id.tvRangeDisplay);
        sbServoController = (SeekBar) findViewById(R.id.seekBarServoController);
        tgOverride = (ToggleButton) findViewById(R.id.tbManualOverride);


        // Read values from firebase
        SensorReads.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // Get individual datafields from firebase
                String temperature = dataSnapshot.child("temperature").getValue(String.class);
                String humidity = dataSnapshot.child("humidity").getValue(String.class);
                String range = dataSnapshot.child("distance").getValue(String.class);

                // Set appropriate textviews in Android with values retrieved
                tvTempDisplay.setText(temperature);
                tvHumidityDisplay.setText(humidity);
                tvRangeDisplay.setText(range);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Manual control activation
        tgOverride.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    ManualOverride.setValue("ON");
                    sbServoController.setVisibility(View.VISIBLE);
                } else {
                    ManualOverride.setValue("OFF");
                    sbServoController.setVisibility(View.GONE);
                }

            }
        });


        // Manual override logic
        sbServoController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int angle = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int angle, boolean b) {
                ServoAngle.setValue(angle);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
