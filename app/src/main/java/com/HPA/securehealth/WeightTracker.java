package com.HPA.securehealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WeightTracker extends AppCompatActivity implements View.OnClickListener {

    Button weightButton;
    TextView trackersTextView;
    TextView exerciseTextView;
    TextView profileTextView;
    TextView weightTextView;
    TextView weightStatus;
    EditText weightEditText;
    EditText heightEditText;
    List<User> userList = new ArrayList<User>();
    String email = "";
    String userString = "";

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
            setContentView(R.layout.activity_weight_tracker_small);
        else
            setContentView(R.layout.activity_weight_tracker);

        weightButton = (Button) findViewById(R.id.weightButton);
        trackersTextView = (TextView) findViewById(R.id.trackersTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        profileTextView = (TextView) findViewById(R.id.profileTextView);
        weightTextView = (TextView) findViewById(R.id.weightText);
        weightStatus = (TextView) findViewById(R.id.weightStatus);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);
        trackersTextView.setOnClickListener(this);
        exerciseTextView.setOnClickListener(this);
        profileTextView.setOnClickListener(this);
        weightButton.setOnClickListener(this);
        weightEditText.setOnClickListener(this);
        heightEditText.setOnClickListener(this);
        weightEditText.setFocusable(false);
        heightEditText.setFocusable(false);

        if(getEmailSharedPreferences()) {
           weightEditText.setVisibility(View.INVISIBLE);
           heightEditText.setVisibility(View.INVISIBLE);
           weightButton.setVisibility(View.INVISIBLE);
            if(checkInList(email) != null) {
                User user = checkInList(email);
                if(user.getWeight().length() != 0 || user.getHeight().length() != 0) {
                    String[] weightString = user.getWeight().split("-");
                    System.out.println(weightString[0] + " " + weightString[1]);
                    String[] heightString = user.getHeight().split("-");
                    System.out.println(heightString[0] + " " + heightString[1]);
                    weightString[1] = weightString[1].replace("kg", "");
                    heightString[1] = heightString[1].replace("cm", "");
                    double weight = (Double.parseDouble(weightString[0]) + Double.parseDouble(weightString[1])) / 2.0;
                    double height1 = (Double.parseDouble(heightString[0]) + Double.parseDouble(heightString[1])) / 2.0;
                    weightTextView.setText(user.getWeight());
                    double bmi = calculateBMI(weight, height1);

                    if (bmi < 18.5) {
                        weightStatus.setText("Your BMI level is low");
                    } else if (bmi >= 18.5 && bmi <= 24.9) {
                        weightStatus.setText("Your BMI level is normal");
                    } else if (bmi >= 25 && bmi <= 29.9) {
                        weightStatus.setText("Your BMI suggests that you're overweight");
                    } else if (bmi >= 30) {
                        weightStatus.setText("Your BMI suggests that you're obese");
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Please enter your weight/height from Profile page", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            Toast.makeText(getBaseContext(), "Please enter your Weight and Height in the two fields", Toast.LENGTH_LONG).show();
            weightEditText.setVisibility(View.VISIBLE);
            heightEditText.setVisibility(View.VISIBLE);
            weightButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public double calculateBMI(double weight, double height) {

        double heightInInches = height * 0.393701;
        System.out.println("Height in Inches: " + heightInInches);
        double heightInMeters = heightInInches *  0.025;
        System.out.println("Height in Meters: " + heightInMeters);
        double squaredHeight = heightInMeters * heightInMeters;
        System.out.println("Height Squared: " + squaredHeight);
        double BMI = weight/squaredHeight;
        System.out.println("BMI: " + BMI);
        return BMI;
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

    public User checkInList(String email) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    return u;
                }
            }
        }
        return null;
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

            case R.id.weightEditText:
                weightEditText.requestFocus();
                weightEditText.setFocusableInTouchMode(true);
                break;

            case R.id.heightEditText:
                heightEditText.requestFocus();
                heightEditText.setFocusableInTouchMode(true);
                break;

            case R.id.weightButton:
                if(weightEditText.getText().toString().length() != 0 && heightEditText.getText().toString().length() != 0) {

                    double bmi = calculateBMI(Double.parseDouble(weightEditText.getText().toString()), Double.parseDouble(heightEditText.getText().toString()));

                    weightTextView.setText(weightEditText.getText().toString() + " KG");
                    heightEditText.setText(heightEditText.getText().toString());

                    if(bmi < 18.5) {
                        weightStatus.setText("Your BMI level is low");
                    }
                    else if( bmi >= 18.5 && bmi <= 24.9) {
                        weightStatus.setText("Your BMI level is normal");
                    }
                    else if( bmi >= 25 && bmi <= 29.9) {
                        weightStatus.setText("Your BMI suggests that you're overweight");
                    }
                    else if( bmi >= 30) {
                        weightStatus.setText("Your BMI suggests that you're obese");
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Please fill the required fields first", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
