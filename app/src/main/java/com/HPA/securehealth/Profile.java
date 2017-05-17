package com.HPA.securehealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Button cancelButton;
    Button saveButton;
    Button activityLevel1Button;
    Button activityLevel2Button;
    Button activityLevel3Button;
    Button activityLevel4Button;
    Button activityLevel15Button;
    Button logoutButton;
    TextView activityText;
    TextView activityDescription;
    EditText nameEditText;
    EditText dayEditText;
    EditText monthEditText;
    EditText yearEditText;
    ImageView profilePicture;
    RadioButton radioMale;
    RadioButton radioFemale;
    Spinner heightSpinner;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    Spinner weightSpinner;
    String userChoosenTask = "";
    String email = "";
    String userString = "";
    List<User> userList = new ArrayList<User>();
    boolean isActivity1Selected = false;
    boolean isActivity2Selected = false;
    boolean isActivity3Selected = false;
    boolean isActivity4Selected = false;
    boolean isActivity5Selected = false;

    private static final Integer REQUEST_CAMERA = 0;
    private static final Integer SELECT_FILE = 1;

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
            setContentView(R.layout.activity_profile_small);
        else
            setContentView(R.layout.activity_profile);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        activityLevel1Button = (Button) findViewById(R.id.activityLevel1Button);
        activityLevel2Button = (Button) findViewById(R.id.activityLevel2Button);
        activityLevel3Button = (Button) findViewById(R.id.activityLevel3Button);
        activityLevel4Button = (Button) findViewById(R.id.activityLevel4Button);
        activityLevel15Button = (Button) findViewById(R.id.activityLevel5Button);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        activityText = (TextView) findViewById(R.id.activityText);
        activityDescription = (TextView) findViewById(R.id.activityDescription);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dayEditText = (EditText) findViewById(R.id.dayEditText);
        monthEditText = (EditText) findViewById(R.id.monthEditText);
        yearEditText = (EditText) findViewById(R.id.yearEditText);
        profilePicture = (ImageView) findViewById(R.id.profileImage);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        weightSpinner = (Spinner)findViewById(R.id.spinnerWeight);
        String[] items1 = new String[]{"40-50kg", "50-60kg", "60-70kg", "70-80kg", "80-90kg", "90-100kg", "100-110kg"};
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        weightSpinner.setAdapter(adapter1);

        heightSpinner = (Spinner)findViewById(R.id.spinnerHeight);
        String[] items = new String[]{"140-150cm", "150-160cm", "160-170cm", "170-180cm", "180-190cm"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        heightSpinner.setAdapter(adapter);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
        activityLevel1Button.setOnClickListener(this);
        activityLevel2Button.setOnClickListener(this);
        activityLevel3Button.setOnClickListener(this);
        activityLevel4Button.setOnClickListener(this);
        activityLevel15Button.setOnClickListener(this);
        nameEditText.setOnClickListener(this);
        dayEditText.setOnClickListener(this);
        monthEditText.setOnClickListener(this);
        yearEditText.setOnClickListener(this);
        dayEditText.setFocusable(false);
        monthEditText.setFocusable(false);
        yearEditText.setFocusable(false);
        getUserData();
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

    public void getUserData() {
        if(getEmailSharedPreferences()) {
            if(checkNGetFromList(email).getName() != "") {
                User user = checkNGetFromList(email);
                nameEditText.setText(user.getName());
                if(user.getGender().equals("Male")) {
                    radioMale.setChecked(true);
                }
                else if(user.getGender().equals("Female")) {
                    radioFemale.setChecked(true);
                }
                if(!user.getDOB().equals("")) {
                    String[] dob = user.getDOB().split("-");
                    dayEditText.setText(dob[0]);
                    monthEditText.setText(dob[1]);
                    yearEditText.setText(dob[2]);
                }
                if(!user.getHeight().equals("")) {
                    int position = adapter.getPosition(user.getHeight());
                    heightSpinner.setSelection(position);
                }
                if(!user.getWeight().equals("")) {
                    int position = adapter1.getPosition(user.getWeight());
                    weightSpinner.setSelection(position);
                }
                if(!user.getActivityLevel().equals("")) {
                    String[] activityLevel = user.getActivityLevel().split(",");
                    activityText.setText(activityLevel[0]);
                    activityDescription.setText(activityLevel[1]);

                    String store = activityText.getText().toString();
                    if(store.equals("Little to No Activity")) {
                        activityLevel1Button.setBackgroundResource(R.drawable.standinggreen);
                    }
                    else if(store.equals("Light Activity")) {
                        activityLevel2Button.setBackgroundResource(R.drawable.standing2green);
                    }
                    else if(store.equals("Moderate Activity")) {
                        activityLevel3Button.setBackgroundResource(R.drawable.running0green);
                    }
                    else if(store.equals("Heavy Activity")) {
                        activityLevel4Button.setBackgroundResource(R.drawable.running1green);
                    }
                    else if(store.equals("Very Heavy Activity")) {
                        activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgreen);
                    }
                }
                loadImageFromStorage();
            }
        }
        else {
      //      Toast.makeText(getBaseContext(), "Please Sign In First", Toast.LENGTH_SHORT).show();
      //      startActivity(new Intent(getBaseContext(), SignIn.class));
        }
    }

    public User checkNGetFromList(String email) {

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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.nameEditText:
                nameEditText.requestFocus();
                nameEditText.setFocusableInTouchMode(true);
                break;

            case R.id.dayEditText:
                dayEditText.requestFocus();
                dayEditText.setFocusableInTouchMode(true);
                break;

            case R.id.monthEditText:
                monthEditText.requestFocus();
                monthEditText.setFocusableInTouchMode(true);
                break;

            case R.id.yearEditText:
                yearEditText.requestFocus();
                yearEditText.setFocusableInTouchMode(true);
                break;

            case R.id.logoutButton:
                email = "";
                saveEmailSharedPreferences();
                Toast.makeText(getBaseContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                break;

            case R.id.cancelButton:
                finish();
                startActivity(new Intent(getBaseContext(), MainScreen.class));
                break;

            case R.id.saveButton:
                if(nameEditText.getText().length() != 0) {
                    saveName(nameEditText.getText().toString());
                }
                if(radioMale.isChecked() || radioFemale.isChecked()) {
                    if(radioMale.isChecked()) {
                        saveGender(radioMale.getText().toString());
                    }
                    else {
                        saveGender(radioFemale.getText().toString());
                    }
                }
                if((dayEditText.getText().length() != 0 && monthEditText.getText().length() == 0 && yearEditText.getText().length() == 0) || (dayEditText.getText().length() == 0 && monthEditText.getText().length() != 0 && yearEditText.getText().length() == 0) || (dayEditText.getText().length() == 0 && monthEditText.getText().length() == 0 && yearEditText.getText().length() != 0) || (dayEditText.getText().length() != 0 && monthEditText.getText().length() != 0 && yearEditText.getText().length() == 0) || (dayEditText.getText().length() == 0 && monthEditText.getText().length() != 0 && yearEditText.getText().length() != 0) || (dayEditText.getText().length() != 0 && monthEditText.getText().length() == 0 && yearEditText.getText().length() != 0)) {
                    Toast.makeText(getBaseContext(), "Please fill all of the Date of Birth fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    saveDOB(dayEditText.getText().toString() + "-" + monthEditText.getText().toString() + "-" + yearEditText.getText().toString());
                }
                if(heightSpinner.getSelectedItem().toString() != null) {
                    saveHeight(heightSpinner.getSelectedItem().toString());
                }
                if(weightSpinner.getSelectedItem().toString() != null) {
                    saveWeight(weightSpinner.getSelectedItem().toString());
                }
                if(activityText.getText().toString().length() != 0) {
                    saveActivityLevel(activityText.getText().toString() + "," + activityDescription.getText().toString());
                }
                Toast.makeText(getBaseContext(), "Changes saved Successfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.profileImage:
                selectImage();
                break;

            case R.id.activityLevel1Button:
                if(!isActivity1Selected) {
                    isActivity1Selected = true;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggreen);
                    isActivity2Selected = false;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2gray);
                    isActivity3Selected = false;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0gray);
                    isActivity4Selected = false;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1gray);
                    isActivity5Selected = false;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgray);
                    activityText.setText("Little to No Activity");
                    activityDescription.setText("Little to no activity");
                }
                else {
                    isActivity1Selected = false;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggray);
                }
                break;

            case R.id.activityLevel2Button:
                if(!isActivity2Selected) {
                    isActivity2Selected = true;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2green);
                    isActivity1Selected = false;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggray);
                    isActivity3Selected = false;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0gray);
                    isActivity4Selected = false;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1gray);
                    isActivity5Selected = false;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgray);
                    activityText.setText("Light Activity");
                    activityDescription.setText("1-3 days per week");
                }
                else {
                    isActivity2Selected = false;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2gray);
                }
                break;

            case R.id.activityLevel3Button:
                if(!isActivity3Selected) {
                    isActivity3Selected = true;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0green);
                    isActivity1Selected = false;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggray);
                    isActivity2Selected = false;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2gray);
                    isActivity4Selected = false;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1gray);
                    isActivity5Selected = false;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgray);
                    activityText.setText("Moderate Activity");
                    activityDescription.setText("3-5 days per week");
                }
                else {
                    isActivity3Selected = false;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0gray);
                }
                break;

            case R.id.activityLevel4Button:
                if(!isActivity4Selected) {
                    isActivity4Selected = true;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1green);
                    isActivity1Selected = false;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggray);
                    isActivity2Selected = false;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2gray);
                    isActivity3Selected = false;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0gray);
                    isActivity5Selected = false;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgray);
                    activityText.setText("Heavy Activity");
                    activityDescription.setText("6-7 days per week");
                }
                else {
                    isActivity4Selected = false;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1gray);
                }
                break;

            case R.id.activityLevel5Button:
                if(!isActivity5Selected) {
                    isActivity5Selected = true;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgreen);
                    isActivity1Selected = false;
                    activityLevel1Button.setBackgroundResource(R.drawable.standinggray);
                    isActivity2Selected = false;
                    activityLevel2Button.setBackgroundResource(R.drawable.standing2gray);
                    isActivity3Selected = false;
                    activityLevel3Button.setBackgroundResource(R.drawable.running0gray);
                    isActivity4Selected = false;
                    activityLevel4Button.setBackgroundResource(R.drawable.running1gray);
                    activityText.setText("Very Heavy Activity");
                    activityDescription.setText("A physically demaning job");
                }
                else {
                    isActivity5Selected = false;
                    activityLevel15Button.setBackgroundResource(R.drawable.runningreallyfastgray);
                }
                break;
        }

    }

    public void saveName(String name) {
        if(getEmailSharedPreferences()) {
                if(checkInList(email,name)) {
                    saveListSharedPreferences(userList);
                }
        }
    }

    public void saveGender(String gender) {
        if(getEmailSharedPreferences()) {
            if(checkInListGender(email,gender)) {
                saveListSharedPreferences(userList);
            }
        }
    }

    public void saveDOB(String dob) {
        if(getEmailSharedPreferences()) {
            if(checkInListDOB(email,dob)) {
                saveListSharedPreferences(userList);
            }
        }
    }

    public void saveHeight(String height) {
        if(getEmailSharedPreferences()) {
            if(checkInListHeight(email,height)) {
                saveListSharedPreferences(userList);
            }
        }
    }

    public void saveWeight(String weight) {
        if(getEmailSharedPreferences()) {
            if(checkInListWeight(email,weight)) {
                saveListSharedPreferences(userList);
            }
        }
    }

    public void saveActivityLevel(String activityLevel) {
        if(getEmailSharedPreferences()) {
            if(checkInListActivityLevel(email,activityLevel)) {
                saveListSharedPreferences(userList);
            }
        }
    }

    public boolean checkInList(String email,String name) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setName(name);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInListGender(String email,String gender) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setGender(gender);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInListDOB(String email,String dob) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setDOB(dob);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInListHeight(String email,String height) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setHeight(height);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInListWeight(String email,String weight) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setWeight(weight);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkInListActivityLevel(String email,String activityLevel) {

        if(getListSharedPreferences()) {
            User[] userArray = userList.toArray(new User[userList.size()]);

            for(User u: userArray) {
                if(u.getEmail().equals(email)) {
                    u.setActivityLevel(activityLevel);
                    return true;
                }
            }
        }
        return false;
    }

    public void saveListSharedPreferences(List counterList) {
        Gson gson = new Gson();
        userString = gson.toJson(counterList);
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("com.HPA.securehealth", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("User List", userString).commit();
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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Profile.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap cropped = getRoundedCroppedImage(bm);
        profilePicture.setImageBitmap(cropped);
        String path = saveToInternalStorage(cropped);
        android.util.Log.e("Path", path);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap cropped = getRoundedCroppedImage(thumbnail);
        profilePicture.setImageBitmap(cropped);
        if(getEmailSharedPreferences()) {
            String path = saveToInternalStorage(cropped);
            android.util.Log.e("Path", path);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory, email + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage() {
        try {
            String path = "/data/user/0/com.HPA.securehealth/app_imageDir";
            File f = new File(path, email + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView)findViewById(R.id.profileImage);
            img.setImageBitmap(getRoundedCroppedImage(b));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private Bitmap getRoundedCroppedImage(Bitmap bmp) {
        int widthLight = bmp.getWidth();
        int heightLight = bmp.getHeight();

        Bitmap output = Bitmap.createBitmap(widthLight, heightLight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, widthLight / 2 ,heightLight / 2,paint);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bmp, 0, 0, paintImage);

        return output;
    }

}
