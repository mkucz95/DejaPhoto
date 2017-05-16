package com.example.android;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


        if(ACTION_KARMA.equals(action) && getAlarm("karma", context)) {
            Log.i("AlarmReciever", "Karma Intent Received");


            setAlarm("karma", context, false); //reset alarm
            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();

            setData(true, path);
        }

        else if(ACTION_RELEASE.equals(action) && getAlarm("release", context)){
            Log.i("AlarmReciever", "Release Intent Received");


            setAlarm("release", context, false); //reset alarm
            setAlarm("karma", context, false); //reset alarm
            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();
        }


    }

    public void setAlarm(String type, Context context, boolean pref){
        Log.i("AlarmReceiver", "called setAlarm: "+ type + pref);

        SharedPreferences sharedPref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(type, pref);
        editor.apply();
    }

    public boolean getAlarm(String type, Context context){ //returns if alarm set or not
        SharedPreferences sharedPref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        return  sharedPref.getBoolean(type, false);
    }


    public void setData(boolean flag, String path){

        for(int i = 0; i<Global.displayCycle.size(); i++){
            Photo photo = Global.displayCycle.get(i);
            if(photo.getPath() == path){
                if(flag) photo.setKarma(true);
                else photo.setReleased(true);
            }
        }
    }


}