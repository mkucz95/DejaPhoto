package com.example.android;

import android.app.Activity;
import android.app.IntentService;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.LocationListener;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.dejaphoto.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * When an intent starts this service, this service reads the action (which contains the new
 * image path) and then sets that image to the wallpaper.
 */
public class WallpaperChanger extends IntentService {

    DisplayMetrics metrics = new DisplayMetrics(); //get screen dimensions
    //getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int height = metrics.heightPixels;
    int width = metrics.widthPixels;
    public RemoteViews rviews;


    public WallpaperChanger() {
        super("WallpaperChanger");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            synchronized (this){
                String imagePath = intent.getExtras().getString("image_path"); //takes info passed from intent
                Bitmap bitmap;
                if(imagePath.equals("DEFAULTPICTURE")){
                    bitmap = BitmapFactory.decodeResource( this.getResources(), R.drawable.default_picture);
                    setBackground(bitmap);
                }

                else {
                    try {//convert image path into something code can use
                        Log.d("WallpaperChanger", "USING IMAGEPATH: " + imagePath);
                        //FindLocationName locationName = new FindLocationName(imagePath);
                        //TextView locationView = (TextView) Activity.findViewById(R.id.display_location);
                        FileInputStream imgIS = new FileInputStream(new File(imagePath));
                        BufferedInputStream bufIS = new BufferedInputStream(imgIS);
                        bitmap = BitmapFactory.decodeStream(bufIS); //
                        Log.i("WallpaperChanger", "Setting background...");
                        setBackground(bitmap);

                    } catch (FileNotFoundException e) { //catch fileinputstream exceptions
                        e.printStackTrace();
                    } //trying to get wallpaper from display cycle node
                }
                updateWidget(imagePath);
            }
            stopService(intent);
        }
    }

    public void updateWidget(String path){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName AppWidget = new ComponentName(this.getPackageName(), DejaPhotoWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);

        rviews = new RemoteViews(AppWidget.getPackageName(), R.layout.dejaphoto_appwidget_layout);
        Geocoder gc = new Geocoder(this.getApplicationContext(), Locale.getDefault());//Locale.getDefault()follow the system's language
        PhotoLocation locName = new PhotoLocation(path, gc);
        rviews.setTextViewText(R.id.display_location, locName.locationName);
        Log.i("updateLocation", "Updating location...");
        appWidgetManager.updateAppWidget(appWidgetIds, rviews);
    }

    public void setBackground(Bitmap bitmap){ //set wallpaper based on inputted bitmap
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        //call java wallpapermanager api

        try {
            wallpaperManager.setBitmap(bitmap); //set wallpaper with new image
            //wallpaperManager.suggestDesiredDimensions(width, height); //set dimensions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
