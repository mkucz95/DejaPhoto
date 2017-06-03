package com.example.android;

import android.content.Context;
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
import android.widget.Toast;

import com.example.dejaphoto.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Timer;

public class ShareActivity extends AppCompatActivity {

    private Switch share;
    private Button saveButton;
    private static User currUser = Global.currUser;
    private static Context context;

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

        context = getApplicationContext();

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

                       // send intent to database handler
                        Intent intent = new Intent(context, DatabaseMediator.class);
                        intent.putExtra("upload", true);
                        startService(intent);

                    } else {
                        Global.shareSetting = false;
                        share.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "Share setting is off", Toast.LENGTH_SHORT).show();

                        //all users photos deleted from web
                        //PhotoStorage.remove(PhotoStorage.getStorageRef(Global.currUser.email));
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

        saveButton = (Button) findViewById(R.id.save_syncSet);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.sync_freq);
                int timeSetting = Integer.parseInt(editText.getText().toString());
                Global.syncInterval = timeSetting;

                //cancel timer and create new one when user changes the sync interval
                Global.syncTimer.cancel();
                Global.syncTimer = new Timer();
                Global.syncTimer.schedule(Global.syncTimerTask, 0, Global.syncInterval*1000);

                Toast.makeText(getApplicationContext(),
                        "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

