package com.example.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

public class ShareActivity extends AppCompatActivity {

    public Switch share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        share = (Switch) findViewById(R.id.s_share);
        if(Global.shareSetting) {
            share.setChecked(true);
        }
        else {
            share.setChecked(false);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!Global.dejaVuSetting) { //if dejavu mode is off, we cannot change settings
                    Toast.makeText(getApplicationContext(),
                            "DejaVu Mode is Off", Toast.LENGTH_SHORT).show();
                } else { //dejavu mode is on
                    if (isChecked) {
                        Global.shareSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Share setting is on", Toast.LENGTH_SHORT).show();
                    } else {
                        Global.shareSetting = false;
                        Toast.makeText(getApplicationContext(),
                                "Share setting is off", Toast.LENGTH_SHORT).show();
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
    }

}
