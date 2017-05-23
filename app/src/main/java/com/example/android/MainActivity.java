package com.example.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_MULTIPLE_REQUEST = 99;
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String GET_INITIAL_LOCATION = "com.example.android.GET_INITIAL_LOCATION";

    static final String dejaAlbum = "Deja Photo Album";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dejaAlbum);

    private static final String deja = "/Deja Photo Album";

    static final int REQUEST_CODE = 1;

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
       // Button addPhoto = (Button) findViewById(R.id.bt_2);
        Button display = (Button) findViewById(R.id.bt_5);
        //Button playApp = (Button) findViewById(R.id.bt_7);
       // Button playApp = (Button) findViewById(R.id.bt_7);

        startApp();
        final LinearLayout l = (LinearLayout) findViewById(R.id.l_settings);
        setContentView(R.layout.content_set);
        Switch mode = (Switch) findViewById(R.id.s_mode);

        Log.d("MainActivity", "MODE: " +mode);

        if (Global.dejaVuSetting) {
            mode.setChecked(true);
        } else {
            mode.setChecked(false);
        }

        /*playApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startRerank();
            }
        } );*/
//        playApp.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startRerank();
//            }
//        } );

      /*  album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
*/
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSettings();
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRerank();
            }
        });

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.dejaVuSetting = true;

                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "DejaVu - On", Toast.LENGTH_SHORT).show();

                } else {
                    Global.dejaVuSetting = false;

                    l.setBackgroundColor(Color.parseColor("#1BEA44"));
                    Toast.makeText(getApplicationContext(),
                            "DejaVu - Off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // MS2 click the camera button to open default camera
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_LONG).show();
        }
    }

    public String saveImage(Bitmap myMap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myMap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File file = new File(Environment.getExternalStorageDirectory() + deja);
        if(!file.exists()) {
            file.mkdirs();
        }

        try {
            File f = new File(file, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved:: --->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void launchActivity() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    public void changeSettings() {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
    }

    public void add () {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
    }

    public void startRerank(){
        Intent intent = new Intent (this, Rerank.class);
        startService(intent);
    }

    public void startApp(){
        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);


        Global.autoWallpaperChange = new AutoWallpaperChange(getApplicationContext());
        Global.timer.schedule(Global.autoWallpaperChange,
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
                != PackageManager.PERMISSION_GRANTED) {
            //Request Permissions
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_MULTIPLE_REQUEST);
        }
        else{
            startApp();
        }
        Log.i("permission", "done with permissions");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("permission", "Requesting Permission");
        if (requestCode == MY_PERMISSIONS_MULTIPLE_REQUEST) {
            Log.i("permission", "checking...");
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("permission", "Permission Now Granted...");
                Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show();
                //Permission Granted, photos now accessible
            }
            else {
                //Permission denied
                Toast.makeText(this, "Read Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 3 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 3 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
