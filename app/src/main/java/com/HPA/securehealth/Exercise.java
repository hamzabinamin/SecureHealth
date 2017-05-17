package com.HPA.securehealth;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Exercise extends AppCompatActivity implements View.OnClickListener {

    TextView trackersTextView;
    TextView exerciseTextView;
    TextView profileTextView;
    ImageView walking;
    ImageView running;

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
            setContentView(R.layout.activity_exercise_small);
        else
            setContentView(R.layout.activity_exercise);

        trackersTextView = (TextView) findViewById(R.id.trackersTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        profileTextView = (TextView) findViewById(R.id.profileTextView);
        walking = (ImageView) findViewById(R.id.walking);
        running = (ImageView) findViewById(R.id.running);
        trackersTextView.setOnClickListener(this);
        exerciseTextView.setOnClickListener(this);
        profileTextView.setOnClickListener(this);
        walking.setOnClickListener(this);
        running.setOnClickListener(this);
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

            case R.id.running:
                finish();
                startActivity(new Intent(getBaseContext(), Running.class));
                break;

            case R.id.walking:
                finish();
                startActivity(new Intent(getBaseContext(), PedometerActivity.class));
                break;

        }
    }
}