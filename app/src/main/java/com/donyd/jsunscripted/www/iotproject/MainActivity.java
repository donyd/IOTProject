package com.donyd.jsunscripted.www.iotproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Database";
    TextView tvTempDisplay, tvHumidityDisplay, tvRangeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        FirebaseDatabase FirebaseDB = FirebaseDatabase.getInstance();
//        DatabaseReference TempReading = FirebaseDB.getReference("temperature");
//        DatabaseReference HumidityReading = FirebaseDB.getReference("humidity");
//        DatabaseReference RangeReading = FirebaseDB.getReference("range");
        DatabaseReference SensorReads = FirebaseDB.getReference("sensor_data");

        // UI references
        tvTempDisplay = (TextView) findViewById(R.id.tvTempDisplay);
        tvHumidityDisplay = (TextView) findViewById(R.id.tvHumidityDisplay);
        tvRangeDisplay = (TextView) findViewById(R.id.tvRangeDisplay);


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

    }
}
