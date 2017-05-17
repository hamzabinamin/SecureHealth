package com.HPA.securehealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    TextView trackersTextView;
    TextView exerciseTextView;
    TextView profileTextView;
    ImageView stepCounter;
    ImageView waterTracking;
    ImageView heartrateTracking;
    ImageView weightTracking;

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

        if (screenInches <= 4)
            setContentView(R.layout.activity_main_screen_small);
        else
            setContentView(R.layout.activity_main_screen);

        trackersTextView = (TextView) findViewById(R.id.trackersTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        profileTextView = (TextView) findViewById(R.id.profileTextView);
        stepCounter = (ImageView) findViewById(R.id.stepCounter);
        waterTracking = (ImageView) findViewById(R.id.waterTracker);
        heartrateTracking = (ImageView) findViewById(R.id.heartrateTracker);
        weightTracking = (ImageView) findViewById(R.id.weightTracker);

        trackersTextView.setOnClickListener(this);
        exerciseTextView.setOnClickListener(this);
        profileTextView.setOnClickListener(this);
        stepCounter.setOnClickListener(this);
        waterTracking.setOnClickListener(this);
        heartrateTracking.setOnClickListener(this);
        weightTracking.setOnClickListener(this);
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

            case R.id.stepCounter:
                finish();
                startActivity(new Intent(getBaseContext(), StepCounter.class));
                break;

            case R.id.waterTracker:
                finish();
                startActivity(new Intent(getBaseContext(), WaterTracking.class));
                break;

            case R.id.heartrateTracker:
                finish();
                startActivity(new Intent(getBaseContext(), Home.class));
                break;

            case R.id.weightTracker:
                finish();
                startActivity(new Intent(getBaseContext(), WeightTracker.class));
                break;
        }
    }
}
