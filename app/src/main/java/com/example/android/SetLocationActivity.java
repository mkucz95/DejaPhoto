package com.example.android;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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

        //stop the timer so the photo doesn't change while setting location
        if(Global.autoWallpaperChange != null && Global.undoTimer != null){
            Log.i("setLocAct", "Cancelling Alarm and Timer");
            Global.autoWallpaperChange.cancel();
            Global.undoTimer.cancel();
            Log.i("setLocAct", "Timer task cancelled: " + Global.autoWallpaperChange.cancel());
        }

        Log.i("setLocAct", "Timer Cancelled...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_set_location);
        Log.i("widgetProv", "IN SETLOCATIONACTIVITY");


        currentLocation = (TextView) findViewById(R.id.curr_loc_name);

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
                    Log.i("widgetProv", Global.displayCycle.get(Global.head).userLocationString);

                }
                //Reset timer
                Log.i("setLocAct", "Saving...Starting new timer task");
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
        //finish();
        Log.i("setLocAct", "Destroying...Starting new timer task");
        Global.autoWallpaperChange = new AutoWallpaperChangeTask(getApplicationContext());
        Global.undoTimer = new Timer();
        Global.undoTimer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    @Override
    protected void onUserLeaveHint()
    {
        super.onUserLeaveHint();
        Log.i("setLocAct", "Home button pressed...Destroying...");
        finish();

    }

}
