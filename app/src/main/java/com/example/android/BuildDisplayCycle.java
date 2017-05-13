package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

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
    private static final String ACTION_NEW = "com.example.android.NEW";
    private static final String ACTION_RERANK_DISPLAY = "com.example.android.RERANK_DISPLAY";
    private static final String TAG = "BuildCycle";

    public BuildDisplayCycle() {
        super("BuildDisplayCycle");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, intent.toString());

        if (intent != null) {
            final String action = intent.getAction();
            Log.i(TAG, action);

           if (ACTION_BUILD_CYCLE.equals(action)) {
                //buildFromFile(sourceFolder);
                Log.i(TAG, "Building cycle from MEDIA...");
                buildFromMedia();
                displayImage();  //once building the cycle is finished, display the first image
           }
            else if(ACTION_RERANK_BUILD.equals(action)){
                Log.i(TAG, "Building Cycle from STRING...");

               Intent rerankIntent = new Intent(this.getApplicationContext(), Rerank.class);
               startService(rerankIntent);

            } else if(ACTION_RERANK_DISPLAY.equals(action)){
               Log.i(TAG, "RERANK INTENT: " + intent.getExtras());
               //Log.i(TAG, "RERANK INTENT EXTRAS: " + rerankIntent.getExtras());
               Bundle newPaths = intent.getExtras();
               String[] paths = newPaths.getStringArray("new_cycle");

               //String[] paths = rerankIntent.getExtras().getStringArray("new_cycle");
               buildFromString(paths);
               displayImage();  //once building the cycle is finished, display the first image
           }

            Log.i(TAG, "Stopping service");
            stopService(intent);
        }
    }

    private void buildFromString(String[] paths) { //would be used to get sorted information
        clearDisplayCycle();
        clearSharedPreferences("head");
        clearSharedPreferences("counter");

        int picNum=-1;

            if (paths != null) { //DCIM contains photos
                picNum = 0;
                for (String currPicture : paths) { //add each photo's path to cycle as a node

                    savePicture(currPicture, picNum);
                    picNum++;
                }
            } else {
                savePicture("DEFAULTPICTURE", picNum);
            }

            saveHeadCount(picNum);
    }

    private void buildFromMedia() {
        clearDisplayCycle(); //just in case
        clearSharedPreferences("head");
        clearSharedPreferences("counter");

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA}; //which columns we will get (all in this case)
        Cursor cr = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

        Log.i(TAG, "uri to access"+uri.toString());
        Log.i(TAG, "name, "+cr.getColumnName(0)+"cr.count "+ cr.getCount());

        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */

        int picNum=-1;

        if(null == cr) {
            Log.i(TAG, "ERROR null==cr in BuildDisplayCycle");
        }else if( cr.getCount()<1) {
            Log.i(TAG, "NO IMAGES PRESENT");
            savePicture("DEFAULTPICTURE", picNum);
        } else { //handle returned data
            Log.i(TAG, "IMAGES PRESENT");
            cr.moveToFirst();
            int pathIndex = cr.getColumnIndex(MediaStore.MediaColumns.DATA);
            int description = cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION);
            picNum = 0;
           do{ //go through all the images
               String uripath = cr.getString(pathIndex);  //get the path and other info that is specified

               Log.i(TAG+" fromMedia", uripath);
               Log.i(TAG+" fromMedia", "INDEX OF PICTURE: "+picNum);

               savePicture(uripath, picNum);
               picNum++;
           } while(cr.moveToNext());
        }

        saveHeadCount(picNum); //save information to shared preferences

            if (cr != null) {
                cr.close();
            }
    }

    public void savePicture(String path, int picNum){ //puts picture to shared preferences using string path
        Log.i(TAG, "# of pics: " + (picNum + 1));

        //add the key-value pair of picPath/counter to shared preferences
        SharedPreferences displayCyclePreferences = getSharedPreferences("display_cycle", MODE_PRIVATE);
        //name of the preference is display cycle
        SharedPreferences.Editor displayCycleEditor = displayCyclePreferences.edit();
        //save the counter as a key string (will be searched by this string
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

    public void clearDisplayCycle(){
        SharedPreferences displayCyclePreferences = getSharedPreferences("display_cycle", MODE_PRIVATE);
        SharedPreferences.Editor displayCycleEditor = displayCyclePreferences.edit();
        displayCycleEditor.clear(); //remove all images from display cycle
        displayCycleEditor.apply();

        clearSharedPreferences("head");
        clearSharedPreferences("counter");
    }

    public void saveHeadCount(int numImages){
        //save the number of pictures we have in get count
        SharedPreferences counterPref = getSharedPreferences("counter", MODE_PRIVATE);
        SharedPreferences headPref = getSharedPreferences("head", MODE_PRIVATE);

        SharedPreferences.Editor editor = counterPref.edit();
        SharedPreferences.Editor headEdit = headPref.edit();

        editor.clear();
        editor.putInt("counter", numImages); //initialize the counter to the number we have

        headEdit.clear();
        headEdit.putInt("head", 0); //start head at 0

        headEdit.apply();
        editor.apply();
    }

    private void displayImage(){
        Intent intent = new Intent(this, ChangeImage.class);
        intent.setAction(ACTION_NEW);

        startService(intent);
    }
}

/*
    private void buildFromFile(boolean sourceFolder) {
        clearSharedPreferences("display_cycle");
        int picNum=-1;

        if (sourceFolder) {
            File dcimDirectory = new File(Environment.getExternalStorageDirectory(), "DCIM"); //get path to DCIM folder
            File cameraDirectory = new File(dcimDirectory.getAbsolutePath() + "/Camera");

            File[] dcimPhotos = cameraDirectory.listFiles();
            if (dcimPhotos != null) { //DCIM contains photos
                picNum = 0;
                for (File currPicture : dcimPhotos) { //add each photo's path to cycle as a node
                    savePicture(currPicture.getAbsolutePath(), ++picNum);
                }
            } else {
                savePicture("DEFAULTPICTURE", picNum);
            }
        } else {
        }

        saveHeadCount(picNum);

    }*/