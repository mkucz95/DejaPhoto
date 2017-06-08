package com.example.android;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

import org.w3c.dom.Text;

import java.util.Timer;

public class SetLocationActivity extends AppCompatActivity {

    EditText newLoc;
    public TextView currentLocation;
    public Button saveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_set_location);
        Log.i("widgetProv", "IN SETLOCATIONACTIVITY");


        currentLocation = (TextView) findViewById(R.id.curr_loc);

        Log.i("widgetProv", "++++++++++++++++++++++++++++");

        Log.i("widgetProv", "Photo Data Set Locations:");
        for(Photo p : Global.displayCycle){
            Log.i("widgetProv", "" + p.photoLocationString);
        }
        Log.i("widgetProv", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Log.i("widgetProv", "User Set Locations:");
        for(int i = 0; i < Global.displayCycle.size(); i++){
            Log.i("widgetProv", Global.displayCycle.get(i).userLocationString);

        }
        Log.i("widgetProv", "++++++++++++++++++++++++++++");
        Log.i("widgetProv", "Current: ");
        Log.i("widgetProv", "User Set: " + Global.displayCycle.get(Global.head).userLocationString);
        Log.i("widgetProv", "Photo Data: " + Global.displayCycle.get(Global.head).photoLocationString);
        Log.i("widgetProv", "---------------------------");
        String onCreate;
        if(Global.displayCycle.get(Global.head).userLocation) {
            onCreate = "Current Location: " + Global.displayCycle.get(Global.head).userLocationString;
            currentLocation.setText(onCreate);
        }
        else if(Global.displayCycle.get(Global.head).photoLocation) {
            onCreate = "Current Location: " + Global.displayCycle.get(Global.head).photoLocationString;
            currentLocation.setText(onCreate);
        }



        saveLocation = (Button) findViewById(R.id.save_loc);

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName AppWidget = new ComponentName(getApplicationContext(), DejaPhotoWidgetProvider.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);
                RemoteViews rviews = new RemoteViews(AppWidget.getPackageName(), R.layout.dejaphoto_appwidget_layout);

                newLoc = (EditText) findViewById(R.id.new_loc);
                String newLocation = newLoc.getText().toString();
                if(newLocation != null){
                    rviews.setTextViewText(R.id.display_location, newLocation);
                    Global.displayCycle.get(Global.head).userLocation = true;
                    Global.displayCycle.get(Global.head).photoLocation = false;
                    Global.displayCycle.get(Global.head).userLocationString = newLocation;
                    Log.i("widgetProv", Global.displayCycle.get(Global.head).userLocationString);

                }
                appWidgetManager.updateAppWidget(appWidgetIds, rviews);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }
}
