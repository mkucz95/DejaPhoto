package com.example.android;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

import org.w3c.dom.Text;

import java.util.Timer;

public class SetActivity extends AppCompatActivity {

    public Switch location;
    public Switch time;
    public Switch dayOfWeek;
    public Switch karma;
    public Switch mode;
    EditText input;
    private Button saveButton;
    TextView newInterval;
    private boolean locationSetting;
    private boolean timeSetting;
    private boolean dofSetting;
    private boolean karmaSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_set);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Log.i("deja", "" + Global.changeInterval);

        updateInterval();
        location = (Switch) findViewById(R.id.s_location);
        time = (Switch) findViewById(R.id.s_time);
        dayOfWeek = (Switch) findViewById(R.id.s_dow);
        karma = (Switch) findViewById(R.id.s_karma);

        final LinearLayout l = (LinearLayout) findViewById(R.id.l_settings);
        setContentView(R.layout.content_set);
        Switch mode = (Switch) findViewById(R.id.s_mode);

        Log.d("SetActivity", "MODE: " +mode);

        if (Global.dejaVuSetting) {
            mode.setChecked(true);
        } else {
            mode.setChecked(false);
        }



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



//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.dejaVuSetting = true;

                    //l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "DejaVu - On", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i("dejaVu", "Turning off");
                    Global.dejaVuSetting = false;

                    //l.setBackgroundColor(Color.parseColor("#1BEA44"));
                    Toast.makeText(getApplicationContext(),
                            "DejaVu - Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                        Log.i("saveMsg", "Location ON");
                        locationSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Location setting is on", Toast.LENGTH_SHORT).show();
                        Log.i("trackerService", "Starting trackerService Intent");
                        startService(trackerIntent);

                    } else {
                        Log.i("saveMsg", "Location OFF");

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
                        Log.i("saveMsg", "Time ON");

                        timeSetting=true;
                        Toast.makeText(getApplicationContext(),
                                "Time setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("saveMsg", "Time OFF");

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
                        Log.i("saveMsg", "Day ON");

                        dofSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Day of Week setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("saveMsg", "Day OFF");

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
                        Log.i("saveMsg", "Karma ON");
                        karmaSetting = true;
                        Log.i("setKarma", "KarmaSetting: " + Global.getSettings()[4]);
                        Toast.makeText(getApplicationContext(),
                                "Karma setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("saveMsg", "Karma OFF");

                        karmaSetting = false;
                        Toast.makeText(getApplicationContext(),
                                "Karma setting is off", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        saveButton = (Button) findViewById(R.id.bt_7);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = (EditText) findViewById(R.id.user_specify);
                int interval = Integer.parseInt(input.getText().toString());
                newInterval = (TextView) findViewById(R.id.change_interval);

                Log.i("updateInterval", "input: " + interval);
                String newInfo = interval + " Seconds";
                newInterval.setText(newInfo);
                Global.changeInterval = (interval * 1000);
                Global.locationSetting = locationSetting;
                Global.daySetting = dofSetting;
                Global.karmaSetting = karmaSetting;
                Global.timeSetting = timeSetting;
                startRerank();
                if(Global.timer != null) {
                    Global.autoWallpaperChange.cancel();
                    Global.timer.cancel();
                    Global.autoWallpaperChange = new AutoWallpaperChange(getApplicationContext());
                    Global.timer = new Timer();
                    Global.timer.schedule(Global.autoWallpaperChange,
                            Global.changeInterval, Global.changeInterval);
                }
            }
        });
    }

    public void startRerank() {
        Intent intent = new Intent(this, Rerank.class);
        startService(intent);
    }

    public void updateInterval(){
        newInterval = (TextView) findViewById(R.id.change_interval);
        String newInfo = Global.changeInterval/100 + " seconds";
        Log.i("deja", "Setting text to: " + newInfo);
        newInterval.setText(newInfo);
    }
}
