package com.example.android;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.MainThread;
import android.util.DisplayMetrics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * When an intent starts this service, this service reads the action (which contains the new
 * image path) and then sets that image to the wallpaper.
 */
public class WallpaperChanger extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String CHANGE_WALLPAPER = "com.example.android.action.BAZ";

    public WallpaperChanger() {
        super("WallpaperChanger");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
           synchronized (this){
               WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
               //call java wallpapermanager api

               DisplayMetrics metrics = new DisplayMetrics(); //get screen dimensions
               int height = metrics.heightPixels;
               int width = metrics.widthPixels;
               String imagePath = intent.getAction(); //takes info passed from intent

               try {//convert image path into something code can use
                   FileInputStream  imgIS  = new FileInputStream(new File(imagePath));
                   BufferedInputStream bufIS = new BufferedInputStream(imgIS);
                   Bitmap bitmap = BitmapFactory.decodeStream(bufIS); //

                   try{
                       wallpaperManager.setBitmap(bitmap); //set wallpaper with new image
                       wallpaperManager.suggestDesiredDimensions(width, height); //set dimensions
                   }catch(IOException e){
                       e.printStackTrace();
                   }
               }catch(FileNotFoundException e){ //catch fileinputstream exceptions
                   e.printStackTrace();
               } //trying to get wallpaper from display cycle node
           }
        stopService(intent);
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
