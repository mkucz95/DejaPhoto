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

/*
CURRENTLY NOT REQUIRED FOR MILESTONE 1
 */
public class AlbumActivity extends AppCompatActivity {

    private Switch dejaAlbum;
    private Switch cameraAlbum;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dejaAlbum = (Switch) findViewById(R.id.deja_album);
        cameraAlbum = (Switch) findViewById(R.id.camera_album);

        setDejaAlbum();
    }

    public void setDejaAlbum() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Deja Photo", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (sharedPreferences.getBoolean("Deja Album Preference", false) == true) {
            dejaAlbum.setChecked(true);
            cameraAlbum.setClickable(false);

        } else {
            dejaAlbum.setChecked(false);
        }

        if (sharedPreferences.getBoolean("Camera Album Preference", false) == true) {
            cameraAlbum.setChecked(true);
            dejaAlbum.setClickable(false);
        } else {
            cameraAlbum.setChecked(false);
        }

        final LinearLayout l = (LinearLayout) findViewById(R.id.l_Album);
        dejaAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Deja Album Preference", true);
                    editor.commit();

                    cameraAlbum.setClickable(false);
                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "Deja Photo is chosen", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Deja Album Preference", false);
                    editor.commit();

                    cameraAlbum.setClickable(true);
                    l.setBackgroundColor(Color.parseColor("#1BEA44"));
                    Toast.makeText(getApplicationContext(),
                            "No chosen Deja Photo", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cameraAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Camera Album Preference", true);
                    editor.apply();

                    dejaAlbum.setClickable(false);
                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "Camera Roll is chosen", Toast.LENGTH_SHORT).show();

                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Camera Album Preference", false);
                    editor.apply();

                    dejaAlbum.setClickable(true);
                    l.setBackgroundColor(Color.parseColor("#1BEA44"));
                    Toast.makeText(getApplicationContext(),
                            "No Chosen Camera Roll", Toast.LENGTH_SHORT).show();
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