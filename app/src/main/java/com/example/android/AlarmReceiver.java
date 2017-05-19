package com.example.android;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/*
this class is used to track the 15 seconds a user has to undo karma or release. if they don't undo
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
    { //what happens when the alarm goes off (timer expires)
        Log.i("AlarmReceiver", "AlarmReceiver got PendingIntent");

        String action = intent.getAction();

        Log.i("AlarmReciever", "action:  "+ action);


        if(ACTION_KARMA.equals(action) && Global.undoKarmaOn) {
            Log.i("AlarmReciever", "Karma Intent Received");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();
            //Log.i("setKarma", "Karma added to : " + Global.displayCycle.get(Global.currIndex).getPath());
            //Photo p = Global.displayCycle.get(Global.currIndex);
            //p.setKarma(true);
            setData(true, Global.karmaPath);
            //Log.i("setKarma", Global.displayCycle.get(Global.currIndex).getPath() + "Karma: " + Global.displayCycle.get(Global.currIndex).isKarma());

        }

        else if(ACTION_RELEASE.equals(action) && Global.undoReleaseOn){
            Log.i("AlarmReciever", "Release Intent Received");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Global.undoReleaseOn = false; //alarm was fired so now it got turned off

            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();

            setData(false, Global.releasePath);

        }
    }

    public void setData(boolean flag, String path){
        ArrayList<Photo> temp = Global.displayCycle;
        for(int i = 0; i<temp.size(); i++){
            Photo photo = temp.get(i);
            Log.i("setKarma", path + " compare to : " + photo.getPath());
            if(photo.getPath().equals(path)){
                if(flag) {
                    Log.i("setKarma", temp.get(i) + ": added karma");
                    photo.setKarma(true);
                }
                else photo.setReleased(true);  //TODO just delete from array list?
            }
            Log.i("setKarma", photo.getPath() + ": karma:  "+ photo.isKarma());


        }
        Global.displayCycle = temp;
        for(Photo p: Global.displayCycle){
            Log.i("setKarma", p.getPath() + ": karma:  "+ p.isKarma());
        }
    }


}