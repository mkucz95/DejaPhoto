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
import java.util.List;

/**
 * This intent service creates a new rank object which has the correct order of the pictures
 * that are supposed to be displayed. this is then sent to the build display cycle
 */
public class Rerank extends IntentService {
    private static final String ACTION_RERANK_DISPLAY = "com.example.android.RERANK_DISPLAY";
    private static final String TAG = "RerankService";

    private String myLat = "", myLong = "";

    public Rerank() {
        super("Rerank");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Intent Handled");
        if (intent != null) {
            ArrayList<Photo> list = gatherCycleInfo(); //populate the arraylist from file


            Log.d("rerank test1","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            //create new rank from the arraylist we collected, and pass in settings user chose
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ getting location...");
            getMyLocation();
            Log.d("rerank test2","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ got my location");
            Rank newRank = new Rank(list, getSettings(), myLat, myLong);
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ New Rank Created");
            String[] newPaths = newRank.getPaths(); //extract paths of relevant pictures
            Log.i(TAG, "this is path0: " + newPaths[0]); //test to see first path

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: REQUEST PERMISSIONS IF NOT SET
            Log.i(TAG, "------------------------Getting Permissions");

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //ActivityCompat.requestPermissions(MainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }

        String provider = "";
        Log.i(TAG, "------------------------In getMyLocation");
        List <String> providerList = lm.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if (providerList.contains(LocationManager.NETWORK_PROVIDER))
            provider = LocationManager.NETWORK_PROVIDER;
        else
            Toast.makeText(this,"no location provider to use",Toast.LENGTH_SHORT).show();
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
