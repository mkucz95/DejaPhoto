package com.example.android;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.example.dejaphoto.R;
import java.util.Timer;

public class SetLocationActivity extends AppCompatActivity {

    EditText newLoc;
    public TextView currentLocation;
    public Button saveLocation;
    public Button defaultLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //stop the timer so the photo doesn't change while setting location
        if(Global.autoWallpaperChange != null && Global.undoTimer != null){
            Log.i("setLocAct", "Cancelling Alarm and Timer");
            Global.autoWallpaperChange.cancel();
            Global.undoTimer.cancel();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_set_location);

        currentLocation = (TextView) findViewById(R.id.curr_loc_name);
        String locToDisplay;

        if(Global.displayCycle.get(Global.head).userLocation) {
            if(Global.isBlank(Global.displayCycle.get(Global.head).userLocationString)) {
                locToDisplay = "No Location Found";
            }
            else {
                locToDisplay = "'" + Global.displayCycle.get(Global.head).userLocationString + "'";
            }
            currentLocation.setText(locToDisplay);
        }
        else if(Global.displayCycle.get(Global.head).photoLocation) {
            if(Global.isBlank(Global.displayCycle.get(Global.head).photoLocationString)) {
                locToDisplay = "No Location Found";
            }
            else{
                locToDisplay = Global.displayCycle.get(Global.head).photoLocationString;
            }
            currentLocation.setText(locToDisplay);
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
                    if(Global.isBlank(newLocation)){
                        newLocation = "No Location Found";
                    }

                    rviews.setTextViewText(R.id.display_location, newLocation);
                    Global.displayCycle.get(Global.head).userLocation = true;
                    Global.displayCycle.get(Global.head).photoLocation = false;
                    Global.displayCycle.get(Global.head).userLocationString = newLocation;

                    //save new loc to file via SQlite
                    FileManager.changeLoc(Global.displayCycle.get(Global.head).getPath(),
                            newLocation, getApplicationContext());

                    Log.i("widgetProv", Global.displayCycle.get(Global.head).userLocationString);
                }

               Log.i("setLocAct", "Saving...Starting new timer task");

                appWidgetManager.updateAppWidget(appWidgetIds, rviews);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        defaultLocation = (Button) findViewById(R.id.revert_default);
        defaultLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName AppWidget = new ComponentName(getApplicationContext(), DejaPhotoWidgetProvider.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);
                RemoteViews rviews = new RemoteViews(AppWidget.getPackageName(), R.layout.dejaphoto_appwidget_layout);
                if(Global.displayCycle.get(Global.head).photoLocationString != null) {
                    rviews.setTextViewText(R.id.display_location, Global.displayCycle.get(Global.head).photoLocationString);
                    Global.displayCycle.get(Global.head).userLocation = false;
                    Global.displayCycle.get(Global.head).photoLocation = true;
                }
                else{
                    rviews.setTextViewText(R.id.display_location, "No Location Found");
                    Global.displayCycle.get(Global.head).userLocation = true;
                    Global.displayCycle.get(Global.head).photoLocation = false;
                }
                appWidgetManager.updateAppWidget(appWidgetIds, rviews);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("setLocAct", "Destroying...Starting new timer task");
        Global.restartTimer(getApplicationContext());
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }


    @Override
    protected void onUserLeaveHint()
    {
        super.onUserLeaveHint();
        Log.i("setLocAct", "Home button pressed...Destroying...");

    }

}
