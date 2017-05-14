package com.example.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

import com.example.dejaphoto.R;

import com.example.dejaphoto.R;

public class SetActivity extends AppCompatActivity {

    private Switch location;
    private Switch time;
    private Switch dayOfWeek;
    private Switch karma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location = (Switch) findViewById(R.id.s_location);
        time = (Switch) findViewById(R.id.s_time);
        dayOfWeek = (Switch) findViewById(R.id.s_dow);
        karma = (Switch) findViewById(R.id.s_karma);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);

        if(sharedPreferences.getBoolean("Location Preference", false) == true) {
            location.setChecked(true);
        }
        else {
            location.setChecked(false);
        }

        if(sharedPreferences.getBoolean("Time Preference", false) == true) {
            time.setChecked(true);
        }
        else {
            time.setChecked(false);
        }

        if(sharedPreferences.getBoolean("Day of Week Preference", false) == true) {
            dayOfWeek.setChecked(true);
        }
        else {
            dayOfWeek.setChecked(false);
        }

        if(sharedPreferences.getBoolean("Karma Preference", false) == true) {
            karma.setChecked(true);
        }
        else {
            karma.setChecked(false);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Location Preference", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Location setting is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Location Preference", false);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Location setting is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Time Preference", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Time setting is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Time Preference", false);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Time setting is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dayOfWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Day of Week Preference", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Day of Week setting is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Day of Week Preference", false);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Day of Week setting is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        karma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Karma Preference", true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Karma setting is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Karma Preference", false);
                    editor.commit();

                    Toast.makeText(getApplicationContext(),
                            "Karma setting is off", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
