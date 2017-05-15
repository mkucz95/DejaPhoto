package com.example.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

             if ("previous".equals(action)) {
                    imageIntent.setAction(ACTION_PREVIOUS);
                    changeImage = true;
                } else if ("next".equals(action)) {
                    imageIntent.setAction(ACTION_NEXT);
                    changeImage = true;
                }
                else if ("karma".equals(action)) {
                    if(getCounter() != -1) {//only call karma if we have one or more pictures!
                        updateIntent.setAction(Intent.ACTION_SEND);
                        updateIntent.putExtra("path", getPath("karma"));
                        updateIntent.putExtra("type", karma);
                       // Toast.makeText(this.getApplicationContext(), "Karma Addded", Toast.LENGTH_SHORT).show();
                        Log.i("AlarmReciever", "widget manager recieved karma intent");
                        karma = true;
                    }
                    else{
                       // Toast.makeText(this.getApplicationContext(), "Cannot Add Karma", Toast.LENGTH_SHORT).show();
                    }
                } else if ("release".equals(action)) {
                    if(getCounter() != -1) { //only call release if we have one or more pictures!
                        updateIntent.setAction(Intent.ACTION_SEND);
                        updateIntent.putExtra("path", getPath("release"));
                        updateIntent.putExtra("type", "release");
                        Log.i("AlarmReciever", "releasing :" + getPath("release"));


                        //Toast.makeText(this.getApplicationContext(), "Photo Released", Toast.LENGTH_SHORT).show();
                        Log.i("AlarmReciever", "widget manager recieved release intent");

                        released = true;
                    }
                    else{
                      //  Toast.makeText(this.getApplicationContext(), "Cannot Release Image", Toast.LENGTH_SHORT).show();
                    }
                }

            if(changeImage) startService(imageIntent); //change the image
            if(released || karma) startService(updateIntent); //update information of picture
            if(released) startService(rerankIntent); //once released rerank the service

                stopService(intent); //stop the widgetManager service
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
    //return the counter #
    public int getCounter(){
        SharedPreferences counter = getSharedPreferences("counter", MODE_PRIVATE);
        return  counter.getInt("counter", -1);
    }

    //find what head the release or karma was pressed and return the path at that head
    public String getPath(String type){
        SharedPreferences cycleAction = getSharedPreferences("cycleAction", MODE_PRIVATE);
        String path = cycleAction.getString(type, "");
        Log.i("WidgetManager", "save path to release, "+ path);
        return path;

    }

}
