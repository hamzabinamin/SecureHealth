package com.HPA.securehealth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Hamza on 11/29/16.
 */
public class Home extends Activity implements View.OnClickListener {
    //TODO run tutorial livrary to show user how to operate monitor
    Button quickMeasurement;
    final static String PREFS_NAME = "HeartRateMonitorSettings";

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
            setContentView(R.layout.home_small);
        else
            setContentView(R.layout.home);
        //check to see if first time launching and launch tutorial
//        TutorialsCardActivity activity = new TutorialsCardActivity();
        quickMeasurement = (Button) findViewById(R.id.quickMeasurementButton);
        quickMeasurement.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String lastVersion =settings.getString("versionname","0");

            if (!lastVersion.equalsIgnoreCase(versionName)) {
                //the app is being launched for first time, do something
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle(getString(R.string.first_time_dialog_title));
                dialogBuilder.setMessage(getString(R.string.first_time_dialog_message));
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton(getString(R.string.yesbutton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //run video tutorial
                        runTutorial();
                    }
                });
                dialogBuilder.setNegativeButton(getString(R.string.nobutton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                // first time task
                // record the fact that the app has been started at least once
                settings.edit().putString("versionname", versionName).commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.quickMeasurementButton:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
                }
                else {
                    intent = new Intent(this, HeartRateMonitor.class);
                    startActivity(intent);
                }
                break;


        }
    }

    public void runTutorial() {
        ArrayList<TutorialEntry> entries = new ArrayList<TutorialEntry>();
        entries.add(new TutorialEntry(R.drawable.phone, getString(R.string.tutorial_1)));
        entries.add(new TutorialEntry(R.drawable.handposition,getString(R.string.tutorial_2)));
        Intent intent = new Intent(getApplicationContext(), TutorialsCardActivity.class);
        intent.putExtra("entries", entries);
        startActivity(intent);
    }
}
