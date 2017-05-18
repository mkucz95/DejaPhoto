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
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class TrackerService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    double initLatitude;
    double initLongitude;
    int initTime;
    Location initLocation;
    int THREAD_PRIORITY_BACKGROUND = 10;


    private static final double LOCATION_RERANK = 304.8; // 1000 feet in meters
    private static final long TIME_RERANK = 2; // 2 hours


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        initLocation = locationManager.getLastKnownLocation(provider);


        this.initTime = (int) ((initLocation.getTime() / (1000*60*60)) % 24);

        HandlerThread thread = new HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Log.i("TrackerService", "onCreate");

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i("TrackerService", "onStartCommand");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        initLocation(locationManager);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("TrackerService", "Destroying");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public void initLocation(LocationManager locationManager){

        Log.i("TrackerService", "initLocation");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.i("TrackerService", "Getting Location...");

        // Initial Location
        initLocation = locationManager.getLastKnownLocation(provider);
        if(initLocation != null) {
            initLatitude = initLocation.getLatitude();
            initLongitude = initLocation.getLongitude();
        }
        else{
            Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
            onDestroy();
        }




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
        Log.i("TrackerService", "requesting location...");

        locationManager.requestLocationUpdates(provider, 2000, 1000, locationListener);

        Log.i("TrackerService", "bottom of initLocation");

    }

    public void updateLocation(Location location) {
        double currLatitude;
        double currLongitute;
        int currTime;
        Log.i("TrackerService", "in updateLocation");


        currTime = (int) ((location.getTime() / (1000*60*60)) % 24);

        if(location != null) {
            currLatitude = location.getLatitude();
            currLongitute = location.getLongitude();
        }

        // Check if out of location boundary for every update
        if(location.distanceTo(initLocation) > LOCATION_RERANK) {
            Log.i("TrackerService", "===========================New Location Detected: " + location);
            initLocation = location;
            // call rerank
            Log.i("TrackerService", "calling Rerank");
            this.initTime = currTime;
            Intent intent = new Intent(this.getApplicationContext(), Rerank.class);
            startService(intent);
        }

        // Check if out of time boundary for every update
       if((currTime - initTime) > TIME_RERANK) {
            initLocation = location;
            this.initTime = currTime;
            // call rerank
            Intent intent = new Intent(this.getApplicationContext(), Rerank.class);
            startService(intent);
        }
    }

}

