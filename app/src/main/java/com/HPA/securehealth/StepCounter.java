package com.HPA.securehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class StepCounter extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    TextView stepCount;
    TextView caloriesBurnt;
    TextView distanceCovered;
    TextView trackersTextView;
    TextView exerciseTextView;
    TextView profileTextView;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    static double weight = 67.0; // kg
    static double height = 178.0; // cm
    static double stepsCount = 4793;
    final static double walkingFactor = 0.57;
    static double CaloriesBurnedPerMile;
    static double strip;
    static double stepCountMile; // step/mile
    static double conversationFactor;
    static double CaloriesBurned;
    static NumberFormat formatter = new DecimalFormat("#0.00");
    static double distance;
    String email;
    String userString;
    List<User> userList = new ArrayList<User>();
    int store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);
        strip = height * 0.415;
        stepCountMile = 160934.4 / strip;
        conversationFactor = CaloriesBurnedPerMile / stepCountMile;

        if (screenInches <= 4)
            setContentView(R.layout.activity_step_counter_small);
        else
            setContentView(R.layout.activity_step_counter);

        stepCount = (TextView) findViewById(R.id.stepCount);
        caloriesBurnt = (TextView) findViewById(R.id.caloriesBurntText);
        distanceCovered = (TextView) findViewById(R.id.distanceText);
        trackersTextView = (TextView) findViewById(R.id.trackersTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        profileTextView = (TextView) findViewById(R.id.profileTextView);
        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        trackersTextView.setOnClickListener(this);
        exerciseTextView.setOnClickListener(this);
        profileTextView.setOnClickListener(this);
    }

    public void calculate() {
        if(getEmailSharedPreferences()) {
            if (getListSharedPreferences()) {
                User user = findInList(email);
                if(user != null) {
                    weight = Double.parseDouble(user.getWeight());
                    height = Double.parseDouble(user.getHeight());

                    CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);
                    strip = height * 0.415;
                    stepCountMile = 160934.4 / strip;
                    conversationFactor = CaloriesBurnedPerMile / stepCountMile;
                    CaloriesBurned = stepsCount * conversationFactor;
                    distance = (stepsCount * strip) / 100000;

                    caloriesBurnt.setText(formatter.format(CaloriesBurned) + " cals");
                    distanceCovered.setText(formatter.format(distance) + " km");
                }
                else {
                    CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);
                    strip = height * 0.415;
                    stepCountMile = 160934.4 / strip;
                    conversationFactor = CaloriesBurnedPerMile / stepCountMile;
                    CaloriesBurned = stepsCount * conversationFactor;
                    distance = (stepsCount * strip) / 100000;

                    caloriesBurnt.setText(formatter.format(CaloriesBurned) + " cals");
                    distanceCovered.setText(formatter.format(distance) + " km");
                }

            }

            }

        }


    public User findInList(String email) {

        User[] userArray = userList.toArray(new User[userList.size()]);

        for(User u: userArray) {

            if(u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount.setText( value + " steps so far");
            store= value;
            calculate();
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
           // stepCount.setText("Step Detector Detected : " + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public boolean getEmailSharedPreferences() {

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("Logged In Email", null) != null) {
            email = sharedPreferences.getString("Logged In Email", null);
            return true;
        }
        else
            return false;
    }

    public boolean getListSharedPreferences() {

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("User List", null) != null) {
            userString = sharedPreferences.getString("User List", null);
            Gson gson = new Gson();
            TypeToken<List<User>> token = new TypeToken<List<User>>() {};
            userList = gson.fromJson(userString, token.getType());
            return true;
        }
        else
            return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.trackersTextView:
                finish();
                startActivity(new Intent(getBaseContext(), MainScreen.class));
                break;

            case R.id.exerciseTextView:
                finish();
                startActivity(new Intent(getBaseContext(), Exercise.class));
                break;

            case R.id.profileTextView:
                finish();
                startActivity(new Intent(getBaseContext(), Profile.class));
                break;
        }

    }
}
