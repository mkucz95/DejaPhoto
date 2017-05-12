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
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;

import static com.example.android.MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

/**
 * This intent service creates a new rank object which has the correct order of the pictures
 * that are supposed to be displayed. this is then sent to the build display cycle
 */
public class Rerank extends IntentService {
    private static final String ACTION_RERANK_BUILD = "com.example.android.RERANK_BUILD";
    private static final String TAG = "RerankService";
    public ArrayList<Photo> list;

    private String myLat = "", myLong = "";

    public Rerank() {
        super("Rerank");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        if (intent != null) {
            gatherCycleInfo(); //populate the arraylist from file

            //create new rank from the arraylist we collected, and pass in settings user chose
            getMyLocation();
            Rank newRank = new Rank(list, getSettings(), myLat, myLong);
            String[] newPaths = newRank.getPaths(); //extract paths of relevant pictures

            Log.i(TAG, "this is path0: " + newPaths[0]); //test to see first path

            Intent cycleIntent = new Intent(this, BuildDisplayCycle.class);
            cycleIntent.setAction(ACTION_RERANK_BUILD); //build new display cycle
            cycleIntent.putExtra("new_cycle", newPaths); //send new string array

            stopService(intent);
        }
    }

    public void getMyLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: REQUEST PERMISSIONS IF NOT SET
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //ActivityCompat.requestPermissions(MainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.i("GetMyLocation", "My Long: "+myLong+", My Lat: " + myLat);
        this.myLat = Double.toString(latitude);
        this.myLong = Double.toString(longitude);
    }

    /*
    this method extracts relevant information (karma released, date, time, location, path etc)
    and creates an arraylist that is then used to create a new display cycle rank
     */

    public void gatherCycleInfo(){
        ArrayList<Photo> pictures = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA}; //which columns we will get (all in this case)
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
            Log.i(TAG, "name, cr.count "+cr.getColumnName(0)+cr.getCount());

            cr.moveToFirst();

            int[] columns = {cr.getColumnIndex(MediaStore.MediaColumns.DATA),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE),
                    cr.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)};

            while(cr.moveToNext()) { //go through all the images
                /*String released = cr.getString(description);
                if(released == "released") continue; //read release from image description
                */

                Photo photo = new Photo(cr.getString(columns[0]), cr.getString(columns[0]), cr.getString(columns[0]),
                        cr.getString(columns[0]),cr.getString(columns[0]));

                pictures.add(photo);

                Log.i(TAG, "added new photo object to list");
            }
        }

        if (cr != null) {
            cr.close();
        }
        this.list = pictures;
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
