package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.util.Locale;

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
                Log.i(TAG, "Building cycle from MEDIA...");

                //call sqlite traverser
                SQLiteHelper helper = new SQLiteHelper();
                helper.iterateAllMedia(Global.mediaUri, Global.wholeTableProjection, this);
                Log.i(TAG, "Reranking... 1st Build");

                Geocoder gc = new Geocoder(this.getApplicationContext(), Locale.getDefault());//Locale.getDefault()follow the system's language
                Log.i("widgetProv", "DisplayCycle size: " + Global.displayCycle.size());
                for (Photo p : Global.displayCycle) {
                    PhotoLocation locName = new PhotoLocation(p.getPath(), gc, false);
                    Log.i("widgetProv", p.getPath() + ": " + locName);
                }

                Log.i("widgetProv", "6666666666666666");

                for (Photo p : Global.displayCycle) {
                    Log.i("widgetProv", "location: " + p.photoLocationString);
                }
                //after build from file, apply rank settings (released/karma)

                Intent rerankIntent = new Intent(this.getApplicationContext(), Rerank.class);
                startService(rerankIntent);

                Log.i("Timers", "Starting timer from BuildDisplayCycle");



            }
            Log.i(TAG, "Stopping service");
            stopService(intent);
        }
    }
}