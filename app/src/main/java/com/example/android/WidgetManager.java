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
    public static  String imagePath = "DEFAULTPICTURE";

    public WidgetManager() {
        super("WidgetManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getExtras().getString("button_pressed");
            //TODO implement actions
            if (PREVIOUS.equals(action)) {
               // imagePath = getImage(true);
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
            wallpaperIntent.setAction(Intent.ACTION_SEND);
            wallpaperIntent.putExtra("image_path", imagePath);
            wallpaperIntent.setType("text/plain");

            startService(wallpaperIntent); //change the wallpaper

            stopService(intent); //stop the widgetManager service
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
}
