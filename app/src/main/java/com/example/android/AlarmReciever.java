package com.example.android;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver
/*gets system messages when the system or other applications send broadcasts
* if the broadcast matches our class, it executes the onRecieve method
 */
{

    String typeIntent;

    @Override
    public void onReceive(Context context, Intent intent)
    { //what happens when the alarm goes off (timer expires)
        String info = this.typeIntent;
        Intent sendInfo = new Intent(context, WidgetManager.class);
        sendInfo.putExtra("button_pressed", info);
        context.startService(sendInfo);
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.ELAPSED_REALTIME, 150000, alarmIntent); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void sendInfo(String type){
        this.typeIntent = type;
    }
}