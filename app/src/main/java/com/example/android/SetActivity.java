package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

public class SetActivity extends AppCompatActivity {

    public Switch location;
    public Switch time;
    public Switch dayOfWeek;
    public Switch karma;
    EditText input;
    private Button saveButton;

    private boolean locationSetting;
    private boolean timeSetting;
    private boolean dofSetting;
    private boolean karmaSetting;


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

        if(Global.locationSetting) {
            location.setChecked(true);
        }
        else {
            location.setChecked(false);
        }

        if(Global.timeSetting) {
            time.setChecked(true);
        }
        else {
            time.setChecked(false);
        }

        if(Global.daySetting) {
            dayOfWeek.setChecked(true);
        }
        else {
            dayOfWeek.setChecked(false);
        }

        if(Global.karmaSetting) {
            karma.setChecked(true);
        }
        else {
            karma.setChecked(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!Global.dejaVuSetting){ //if dejavu mode is off, we cannot change settings
                    Toast.makeText(getApplicationContext(),
                            "DejaVu Mode is Off", Toast.LENGTH_SHORT).show();
                }
                else { //dejavu mode is on
                    Intent trackerIntent = new Intent (getApplicationContext(), TrackerService.class);
                    if (isChecked) {
                        locationSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Location setting is on", Toast.LENGTH_SHORT).show();
                        Log.i("trackerService", "Starting trackerService Intent");
                        startService(trackerIntent);

                    } else {
                        locationSetting = false;
                        Toast.makeText(getApplicationContext(),
                                "Location setting is off", Toast.LENGTH_SHORT).show();
                        Log.i("trackerService", "Stopping trackerService Intent");
                        stopService(trackerIntent);
                    }
                }
            }
        });

        time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!Global.dejaVuSetting) { //if dejavu mode is off, we cannot change settings
                    Toast.makeText(getApplicationContext(),
                            "DejaVu Mode is Off", Toast.LENGTH_SHORT).show();
                } else { //dejavu mode is on
                    if (isChecked) {
                        timeSetting=true;
                        Toast.makeText(getApplicationContext(),
                                "Time setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        timeSetting=false;
                        Toast.makeText(getApplicationContext(),
                                "Time setting is off", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dayOfWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!Global.dejaVuSetting) { //if dejavu mode is off, we cannot change settings
                    Toast.makeText(getApplicationContext(),
                            "DejaVu Mode is Off", Toast.LENGTH_SHORT).show();
                } else { //dejavu mode is on
                    if (isChecked) {
                        dofSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Day of Week setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        dofSetting = false;
                        Toast.makeText(getApplicationContext(),
                                "Day of Week setting is off", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        karma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!Global.dejaVuSetting) { //if dejavu mode is off, we cannot change settings
                    Toast.makeText(getApplicationContext(),
                            "DejaVu Mode is Off", Toast.LENGTH_SHORT).show();
                } else { //dejavu mode is on
                    if (isChecked) {
                        karmaSetting = true;
                        Log.i("setKarma", "KarmaSetting: " + Global.getSettings()[4]);
                        Toast.makeText(getApplicationContext(),
                                "Karma setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        karmaSetting = false;
                        Toast.makeText(getApplicationContext(),
                                "Karma setting is off", Toast.LENGTH_SHORT).show();
                    }
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

        saveButton = (Button) findViewById(R.id.bt_7);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.locationSetting = locationSetting;
                Global.daySetting = dofSetting;
                Global.karmaSetting = karmaSetting;
                Global.timeSetting = timeSetting;
                startRerank();
            }
        });
    }

    public void startRerank() {
        Intent intent = new Intent(this, Rerank.class);
        startService(intent);
    }

    public void save(View view) {
        input = (EditText) findViewById(R.id.user_specify);

        Global.changeInterval = Integer.parseInt(input.getText().toString()) * 1000;
        //set the interval specified by user

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

        TextView interval = (TextView) findViewById(R.id.prevMin);
        TextView intervalNext = (TextView) findViewById(R.id.change_interval);
        intervalNext.setText(Long.toString(Global.changeInterval)); //show settings on settings page
    }

}
