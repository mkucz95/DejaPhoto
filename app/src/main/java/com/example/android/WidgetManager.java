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
    //ACTION DEFINITIONS
    private static final String ACTION_PREVIOUS = "com.example.android.PREVIOUS";
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    public static final String ACTION_RELEASE = "com.example.android.RELEASE";

    public WidgetManager() {
        super("WidgetManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
                final String action = intent.getExtras().getString("button_pressed");
                boolean changeImage = false;
                boolean released = false;
                boolean karma = false;

                Intent imageIntent = new Intent(this, ChangeImage.class);
                Intent updateIntent = new Intent(this, UpdateImageInfo.class);
                Intent rerankIntent = new Intent(this, Rerank.class);


            //TODO implement actions
                if ("previous".equals(action)) {
                    imageIntent.setAction(ACTION_PREVIOUS);
                    changeImage = true;
                } else if ("next".equals(action)) {
                    imageIntent.setAction(ACTION_NEXT);
                    changeImage = true;
                }
                else if ("karma".equals(action)) {
                    updateIntent.setAction(ACTION_KARMA);
                    karma = true;
                } else if ("release".equals(action)) {
                    updateIntent.setAction(ACTION_RELEASE);
                    released = true;
                }


            if(changeImage) startService(imageIntent); //change the image
            if(released || karma) startService(updateIntent); //update information of picture
            if(released) startService(rerankIntent);

                stopService(intent); //stop the widgetManager service
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

}
