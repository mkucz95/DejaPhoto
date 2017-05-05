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
 * helper methods.
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

               String imagePath = "TESTSTRING"; //TODO get info from display cycle node
               String defaultImage = "DEFAULT IMAGE/EMPTY";
               FileInputStream imgIS = null;

               try {
                  imgIS  = new FileInputStream(new File(imagePath));
               }catch(FileNotFoundException e){
                   e.printStackTrace();
               } //trying to get wallpaper from display cycle node

               if (imgIS == null){ //filenotfound
                   try{
                       imgIS  = new FileInputStream(new File(defaultImage));
                   }catch(FileNotFoundException e){
                       e.printStackTrace();
                   } //if unsuccesful, then try to load default image
               }
               //TODO implement getting imagepath or data from DisplayCycle node

               BufferedInputStream bufIS = new BufferedInputStream(imgIS);

               Bitmap bitmap = BitmapFactory.decodeStream(bufIS);

               try{
                   wallpaperManager.setBitmap(bitmap);
                   wallpaperManager.suggestDesiredDimensions(width, height);
               }catch(IOException e){
                   e.printStackTrace();
               }
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
