package com.example.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


public class updateLocation extends Activity {


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //In android programming, there are 2 ways to obtain current location: GPS/ network
        //these 2 providers have their own pros and cons
        //'criteria' can auto pick a location provider based on device's circumstance
        Criteria criteria = new Criteria();
        //here we can choose between fine/coarse
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //indicate whether we want the altitude/ bearing(Chin: fang wei) info
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        //indicate how much battery we want to consume
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);

        //need permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        updateWithNewLocation(location);

        //!!!!!!!!!!!!!!!!!!!! The main code, function description:
        //requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener)
        //minDistance: used to control frequency of location updates, if greater than 0 then location provider
        //will send application an update when the location has changed by at least minDistance meters
        //everytime i move 10 meters (from last location saved by platform) i get an update
        //OR at least minTime milliseconds have passed (2000=2s). minTime takes precedence
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);

    }


    private void updateWithNewLocation(Location location) {
        //get lat and long
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
        }
    }


    //monitor change of location, if provider passes in the same location, nothing happens
    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

    }



