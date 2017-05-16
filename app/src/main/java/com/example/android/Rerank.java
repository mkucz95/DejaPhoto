package com.example.android;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This intent service creates a new rank object which has the correct order of the pictures
 * that are supposed to be displayed. this is then sent to the build display cycle
 */
public class Rerank extends IntentService {
    private static final String ACTION_RERANK_DISPLAY = "com.example.android.RERANK_DISPLAY";
    private static final String TAG = "RerankService";
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;


    private String myLat = "", myLong = "";

    public Rerank() {
        super("Rerank");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Intent Handled");
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("settings", 0);

            boolean isLocaOn = sharedPreferences.getBoolean("location", false);
            boolean isTimeOn = sharedPreferences.getBoolean("time", false);
            boolean isWeekOn = sharedPreferences.getBoolean("day", false);
            boolean isKarma = sharedPreferences.getBoolean("karma", false);
            ArrayList<Photo> list = gatherCycleInfo(); //populate the arraylist from file


            Log.d("rerank test1", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            //create new rank from the arraylist we collected, and pass in settings user chose
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ getting location...");
            getMyLocation();
            Log.d("rerank test2", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ got my location");
            Rank newRank = new Rank(list, getSettings(), myLat, myLong, isTimeOn, isLocaOn, isWeekOn, isKarma);
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ New Rank Created");
            String[] newPaths = newRank.getPaths(); //extract paths of relevant pictures

            Log.i("distanceRank", "Location Ranking: ");
            for(String s : newPaths){
                Log.i("distanceRank", "Path: " + s);
            }

            //Log.i(TAG, "this is path0: " + newPaths[0]); //test to see first path

            Bundle data = new Bundle();
            data.putStringArray("new_cycle", newPaths);

            Intent cycleIntent = new Intent(this, BuildDisplayCycle.class);
            cycleIntent.setAction(ACTION_RERANK_DISPLAY); //build new display cycle
            cycleIntent.putExtras(data);
            Log.i(TAG, "cycle intent extras: " + cycleIntent.getExtras());

            startService(cycleIntent);

            stopService(intent);
        }
    }


    public void getMyLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = "";
        Log.i(TAG, "------------------------In getMyLocation");
        List<String> providerList = lm.getProviders(true);
        for(String s : providerList){
            Log.i(TAG, "Provider: " + s);
        }
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            Log.i(TAG, "------------------------Using GPS Provider");

        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.i(TAG, "------------------------Using Network Provider");
        } else
            Toast.makeText(this, "no location provider to use", Toast.LENGTH_SHORT).show();

        Boolean permissions = (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

        Log.i(TAG, "Permissions: " + permissions);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

        Location location = lm.getLastKnownLocation(provider);

        if(location != null){
            Log.i(TAG, "------------------------Got Location: " + location);
            double longitude = location.getLongitude();
            Log.i(TAG, "------------------------Set Longitude t: " + longitude);
            double latitude = location.getLatitude();
            Log.i(TAG, "------------------------Set Latitude t: " + latitude);
            Log.i(TAG, "------------------------My Long: " + myLong + ", My Lat: " + myLat);
            this.myLat = Double.toString(latitude);
            this.myLong = Double.toString(longitude);
        }
    }

/*
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        Log.i("permission", "Requesting Permission");
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            Log.i("permission", "checking...");
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Permission denied
                Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

    /*
    this method extracts relevant information (karma released, date, time, location, path etc)
    and creates an arraylist that is then used to create a new display cycle rank
     */

    public ArrayList<Photo> gatherCycleInfo(){
        ArrayList<Photo> pictures = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.DESCRIPTION, MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};
        //which columns we will get (all in this case)

        Cursor cr = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */

        if(null == cr) {
            Log.i(TAG, "ERROR null==cr in BuildDisplayCycle");
        }else if( cr.getCount()<1) {
            Log.i(TAG, "NO IMAGES PRESENT");
        } else { //handle returned data
            Log.i(TAG, "IMAGES PRESENT");
            Log.i(TAG, "uri to access"+uri.toString());
            Log.i(TAG, "name "+cr.getColumnName(0)+", cr.count "+ cr.getCount());

            cr.moveToFirst();


            int[] columns = {cr.getColumnIndex(MediaStore.MediaColumns.DATA),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)};
            //used below to access the columns we want

             do{ //go through all the images

                 for(int i = 0; i<5; i++){
                     Log.i(TAG, "column"+i+" ----------------------------------- "+cr.getString(columns[i]));
                 }

                 Photo photo = new Photo(cr.getString(columns[0]), cr.getString(columns[1]),
                         cr.getString(columns[2]), cr.getString(columns[3]),
                         cr.getString(columns[4]));

                 pictures.add(photo);

                Log.i(TAG, "added new photo object to list");
            } while(cr.moveToNext());
        }

        if (cr != null) {
            cr.close();
        }

        return pictures;
    }


    /*
    this function accessed shared preferences to see what settings the user has selected for their
    ranking, and returns that in a boolean array, default is true for all settings!
     */
    public boolean[] getSettings(){
        boolean[] settings = {true, true, true, true};
        String[] type = {"location", "time", "day", "karma"};

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
      for(int i=0; i<settings.length; i++) {
          settings[i] = sharedPreferences.getBoolean(type[i], true);
      }
      return settings;
    }


}
