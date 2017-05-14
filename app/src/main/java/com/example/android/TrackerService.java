package com.example.android;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class TrackerService extends Service {

    private final Context context;

    // The location boundary we need to rerank
    private static final double LOCATION_RERANK = 304.8; // 1000 feet
    private static final long TIME_RERANK = 2; // 2 hours

    Intent intent = new Intent(this, Rerank.class);

    public TrackerService(Context context) {
        this.context = context;
    }

    // Tracker thread to separate from main thread
    final class TrackerThread implements Runnable {
        int startId;

        double initLatitude;
        double initLongitude;
        int initTime;

        Location initLocation;

        public TrackerThread(int startId) {
            this.startId = startId;
        }

        @Override
        public void run() {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            String provider = locationManager.getBestProvider(criteria, true);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            // Initial Location
            initLocation = locationManager.getLastKnownLocation(provider);
            initLatitude = initLocation.getLatitude();
            initLongitude = initLocation.getLongitude();
            initTime = (int) ((initLocation.getTime() / (1000*60*60)) % 24);

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                   if(location != null) {
                       updateLocation(location);
                   }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
        }

        public void updateLocation(Location location) {
            double currLatitude;
            double currLongitute;
            int currTime;

            currTime = (int) ((location.getTime() / (1000*60*60)) % 24);

            if(location != null) {
                currLatitude = location.getLatitude();
                currLongitute = location.getLongitude();
            }

            // Check if out of location boundary for every update
            if(location.distanceTo(initLocation) > LOCATION_RERANK) {
                initLocation = location;
                // call rerank
                startService(intent);
            }

            // Check if out of time boundary for every update
            if((currTime - initTime) > TIME_RERANK) {
                initLocation = location;
                // call rerank
                startService(intent);
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(TrackerService.this, "Tracking started", Toast.LENGTH_SHORT).show();
        Thread thread = new Thread(new TrackerThread(startId));
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
