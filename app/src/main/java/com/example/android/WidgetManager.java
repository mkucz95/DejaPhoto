package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

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
    public static  String imagePath = "DEFAULT_PICTURE";

    public WidgetManager() {
        super("WidgetManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getExtras().getString("button_pressed");

            //TODO implement actions
            if (PREVIOUS.equals(action)) {
                 imagePath = getImage(0);
            } else if (NEXT.equals(action)) {
                 imagePath = getImage(1);
            }else if (KARMA.equals(action)) {
                imagePath = getImage(2);
            }else if (RELEASE.equals(action)) {
                imagePath = getImage(3);
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

    public String getImage(int next){
        Intent imageIntent = new Intent(this, ChangeImage.class);
        if(next == 0) imageIntent.setAction(PREVIOUS);
        else if(next == 1) imageIntent.setAction(NEXT);
        else if (next == 2) imageIntent.setAction(KARMA);
        else if (next == 3)imageIntent.setAction(RELEASE);

        startService(imageIntent);

        SharedPreferences sharedPreferences = getSharedPreferences("current_image", MODE_PRIVATE);
        String imgPath = sharedPreferences.getString("image", ""); //get current image

        return imgPath;
    }
}
