package com.example.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends Service {
    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void run(){
        //TODO implement AlarmManager
        /*if there are no buttons pressed for more than a certain time
        automatically call getImage() on display cycle and
         */
        //only happens when buttons are not pressed
        Intent changeWallpaper = new Intent(this, WidgetManager.class);
        changeWallpaper.setAction("next");
        startService(changeWallpaper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Thread thread = new Thread(new MyThread(startId)); //TODO
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
