package com.HPA.securehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WaterTracking extends AppCompatActivity implements View.OnClickListener {

    TextView trackersTextView;
    TextView exerciseTextView;
    TextView profileTextView;
    TextView waterCount;
    Button addWater;
    String email;
    String userString;
    int waterCountStore;
    List<User> userList = new ArrayList<User>();


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
            setContentView(R.layout.activity_water_tracking_small);
        else
            setContentView(R.layout.activity_water_tracking);

        trackersTextView = (TextView) findViewById(R.id.trackersTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        profileTextView = (TextView) findViewById(R.id.profileTextView);
        waterCount = (TextView) findViewById(R.id.glassesCount);
        addWater = (Button) findViewById(R.id.addWaterButton);
        setCounter();
        trackersTextView.setOnClickListener(this);
        exerciseTextView.setOnClickListener(this);
        profileTextView.setOnClickListener(this);
        addWater.setOnClickListener(this);

    }

    public void setCounter() {

        if(getEmailSharedPreferences()) {
            if(getListSharedPreferences()) {
                User user = checkInList(email);
                waterCount.setText(String.valueOf(user.getWaterCount()));
            }
        }
        else {
            if (getCountSharedPreferences()) {
                System.out.println("Got here");
                waterCount.setText(String.valueOf(waterCountStore));
            } else {
                System.out.println("Got in else");
            }
        }
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

            case R.id.addWaterButton:
               if(getEmailSharedPreferences()) {
                   if(getListSharedPreferences()) {
                       User user = checkInList(email);
                       if(user != null) {
                           user.setWaterCount(user.getWaterCount() + 1);
                           waterCount.setText(String.valueOf(user.getWaterCount()));
                           checkNSaveInList(email, user);
                       }
                   }
               }
                else {
                    if(getCountSharedPreferences()) {
                        System.out.println(getCountSharedPreferences());
                        waterCountStore = waterCountStore + 1;
                        waterCount.setText(String.valueOf(String.valueOf(waterCountStore)));
                        saveCountSharedPreferences(waterCountStore);
                    }
                   else {
                        int countstore = 1;
                        waterCount.setText(String.valueOf(String.valueOf(countstore)));
                        saveCountSharedPreferences(countstore);
                    }
               }

        }
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

    public void checkNSaveInList(String email, User user) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(int i=0; i<userArray.length; i++) {
                if(userArray[i].getEmail().equals(email)) {
                    userList.set(i, user);
                    saveListSharedPreferences(userList);
                    break;
                }
            }
        }
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

    public void saveListSharedPreferences(List counterList) {
        Gson gson = new Gson();
        userString = gson.toJson(counterList);
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("User List", userString).commit();
    }

    public boolean getCountSharedPreferences() {

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealthh", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("Water Count", -1) != -1) {
            waterCountStore = sharedPreferences.getInt("Water Count", -1);
            return true;
        }
        else
            return false;
    }

    public void saveCountSharedPreferences(int count) {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("Water Count", count).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getCountSharedPreferences()) {
            waterCountStore = 0;
            saveCountSharedPreferences(waterCountStore);
        }
    }
}
