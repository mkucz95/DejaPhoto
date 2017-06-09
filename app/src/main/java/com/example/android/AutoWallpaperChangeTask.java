package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.TimerTask;

/**
 * Created by mkucz on 5/18/2017.
 * <p>
 * used to manage the auto wallpaper change feature as a TimerTask
 */

public class AutoWallpaperChangeTask extends TimerTask {
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    Context context;

    AutoWallpaperChangeTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.i("Timers", "AutoWallpaperChangeTask Called");
        Intent changeIntent = new Intent(context, ChangeImage.class);
        changeIntent.setAction(ACTION_NEXT); //show next picture
        context.startService(changeIntent); //call change image
    }

}
