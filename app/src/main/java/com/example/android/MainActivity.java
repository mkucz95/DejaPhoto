package com.example.android;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";

    private static final String GET_INITIAL_LOCATION = "com.example.android.GET_INITIAL_LOCATION";
    public static final int MY_PERMISSIONS_MULTIPLE_REQUEST = 99;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setAlarm(); //Amanda: call function for autoWallpaper change

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

        startApp();

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
        Intent intent = new Intent (this, Rerank.class);
        startService(intent);

    }

    //Amanda's code, dont touch
    public void setAlarm(){

//        Intent clickIntent = new Intent(getApplicationContext(), WidgetManager.class);
//        clickIntent.setAction(Intent.ACTION_SEND);
//        clickIntent.setType("text/plain");
//        clickIntent.putExtra("button_pressed", "next");
//        PendingIntent pending = PendingIntent.getService(this, 0, clickIntent, 0);

        /*Below code is to obtain time it has been since service starts*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        Intent receiverIntent = new Intent(getApplicationContext(), myReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, receiverIntent, 0);
        //not sure why, but 'this' is faster than  getApplicationContext()
        //PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, receiverIntent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, startTime, 5000, pending);
        //am.setWindow(AlarmManager.RTC_WAKEUP, 0, 5000, pending);

    }

    //Amanda's code dont touch
    //Exact same code as SetAlarm() except 'cancel()'
    public void cancelAlarm(){

        /*Below code is to obtain time it has been since service starts*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        Intent receiverIntent = new Intent(getApplicationContext(), myReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_CANCEL_CURRENT); //UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getApplication().getSystemService(ALARM_SERVICE);
        am.cancel(pending);
        am.setRepeating(AlarmManager.RTC, startTime, 5000, pending);
    }

    public void startApp(){
        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);

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
