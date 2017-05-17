package com.HPA.securehealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Hamza on 11/24/2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    Button signupButton;
    TextView continueTextView;
    String email = "";

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
            setContentView(R.layout.activity_main_small);
        else
            setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        continueTextView = (TextView) findViewById(R.id.continueTextView);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        continueTextView.setOnClickListener(this);

        if(getEmailSharedPreferences()) {
            finish();
            startActivity(new Intent(getBaseContext(), MainScreen.class));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.loginButton:
                finish();
                startActivity(new Intent(getBaseContext(), SignIn.class));
                break;

            case R.id.signupButton:
                finish();
                startActivity(new Intent(getBaseContext(), SignUp.class));
                break;

            case R.id.continueTextView:
                finish();
                if(getEmailSharedPreferences()) {
                    email = "";
                    saveEmailSharedPreferences();
                }
                startActivity(new Intent(getBaseContext(), MainScreen.class));
                break;
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

    public void saveEmailSharedPreferences() {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Logged In Email", null).commit();
    }

}
