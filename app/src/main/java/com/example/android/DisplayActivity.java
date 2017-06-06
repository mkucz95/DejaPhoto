package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

public class DisplayActivity extends AppCompatActivity {

    public Switch user;
    public Switch friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (Switch) findViewById(R.id.s_displayUser);
        if (Global.displayUser) {
            user.setChecked(true);
        } else {
            user.setChecked(false);
        }

        friend = (Switch) findViewById(R.id.s_displayFriend);
        if (Global.displayFriend) {
            friend.setChecked(true);
        } else {
            friend.setChecked(false);
        }


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.displayUser = true;
                    Toast.makeText(getApplicationContext(),
                            "Display User photo", Toast.LENGTH_SHORT).show();
                } else {
                    Global.displayUser = false;
                    Toast.makeText(getApplicationContext(),
                            "Will not display currUser photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.displayFriend = true;
                    Toast.makeText(getApplicationContext(),
                            "Display friend photo", Toast.LENGTH_SHORT).show();
                } else {
                    Global.displayFriend = false;
                    Toast.makeText(getApplicationContext(),
                            "Will not display friend photo", Toast.LENGTH_SHORT).show();
                    FileManager.removeFriendImages();
                }
            }
        });

    }

}
