package com.example.android;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_MULTIPLE_REQUEST = 99;
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String GET_INITIAL_LOCATION = "com.example.android.GET_INITIAL_LOCATION";

    public static int n = 1;

    Context context;

    private FileManager fileManager;

    File dejaCopy;
    File dejaPhoto;

    static final int REQUEST_CODE = 1;
    String TAG = "deja";
    static final int SELECT_IMAGE = 2;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(Manifest.permission.READ_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button camera = (Button) findViewById(R.id.bt_1);
        Button settings = (Button) findViewById(R.id.bt_3);
        Button addPhoto = (Button) findViewById(R.id.bt_2);
        Button display = (Button) findViewById(R.id.bt_7);
        Button share = (Button) findViewById(R.id.bt_4);
        Button addFriends = (Button) findViewById(R.id.bt_6);

        Display displayWindow = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        displayWindow.getSize(size);
        Global.windowWidth = size.x;
        Global.windowHeight = size.y;

        dejaCopy = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "DejaCopy");

        if (!dejaCopy.exists()) {
            Log.i(TAG, "Folder doesn't exist, creating it...");
            boolean rv = dejaCopy.mkdir();
            Log.i(TAG, "Folder creation " + ( rv ? "success" : "failed"));
        } else {
            Log.i(TAG, "Folder already exists.");
        }



        startApp();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisplay();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShare();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSettings();
            }
        });

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addFriend();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });

        Global.syncTimerTask = new DatabaseSync();
        Global.syncTimer.schedule(Global.syncTimerTask, 0, Global.syncInterval*1000); //schedule timer
    }

    // MS2 click the camera button to open default camera
    public void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void changeSettings() {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
    }

    public void addFriend() {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
    }

    public void openDisplay() {
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }

    public void openShare() {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }

    public void addPhoto () {
        Intent intent = new Intent();
        //Set intent type -- images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Allow multiple selections
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        Toast.makeText(getApplicationContext(), "Press and hold to select multiple images", Toast.LENGTH_SHORT).show();
    }

    /*
     * This method receives intent from startActivityForResult and extracts the URI in order
     * to get image path data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        context = getApplicationContext();
        fileManager = new FileManager(context);

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (data != null) {
                //Clipdata for multiple selections, Uri format for single selection
                ClipData clipData = data.getClipData();
                Uri singleUri = data.getData();
                //Multiple image case
                if (clipData != null) {
                    //Go through each item in clip data
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.i(TAG, "" + item.getUri().toString());
                        Uri uri = item.getUri();
                        //Get the absolute path from uri
                        String p = fileManager.getImagePath(uri);
                        try {
                            fileManager.copyFile(new File(p), dejaCopy);
                            Global.uploadImageQueue.add(p);
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                //Single image case
                else if(singleUri != null){
                    String p  = fileManager.getImagePath(singleUri);
                    try {
                        Log.i(TAG, "" + singleUri.toString());
                        fileManager.copyFile(new File(p), dejaCopy);
                        Global.uploadImageQueue.add(p);
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        // take a picture and save in deja folder
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            fileManager.saveFile(thumbnail);
            Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_LONG).show();
        }
        SQLiteHelper helper = new SQLiteHelper();
        helper.iterateAllMedia(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Global.wholeTableProjection, this);

    }

    public void startApp(){
        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);

        Global.autoWallpaperChange = new AutoWallpaperChangeTask(getApplicationContext());
        Global.undoTimer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);

        Log.i("BuildCycle", "Calling BuildDisplayCycle...");
        displayCycleIntent.setAction(ACTION_BUILD_CYCLE);
        startService(displayCycleIntent);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void requestPermission(){
        Log.i("permission", "checking permission...");
        //Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Request.java Permissions
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_MULTIPLE_REQUEST);
        }
        else{
            startApp();
        }
        //startApp();
        Log.i("permission", "done with permissions");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("permission", "Requesting Permission");
        if (requestCode == MY_PERMISSIONS_MULTIPLE_REQUEST) {
            Log.i("permission", "checking...");
            if (grantResults.length == 5 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("permission", "Read Permission Now Granted...");
                Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show();
                //Permission Granted, photos now accessible
            }
            else {
                //Permission denied
                Toast.makeText(this, "Read Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Location Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", " Location Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Camera Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Camera Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Write Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Write Access Denied", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void saveInterval(View v) {
        EditText input = (EditText) findViewById(R.id.user_specify);

        Log.i("saveMsg", "input:" + input);

        if(input == null || input.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a valid time Interval", Toast.LENGTH_SHORT).show();
            return;
        }

        long newInterval = Integer.parseInt(input.getText().toString());

        //set the interval specified by currUser
        Global.changeInterval = newInterval * 1000;
        Global.undoTimer.cancel();
        Global.undoTimer = new Timer();
        Global.autoWallpaperChange = new AutoWallpaperChangeTask(getApplicationContext());
        Global.undoTimer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

        Log.i("saveMsg", "Location: " + Global.locationSetting);
        Log.i("saveMsg", "Time: " + Global.timeSetting);
        Log.i("saveMsg", "Day: " + Global.daySetting);
        Log.i("saveMsg", "Karma: " + Global.karmaSetting);

        TextView interval = (TextView) findViewById(R.id.prevMin);
        TextView intervalNext = (TextView) findViewById(R.id.change_interval);
        intervalNext.setText(Long.toString(newInterval) + " seconds"); //show settings on settings page
    }
}
