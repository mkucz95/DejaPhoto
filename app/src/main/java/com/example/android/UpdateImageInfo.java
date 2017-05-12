package com.example.android;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * A service that is called when karma is increased, picture released or user moves further
 * It goes into the MediaStore sqlite database and then changes the description field of each image
 * to add whether the image has karma or is 'released'
 */
public class UpdateImageInfo extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_KARMA = "com.example.android.action.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.action.RELEASE";
    public static final String TAG = "UpdateImageInfo";

    public UpdateImageInfo() {
        super("UpdateImageInfo");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String path = intent.getExtras().toString();

            if (ACTION_KARMA.equals(action)) {
                modifyImage(path, "karma");
            } else if (ACTION_RELEASE.equals(action)) {
                modifyImage(path, "released");
            }

            stopService(intent);
        }
    }

    private void modifyImage(String path, String infoToAdd){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DESCRIPTION}; //which columns we will get (all in this case)
        Cursor cr = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */
        Log.i(TAG, "modifyImage called with string: "+infoToAdd);


        if(null==cr) {
            System.out.println("ERROR null==cr in modifyImage");
        }else if( cr.getCount()<1) {
            System.out.println("ERROR no image to modify in modifyImage");
        } else { //handle returned data
            cr.moveToFirst();
            int pathIndex = cr.getColumnIndex(MediaStore.MediaColumns.DATA);
            int description = cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION);

            Log.i(TAG, "looking for image");


            while(cr.moveToNext()) { //go through all the images
                String uripath = cr.getString(pathIndex);  //get the path/date
                Log.i(TAG, "looking for image: "+ path+"    ----    image in this row: "+ uripath);

                if(uripath.equals(path)){
                    ContentValues newUserValue = new ContentValues();

                    // Defines a variable to contain the number of updated rows
                    int rowsUpdated = 1;
                    String currString = cr.getString(description);
                    String newDescription = currString+","+infoToAdd;
                    newUserValue.put(MediaStore.Images.ImageColumns.DESCRIPTION, newDescription);

                    //get uri of image we are trying to edit
                    Uri currUri= ContentUris
                            .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    cr.getInt(cr.getColumnIndex(MediaStore.Images.ImageColumns._ID)));

                  int numUpdated =  getContentResolver().update(currUri, newUserValue,
                           MediaStore.Images.Media._ID + "= ?", null);

                    Log.i(TAG, "updated: "+ numUpdated+" rows");
                }
            }
        }

        if (cr != null) {
            cr.close();
        }
    }
}
