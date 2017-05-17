package com.HPA.securehealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button backButton;
    EditText emailEditText;
    EditText passwordEditText;
    EditText repeatPasswordEditText;
    Button signupButton;
    List<User> userList;
    String userString;

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
            setContentView(R.layout.activity_sign_up_small);
        else
            setContentView(R.layout.activity_sign_up);

        backButton = (Button) findViewById(R.id.backButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        repeatPasswordEditText = (EditText) findViewById(R.id.repeatpasswordEditText);
        repeatPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        signupButton = (Button) findViewById(R.id.signupButton);
        userList = new ArrayList<User>();
        backButton.setOnClickListener(this);
        emailEditText.setOnClickListener(this);
        passwordEditText.setOnClickListener(this);
        repeatPasswordEditText.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        emailEditText.setFocusable(false);
        passwordEditText.setFocusable(false);
        repeatPasswordEditText.setFocusable(false);
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backButton:
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                break;

            case R.id.emailEditText:
                emailEditText.requestFocus();
                emailEditText.setFocusableInTouchMode(true);
                break;

            case R.id.passwordEditText:
                passwordEditText.requestFocus();
                passwordEditText.setFocusableInTouchMode(true);
                break;

            case R.id.repeatpasswordEditText:
                repeatPasswordEditText.requestFocus();
                repeatPasswordEditText.setFocusableInTouchMode(true);
                break;

            case R.id.signupButton:
                if(validation()) {
                    if(getListSharedPreferences()) {
                        userList.add(new User(emailEditText.getText().toString(), passwordEditText.getText().toString()));
                    }
                    else {
                        userList.add(new User(emailEditText.getText().toString(), passwordEditText.getText().toString()));
                    }
                    saveListSharedPreferences(userList);
                    finish();
                    startActivity(new Intent(getBaseContext(), SignIn.class));
                }
        }
    }

    public boolean validation() {
        if(emailEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0 && repeatPasswordEditText.getText().length() > 0) {

            if(checkInList(emailEditText.getText().toString())) {
                Toast.makeText(getBaseContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                return false;
            }

            if(!emailEditText.getText().toString().matches(  "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+")) {
                emailEditText.requestFocus();
                emailEditText.setError("Please enter a Valid Email ID");
                return false;
            }
            if(passwordEditText.getText().toString().equals(repeatPasswordEditText.getText().toString())) {
                Toast.makeText(getBaseContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                Toast.makeText(getBaseContext(), "Password & Repeat Password must be the same", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            Toast.makeText(getBaseContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
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

    public boolean checkInList(String email) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email))
                    return true;
            }
        }
        return false;
    }
}
