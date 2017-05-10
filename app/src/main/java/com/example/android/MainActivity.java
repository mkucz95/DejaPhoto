package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.dejaphoto.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        Button album = (Button) findViewById(R.id.bt_1);
        Button dejaMode = (Button) findViewById(R.id.bt_4);
        Button settings = (Button) findViewById(R.id.bt_3);
        Button addPhoto = (Button) findViewById(R.id.bt_2);
        Button display = (Button) findViewById(R.id.bt_5);

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });

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

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApp();
            }
        });

=======
>>>>>>> 559b2c423f93780a1cd960072cb9bc6f2f579ec3
        Intent backgroundIntent = new Intent(this, BackgroundService.class);
        startService(backgroundIntent);  //starts service that keeps track of time and location


        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);
       // displayCycleIntent.putExtra("source", true);
       // displayCycleIntent.setAction(ACTION_BUILD_CYCLE); TODO this causes crash
        startService(displayCycleIntent);
        //starts service that first builds and calls another service to save display cycle

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

<<<<<<< HEAD
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


    public void startApp(){
     /*   SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();  shared preferences won't work
        since it can only store. another option is local storage*/

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

=======
>>>>>>> 559b2c423f93780a1cd960072cb9bc6f2f579ec3
}
