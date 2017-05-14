package com.example.android;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver
/*gets system messages when the system or other applications send broadcasts
* if the broadcast matches our class, it executes the onRecieve method
 */
{
    private static final String ACTION_KARMA = "com.example.android.KARMA";

    @Override
    public void onReceive(Context context, Intent intent)
    { //what happens when the alarm goes off (timer expires)
        Log.i("AlarmReceiver", "AlarmReceiver got PendingIntent");

        String action = intent.getExtras().getString("action");

        Log.i("AlarmReciever", "action:  "+ action);


        Intent sendInfo = new Intent(context, WidgetManager.class);
        sendInfo.setAction(Intent.ACTION_SEND);
        sendInfo.setType("text/plain");


        if("karma".equals(action)) {
            sendInfo.putExtra("button_pressed", "karma");
            setAlarm("karma", context, false); //reset alarm
        }

        else {
            sendInfo.putExtra("button_pressed", "release");
            setAlarm("release", context, false); //reset alarm
        }

        context.startService(sendInfo);
    }

    public void setAlarm(String type, Context context, boolean pref){
        Log.i("AlarmReceiver", "called setAlarm: "+ type + pref);

        SharedPreferences sharedPref = context.getSharedPreferences("alarm", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(type, pref);
        editor.apply();
    }
}