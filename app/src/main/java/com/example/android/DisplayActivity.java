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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.util.Timer;

import static com.example.dejaphoto.R.string.share;

public class DisplayActivity extends AppCompatActivity {

    public Switch user;
    public Switch friend;
    public Button saveDisplay;
    private static boolean friendDisplay = Global.displayFriend;
    private static boolean userDisplay = Global.displayUser;
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (Switch) findViewById(R.id.s_displayUser);
        friend = (Switch) findViewById(R.id.s_displayFriend);

        displayUpdate();

        saveDisplay = (Button) findViewById(R.id.save_display);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userDisplay = true;
                    Toast.makeText(getApplicationContext(),
                            "Display User photo", Toast.LENGTH_SHORT).show();
                } else {
                    userDisplay = false;
                    Toast.makeText(getApplicationContext(),
                            "Will not display currUser photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    friendDisplay = true;
                    Toast.makeText(getApplicationContext(),
                            "Display friend photo", Toast.LENGTH_SHORT).show();
                } else {
                    friendDisplay = false;
                    Toast.makeText(getApplicationContext(),
                            "Will not display friend photo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.displayFriend = friendDisplay;
                Global.displayUser = userDisplay;

                if(!Global.displayFriend){
                    FileManager.deleteFolder("DejaPhotoFriends");
                    //if we switch off display friends, remove their photos from device
                }

                Intent intent = new Intent(getApplicationContext(), BuildDisplayCycle.class);
                intent.setAction(ACTION_BUILD_CYCLE);
                startService(intent); //rebuild display cycle with new display settings


                Toast.makeText(getApplicationContext(),
                        "Display Setting Saved and Build Display Cycle", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayUpdate(){
        if (Global.displayUser) {
            user.setChecked(true);
        } else {
            user.setChecked(false);
        }

        if (Global.displayFriend) {
            friend.setChecked(true);
        } else {
            friend.setChecked(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayUpdate();
    }
}
