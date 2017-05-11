package com.example.android;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Manifest;

/**
 * ideas guided by https://developer.android.com/guide/topics/providers/content-provider-basics.html
 * and https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#query(android.content.ContentResolver, android.net.Uri, java.lang.String[], java.lang.String, java.lang.String)
 * https://developer.android.com/reference/android/provider/MediaStore.Images.ImageColumns.html#BUCKET_ID
 * https://developer.android.com/reference/android/provider/MediaStore.MediaColumns.html#DATA
 * https://developer.android.com/reference/android/database/Cursor.html
 */
public class BuildDisplayCycle extends IntentService {
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String ACTION_RERANK_BUILD = "com.example.android.RERANK_BUILD";
    String[] paths;

    public BuildDisplayCycle() {
        super("BuildDisplayCycle");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
           //boolean sourceFolder = intent.getExtras().getBoolean("source");

            if (ACTION_BUILD_CYCLE.equals(action)) {
                //buildFromFile(sourceFolder);
                buildFromMedia();
            }
            else if(ACTION_RERANK_BUILD.equals(action)){
                buildFromString(paths);
            }

            /*
            else if(ACTION_NEW_PHOTO.equals(action)){
            }
            */

            stopService(intent);
        }
    }

    private void buildFromFile(boolean sourceFolder) {
        clearSharedPreferences("display_cycle");

        if (sourceFolder) {
            File dcimDirectory = new File(Environment.getExternalStorageDirectory(), "DCIM"); //get path to DCIM folder
            File cameraDirectory = new File(dcimDirectory.getAbsolutePath() + "/Camera"); //TODO

            int picNum=0;

            File[] dcimPhotos = cameraDirectory.listFiles();
            if (dcimPhotos != null) { //DCIM contains photos
                for (File currPicture : dcimPhotos) { //add each photo's path to cycle as a node
                    picNum++;
                    savePicture(currPicture.getAbsolutePath(), picNum);
                }
            } else {
                savePicture("DEFAULTPICTURE", picNum);
            }
        } else {
        }
    }

    private void buildFromString(String[] paths) { //would be used to get sorted information
            int picNum=0;

            if (paths != null) { //DCIM contains photos
                for (String currPicture : paths) { //add each photo's path to cycle as a node
                    picNum++;
                    savePicture(currPicture, picNum);
                }
            } else {
                savePicture("DEFAULTPICTURE", picNum);
            }
    }

    private void buildFromMedia() {


        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DESCRIPTION, MediaStore.Images.ImageColumns.LATITUDE}; //which columns we will get (all in this case)
        Cursor cr = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */

        int picNum=0;

        if(null==cr) {
            System.out.println("ERROR null==cr in BuildDisplayCycle");
        }else if( cr.getCount()<1) {
          //todo handle no images present---- send default image
            savePicture("DEFAULTPICTURE", picNum);
        } else { //handle returned data

            cr.moveToFirst();
            int pathIndex = cr.getColumnIndex(MediaStore.MediaColumns.DATA);
            int description = cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION);

            while(cr.moveToNext()) { //go through all the images
                /*String released = cr.getString(description);
                if(released == "released") continue; //read release from image description
                */

                String uripath = cr.getString(pathIndex);  //get the path and other info that is specified
                picNum++;
                savePicture(uripath, picNum);
            }
        }

        //save the number of pictures we have in get count
        SharedPreferences counterPref = getSharedPreferences("counter", MODE_PRIVATE);
        SharedPreferences headPref = getSharedPreferences("head", MODE_PRIVATE);

        SharedPreferences.Editor editor = counterPref.edit();
        SharedPreferences.Editor headEdit = headPref.edit();

        editor.putInt("counter", picNum); //initialize the counter to the number we have
        headEdit.putInt("head", 0); //start head at 0

        headEdit.apply();
        editor.apply();

            if (cr != null) {
                cr.close();
            }
    }

    public void savePicture(String path, int picNum){ //puts picture to shared preferences using string path
        System.out.println("Number of pics" + picNum);

        //add the key-value pair of picPath/counter to shared preferences
        SharedPreferences displayCyclePreferences = getSharedPreferences("display_cycle", MODE_PRIVATE);
        //name of the preference is display cycle
        SharedPreferences.Editor displayCycleEditor = displayCyclePreferences.edit();
        //save the coutner as a key string (will be searched by this string
        //the value of the pair is the absolute path to the image
        displayCycleEditor.putString(Integer.toString(picNum), path);
        displayCycleEditor.apply();
    }

    public void clearSharedPreferences(String type){
        SharedPreferences sharedPreferences = getSharedPreferences(type, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //display cycle cleared
    }
}