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
    private static final String ACTION_NEW = "com.example.android.NEW";

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

            boolean[] settings = getSettings();

            boolean isLocaOn = settings[0], isTimeOn = settings[1], isWeekOn = settings[2],
                    isKarma =  settings[3];


            getMyLocation();
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ got my location");


            Rank newRank = new Rank(getSettings(), myLat, myLong, isTimeOn, isLocaOn, isWeekOn, isKarma);
            Log.i(TAG, "++++++++++++++++++++++++++++++++++++++ New Rank Created");

            Global.head = 0;
            Intent imageIntent = new Intent(this, ChangeImage.class);
            imageIntent.setAction(ACTION_NEW); //display new picture

            startService(imageIntent);
            stopService(intent);
        }
    }

    /*
    returns users current location to be used in ranking
     */

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //nothing in here needed wtf
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
    this function accessed shared preferences to see what settings the user has selected for their
    ranking, and returns that in a boolean array, default is true for all settings!
     */
    public boolean[] getSettings(){
      return Global.getSettings();
    }

}
