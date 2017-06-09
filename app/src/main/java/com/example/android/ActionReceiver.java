package com.example.android;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.util.ArrayList;
import java.util.Timer;

import static android.content.Intent.ACTION_SYNC;

/*
this class is used to track the 15 seconds a currUser has to undo karma or release. if they don't undo
then it sends and intent service to

implemented based on a similar class structure as the DJWidgetProvider.java
 */

public class ActionReceiver extends BroadcastReceiver
/*gets system messages when the system or other applications send broadcasts
* if the broadcast matches our class, it executes the onRecieve method
 */ {
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.RELEASE";
    private final String TAG = "PhotoInfoReciever";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) { //what happens when the alarm goes off (undoTimer expires)
        this.context = context;

        Log.i(TAG, "PhotoInfoReciever got PendingIntent");
        FileManager fileManager = new FileManager(context);

        String action = intent.getAction();

        Log.i(TAG, "action:  " + action);

        if (ACTION_KARMA.equals(action) && Global.undoKarmaOn) {
            Log.i(TAG, "Karma");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName AppWidget = new ComponentName(context, DejaPhotoWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);
            RemoteViews rviews = new RemoteViews(AppWidget.getPackageName(), R.layout.dejaphoto_appwidget_layout);


            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();

            //Set karma num on widget
            rviews.setTextViewText(R.id.karma_num, "Karma: " + Global.karmaNum);
            appWidgetManager.updateAppWidget(appWidgetIds, rviews);

            Log.i("updateInterval", "____________________");
            Log.i("updateInterval", "Restarting timer from AlarmReceiver: Karma");
            Global.restartTimer(context);

            FileManager.addKarma(Global.karmaPath, context);
            fileManager.setDisplayCycleData(true, Global.karmaPath);
            fileManager.addToQueue(Global.karmaPath);

<<<<<<< HEAD:app/src/main/java/com/example/android/AlarmReceiver.java
        }

        else if(ACTION_RELEASE.equals(action) && Global.undoReleaseOn){
            Log.i("AlarmReciever", "Release");
=======
        } else if (ACTION_RELEASE.equals(action) && Global.undoReleaseOn) {
            Log.i(TAG, "Release");
>>>>>>> dd77280208c0d39aae984ebcd146285629b8972b:app/src/main/java/com/example/android/ActionReceiver.java

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Global.undoReleaseOn = false; //alarm was fired so now it got turned off

            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();

            Log.i("updateInterval", "____________________");
            Log.i("updateInterval", "Restarting timer from AlarmReceiver: Release");
            Global.restartTimer(context);

            fileManager.setDisplayCycleData(false, Global.releasePath);
        }
    }


}