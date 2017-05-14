package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

import com.example.dejaphoto.R;

import com.example.dejaphoto.R;

public class ModeActivity extends AppCompatActivity {

    private Switch mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveSharedPref(false);

        mode = (Switch) findViewById(R.id.s_mode);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);

        if(sharedPreferences.getBoolean("Mode Preference", false) == true) {
            mode.setChecked(true);
        }
        else {
            mode.setChecked(false);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final LinearLayout l = (LinearLayout) findViewById(R.id.l_switch);


        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    saveSharedPref(true);

                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "Deja vu mode is on", Toast.LENGTH_SHORT).show();

                }
                else {
                    saveSharedPref(false);

                    l.setBackgroundColor(Color.parseColor("#1BEA44"));
                    Toast.makeText(getApplicationContext(),
                            "Deja vu mode is off", Toast.LENGTH_SHORT).show();
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

    public void saveSharedPref(boolean type){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Mode Preference", type);
        editor.commit();
    }

}