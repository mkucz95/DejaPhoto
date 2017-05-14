package com.example.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

public class IntervalActivity extends AppCompatActivity {

    private EditText timeSpecify;
    private Button saveButton;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        timeSpecify = (EditText) findViewById(R.id.user_specify);
        saveButton = (Button) findViewById(R.id.bt_7);

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void save(View view) {
        input = (EditText) findViewById(R.id.user_specify);

        SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Interval Preference", input.getText().toString());
        editor.apply();
        Toast.makeText(IntervalActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void show(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Deja Photo", MODE_PRIVATE);
        String min = sharedPreferences.getString("Interval Preference", "");
        TextView showMin = (TextView) findViewById(R.id.textMin);

        showMin.setText(min);
    }

}