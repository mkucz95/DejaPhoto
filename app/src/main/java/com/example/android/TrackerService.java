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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class TrackerService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    double initLatitude;
    double initLongitude;
    Location initLocation;
    int THREAD_PRIORITY_BACKGROUND = 10;


    private static final double LOCATION_RERANK = 304.8; // 1000 feet
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
        //int currTime;
        Log.i("TrackerService", "in updateLocation");


        //currTime = (int) ((location.getTime() / (1000*60*60)) % 24);

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
            Intent intent = new Intent(this.getApplicationContext(), Rerank.class);
            startService(intent);
        }

        // Check if out of time boundary for every update
       /* if((currTime - initTime) > TIME_RERANK) {
            initLocation = location;
            // call rerank
            startService(intent);
        }*/
    }

}


    /*private final Context context;

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

            Log.i("TrackerService", "In run() method");

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
    }*/

