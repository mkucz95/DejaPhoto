package com.android.dejaphoto;

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

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final LinearLayout l = (LinearLayout) findViewById(R.id.l_Album);

        final Switch dejaAlbum = (Switch) findViewById(R.id.deja_album);
        final Switch cameraAlbum = (Switch) findViewById(R.id.camera_album);

        dejaAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cameraAlbum.setClickable(false);
                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "Deja Photo is chosen", Toast.LENGTH_SHORT).show();
                }
                else {
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
                if(isChecked) {
                    dejaAlbum.setClickable(false);
                    l.setBackgroundColor(Color.parseColor("#2DC0C5"));
                    Toast.makeText(getApplicationContext(),
                            "Camera Roll is chosen", Toast.LENGTH_SHORT).show();
                }
                else {
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
