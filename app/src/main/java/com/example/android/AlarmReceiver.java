package com.example.android;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.ACTION_SYNC;

/*
this class is used to track the 15 seconds a currUser has to undo karma or release. if they don't undo
then it sends and intent service to

implemented based on a similar class structure as the DJWidgetProvider.java
 */

public class AlarmReceiver extends BroadcastReceiver
/*gets system messages when the system or other applications send broadcasts
* if the broadcast matches our class, it executes the onRecieve method
 */
{
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.RELEASE";

    @Override
    public void onReceive(Context context, Intent intent)
    { //what happens when the alarm goes off (undoTimer expires)
        Log.i("AlarmReceiver", "AlarmReceiver got PendingIntent");
        FileManager fileManager = new FileManager(context);

        String action = intent.getAction();

        Log.i("AlarmReciever", "action:  "+ action);

        if(ACTION_KARMA.equals(action) && Global.undoKarmaOn) {
            Log.i("AlarmReciever", "Karma");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();
            //Log.i("setKarma", "Karma added to : " + Global.displayCycle.get(Global.currIndex).getPath());
            //Photo p = Global.displayCycle.get(Global.currIndex);
            //p.setKarma(true);
            fileManager.setDisplayCycleData(true, Global.karmaPath);
            //Log.i("setKarma", Global.displayCycle.get(Global.currIndex).getPath() + "Karma: " + Global.displayCycle.get(Global.currIndex).isKarma());

        }

        else if(ACTION_RELEASE.equals(action) && Global.undoReleaseOn){
            Log.i("AlarmReciever", "Release");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Global.undoReleaseOn = false; //alarm was fired so now it got turned off

            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();

            fileManager.setDisplayCycleData(false, Global.releasePath);
        }
    }

}