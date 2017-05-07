package com.example.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi; /*for WallpaperAuto()'s Calendar*/

public class BackgroundService extends Service {
    public BackgroundService() {
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }




    //@Override
    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void run(){
    public void WallpaperAutoChange(){
        //TODO implement AlarmManager
        /*if there are no buttons pressed for more than a certain time
        automatically call getImage() on display cycle and
         */
        //only happens when buttons are not pressed

//        Intent changeWallpaper = new Intent(this, WidgetManager.class);
//        changeWallpaper.setAction("next");
//        startService(changeWallpaper);

        /*Below code is to obtain time it has been since service starts*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

        /*set time of auto Wallpaper change: 5 min*/
        /*multiply 1000 because unit is mulli*/
        long repeatTime = 5*60*60*1000;

        /*Let AlarmManager know that wallpaper needs to be changed*/
        Intent intent = new Intent(this, Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* setRepeating() : so that it repeats itself*/
        am.setRepeating(AlarmManager.RTC, startTime, repeatTime, sender);

        /*immediately change the wallpaper when this method is called*/
        Intent i = new Intent(this, WallpaperChanger.class);
        startService(i); //startActivity(i);
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