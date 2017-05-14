package com.example.android;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String ACTION_RERANK_BUILD = "com.example.android.RERANK_BUILD";

    private static final String GET_INITIAL_LOCATION = "com.example.android.GET_INITIAL_LOCATION";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent clickIntent = new Intent(getApplicationContext(), WidgetManager.class);
        clickIntent.setAction(Intent.ACTION_SEND);
        clickIntent.setType("text/plain");
        clickIntent.putExtra("button_pressed", "next");
        //startService(clickIntent);
        PendingIntent pending = PendingIntent.getService(this, 0, clickIntent, 0);
        am.setRepeating(AlarmManager.RTC, 0, 5000, pending);

        System.out.println(Manifest.permission.READ_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  Button album = (Button) findViewById(R.id.bt_1);
        Button dejaMode = (Button) findViewById(R.id.bt_4);
        Button settings = (Button) findViewById(R.id.bt_3);
       // Button addPhoto = (Button) findViewById(R.id.bt_2);
        Button display = (Button) findViewById(R.id.bt_5);
        Button interval = (Button) findViewById(R.id.bt_6);
        Button playApp = (Button) findViewById(R.id.bt_7);

        playApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startRerank();
            }
        } );

      /*  album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
*/
        dejaMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMode();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSettings();
            }
        });

       /* addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        */

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApp();
            }
        });

        interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInterval();
            }
        });
    }

    public void launchActivity() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    public void setMode() {
        Intent intent = new Intent(this, ModeActivity.class);
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

    public void setInterval() {
        Intent intent = new Intent(this, IntervalActivity.class);
        startActivity(intent);
    }

    public void startRerank(){
        Intent intent = new Intent (this,Rerank.class);
        startService(intent);
    }


    public void startApp(){
        SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);
        // displayCycleIntent.putExtra("source", true);
        Log.i("BuildCycle", "Calling BuildDisplayCycle...");
        displayCycleIntent.setAction(ACTION_RERANK_BUILD);
       /* displayCycleIntent.setAction(Intent.ACTION_SEND);
        displayCycleIntent.putExtra("method", "fromMedia");*/
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
                != PackageManager.PERMISSION_GRANTED) {
            //Request Permissions
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else{
            Intent backgroundIntent = new Intent(this, BackgroundService.class);
            startService(backgroundIntent);  //starts service that keeps track of time and location
            Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);
            // displayCycleIntent.putExtra("source", true);
            Log.i("permission", "Permission already granted...");
            displayCycleIntent.setAction(ACTION_BUILD_CYCLE);
            displayCycleIntent.putExtra("method", "fromMedia");
            startService(displayCycleIntent);
        }
        Log.i("permission", "done with permissions");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        Log.i("permission", "Requesting Permission");
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            Log.i("permission", "checking...");
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("permission", "Permission Now Granted...");
                Toast.makeText(this, "Read External Storage permission granted", Toast.LENGTH_SHORT).show();
                //Permission Granted, photos now accessible
                Intent backgroundIntent = new Intent(this, BackgroundService.class);
                startService(backgroundIntent);  //starts service that keeps track of time and location
                Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);
                // displayCycleIntent.putExtra("source", true);
                Log.i("BuildCycle", "Calling BuildDisplayCycle...");
                displayCycleIntent.setAction(Intent.ACTION_SEND);
                displayCycleIntent.putExtra("method", "fromMedia");
                startService(displayCycleIntent);
                //starts service that first builds and calls another service to save display cycle

            } else {
                //Permission denied
                Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
