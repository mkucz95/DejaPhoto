package com.example.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi; /*for WallpaperAuto()'s Calendar*/
import android.util.Log;

public class BackgroundService extends Service {
    public BackgroundService()
    {
       //wallpaperAutoChange();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        /*Thread thread = new Thread(new MyThread(startId)); //TODO the mythread call gives error
        thread.start();*/
        //run();
        wallpaperAutoChange();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    //@Override
    @RequiresApi(api = Build.VERSION_CODES.N)

    public void wallpaperAutoChange(){
        //TODO implement AlarmManager

        /*Below code is to obtain time it has been since service starts*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        //TODO GET USER TIME FROM SHARED PREFERENCES

        /*set time of auto Wallpaper change: 5 min*/
        /*multiply 1000 because unit is mulli*/
        long repeatTime = 5*60*60*1000;
        Log.d("test befor alarm "," ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        /*Let AlarmManager know that wallpaper needs to be changed*/
        Intent intent = new Intent(this, Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* setRepeating() : so that it repeats itself*/
        am.setRepeating(AlarmManager.RTC, startTime, repeatTime, sender);

        Log.d("test alarm","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        /*immediately change the wallpaper when this method is called*/
        Intent clickIntent = new Intent(getApplicationContext(), WidgetManager.class);
        clickIntent.setAction(Intent.ACTION_SEND);
        clickIntent.setType("text/plain");
        clickIntent.putExtra("button_pressed", "next");
        startService(clickIntent); //calls widgetmanager as if the 'next' button was pressed
    }

    public class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive (Context context, Intent intent)
        {
            Intent i = new Intent(context, WallpaperChanger.class);

            //Bundle(key, value): a map from string values to various Parcelable types
            //Use Bundle to store Data
            Bundle bundleRet = new Bundle();
            bundleRet.putString("STR_CALLER", "");

            //transfer data from 1 act to another
            i.putExtras(bundleRet);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
