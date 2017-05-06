package com.example.android;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WidgetManager extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String PREVIOUS = "previous";
    public static final String NEXT = "next";
    public static final String KARMA = "karma";
    public static final String RELEASE = "release";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.android.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.android.extra.PARAM2";
    public static  String imagePath = "";



    public WidgetManager() {
        super("WidgetManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            //TODO implement actions
            if (PREVIOUS.equals(action)) {
              //  imagePath = getImage(true);
            } else if (NEXT.equals(action)) {
               // imagePath = getImage(false);
            }else if (KARMA.equals(action)) {
               // imagePath = updateCycle(true);
            }else if (RELEASE.equals(action)) {
               // imagePath = updateCycle(false);
            }

            //send new intent to the wallpaper changer intent service
            //includes file path
            Intent wallpaperIntent = new Intent(this, WallpaperChanger.class);
            intent.setAction(imagePath);

            stopService(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
}