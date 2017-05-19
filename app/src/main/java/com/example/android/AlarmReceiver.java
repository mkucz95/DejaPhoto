package com.example.android;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
        String path =intent.getExtras().getString("path");

        Log.i("AlarmReciever", "action:  "+ action);

        if(ACTION_KARMA.equals(action) && Global.undoKarmaOn) {
            Log.i("AlarmReciever", "Karma Intent Received");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();

            //Global.displayCycle.get(DejaPhotoWidgetProvider.currIndex).setKarma(true);
            setData(true, path);
        }

        else if(ACTION_RELEASE.equals(action) && Global.undoReleaseOn){
            Log.i("AlarmReciever", "Release Intent Received");

            Global.undoKarmaOn = false; //alarm was fired so now it got turned off
            Global.undoReleaseOn = false; //alarm was fired so now it got turned off

            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();

            setData(false, path);
        }
    }

    public void setData(boolean flag, String path){

        for(int i = 0; i<Global.displayCycle.size(); i++){
            Photo photo = Global.displayCycle.get(i);
            if(photo.getPath() == path){
                if(flag) photo.setKarma(true);
                else Global.displayCycle.remove(i);  //TODO just delete from array list?
                break;
            }
        }
    }

}