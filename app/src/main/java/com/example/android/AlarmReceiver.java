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

        Log.i("AlarmReciever", "action:  "+ action);


        Intent sendInfo = new Intent(context, WidgetManager.class);
        sendInfo.setAction(Intent.ACTION_SEND);
        sendInfo.setType("text/plain");


        if(ACTION_KARMA.equals(action) && getAlarm("karma", context)) {
            Log.i("AlarmReciever", "Karma Intent Received");


            sendInfo.putExtra("button_pressed", "karma");
            setAlarm("karma", context, false); //reset alarm
            Toast.makeText(context, "Karma Added", Toast.LENGTH_SHORT).show();

            context.startService(sendInfo);
        }

        else if(ACTION_RELEASE.equals(action) && getAlarm("release", context)){
            Log.i("AlarmReciever", "Release Intent Received");
            sendInfo.putExtra("button_pressed", "release");


            setAlarm("release", context, false); //reset alarm
            setAlarm("karma", context, false); //reset alarm
            Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show();


            context.startService(sendInfo);
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

}