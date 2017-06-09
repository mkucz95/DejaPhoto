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
import android.widget.TextView;
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
    private Button testButton;
    private TextView textView;
    private static User currUser = Global.currUser;
    private static Context context;
    private static final int TEST_INTERVAL = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.syncInterval);
        share = (Switch) findViewById(R.id.s_share);

        displayUpdate(true); //update the switches do display settings

        context = getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        Global.shareSetting = true;
                        Toast.makeText(getApplicationContext(),
                                "Sharing On", Toast.LENGTH_SHORT).show();

                    } else {
                        Global.shareSetting = false;
                        share.setChecked(false);
                        Toast.makeText(getApplicationContext(),
                                "Sharing Off", Toast.LENGTH_SHORT).show();

                     //remove references to curr user's pictures if they switch off
                        DatabaseReference reference = Global.currUser.userPhotosRef();
                        if(reference != null) reference.removeValue();
                    }
            }
        });

        saveButton = (Button) findViewById(R.id.save_syncSet);
        testButton = (Button) findViewById(R.id.test_10sec);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.sync_freq);
                int timeSetting = Integer.parseInt(editText.getText().toString());

                Global.syncInterval = timeSetting;
                displayUpdate(false);

                //cancel timer and create new one when user changes the sync interval
                timerReset();

                Toast.makeText(getApplicationContext(),
                        "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancel timer and create new one when user changes the sync interval
                Global.syncInterval = 10;
                displayUpdate(false);

                timerReset();
                Toast.makeText(getApplicationContext(),
                        "Test: 10s sync interval", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    sets the view of the UI to the desired setting
     */
    private void displayUpdate(boolean switchUpdate){
       if(switchUpdate) {
           if (Global.shareSetting) {
               share.setChecked(true);
           } else {
               share.setChecked(false);
           }
       }
       else{
           String text = Global.syncInterval + " seconds";
           textView.setText(text);
       }

    }

    @Override
    protected void onStart() {
        super.onStart();

        displayUpdate(true);
        displayUpdate(false);
    }

    private static void timerReset(){
        Global.syncTimerTask.cancel();
        Global.syncTimerTask = new DatabaseSync();
        Global.syncTimer.schedule(Global.syncTimerTask, 0, Global.syncInterval*1000);
    }
}

