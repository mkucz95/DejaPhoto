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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Intent.ACTION_SYNC;

/* Class: ActionReceiver
 * Function: this class is used to track the 15 seconds a currUser has to undo karma or release.
 *           if they don't undo then it sends and intent service to
 *
 *           implemented based on a similar class structure as the DJWidgetProvider.java
 */
public class ActionReceiver extends BroadcastReceiver {

    /* Variables Declaration
     *
     * gets system messages when the system or other applications send broadcasts
     * if the broadcast matches our class, it executes the onRecieve method
     */

    private static final String ACTION_KARMA = "com.example.android.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.RELEASE";
    private final String TAG = "PhotoInfoReciever";
    Context context;

    /* Method: onReceive
     * Param: Context context, Intent intent
     * Function: this method is responding to the karma and release button event
     *           and make corresponding action
     * Return: none
     */
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
            int newKarma = Global.displayCycle.get(Global.head).getKarma() + 1;
            rviews.setTextViewText(R.id.karma_num, "Karma: " + newKarma);
            appWidgetManager.updateAppWidget(appWidgetIds, rviews);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            String filename = Global.displayCycle.get(Global.head).getPath().substring(Global.displayCycle.get(Global.head).getPath().lastIndexOf("/") + 1);

            reference.child("photos").child(Friends.getFriends(Global.currUser.email).get(0).replace(".", ",")).child(filename.replace(".", ",")).child("karma").setValue(newKarma);
            Log.i("AddKarma", "photos -> " + Friends.getFriends(Global.currUser.email).get(0).replace(".", ",") + " -> " + filename.replace(".", ",") + " -> karma " + newKarma);


            Global.restartTimer(context);

            // Delegate FileManager to take corresponding actions
            FileManager.addKarma(Global.karmaPath, context);
            fileManager.setDisplayCycleData(true, newKarma, Global.karmaPath);
            fileManager.addToQueue(Global.karmaPath);
        }
        else if (ACTION_RELEASE.equals(action) && Global.undoReleaseOn) {
            Log.i(TAG, "Release");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Global.undoReleaseOn = false; //alarm was fired so now it got turned off

            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();

            Global.restartTimer(context);

            fileManager.setDisplayCycleData(false, 0, Global.releasePath);
        }
    }


}