package com.example.android;

import android.content.Context;
import android.content.Intent;

import java.util.TimerTask;

/**
 * Created by mkucz on 5/18/2017.
 * <p>
 * used to manage the auto wallpaper change feature as a TimerTask
 */

public class AutoWallpaperChange extends TimerTask {
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    Context context;

    AutoWallpaperChange(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Intent changeIntent = new Intent(context, ChangeImage.class);
        changeIntent.setAction(ACTION_NEXT); //show next picture
        context.startService(changeIntent); //call change image
    }

}
