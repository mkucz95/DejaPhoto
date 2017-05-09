package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.dejaphoto.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent backgroundIntent = new Intent(this, BackgroundService.class);
        startService(backgroundIntent);  //starts service that keeps track of time and location

        SharedPreferences counterPref = getSharedPreferences("counter", MODE_PRIVATE);
        SharedPreferences.Editor editor = counterPref.edit();
        editor.putInt("counter", 0); //initialize the counter to 0

        editor.apply();

        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);
        displayCycleIntent.putExtra("source", true);
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

}
