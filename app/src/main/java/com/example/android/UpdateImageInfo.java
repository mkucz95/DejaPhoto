package com.example.android;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * A service that is called when karma is increased, picture released or user moves further
 * It goes into the MediaStore sqlite database and then changes the description field of each image
 * to add whether the image has karma or is 'released'
 */
public class UpdateImageInfo extends IntentService {
    private static final String ACTION_KARMA = "com.example.android.action.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.action.RELEASE";
    public static final String TAG = "UpdateImageInfo";

    public UpdateImageInfo() {
        super("UpdateImageInfo");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("UpdateInfo", "UpdateCalled");

        if (intent != null) {
            //final String action = intent.getAction();
            final String path = intent.getStringExtra("path");
            final String type = intent.getExtras().getString("type");
            Log.i(TAG, "path: "+path);

            if ("karma".equals(type)) {
                Log.i("UpdateInfo", "KARMA-------");

                modifyImage(path, "karma");
            } else if ("release".equals(type)) {
                Log.i("UpdateInfo", "RELEASE-----------");

                modifyImage(path, "released");
            }
            stopService(intent);
        }
    }

    private void modifyImage(String path, String infoToAdd){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DESCRIPTION, MediaStore.Images.Media._ID}; //which columns we will get (all in this case)
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
            int idLoc = cr.getColumnIndex(MediaStore.Images.Media._ID);

            Log.i(TAG, "looking for image");


           do { //go through all the images
                String uripath = cr.getString(pathIndex);  //get the path/date
                Log.i(TAG, "looking for image: "+ path+"    ----    image in this row: "+ uripath);

                if(uripath.equals(path)){
                   /* if(infoToAdd.equals("released")){
                        //delete here
                        File newFile = new File(path);
                        newFile.delete();
                        Log.i("UpdateInfo", "Deleted: " + path);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));

                    }*/
                    Long id = cr.getLong(idLoc);
                    String[] selectionArgs = {path};

                    Log.i("UpdateInfo", "Selection Args: " + selectionArgs[0]);
                    String selectionClause =  MediaStore.Images.Media.DATA + " = ?";

                    ContentValues newUserValue = new ContentValues();

                    String currString = cr.getString(description);
                    String newDescription = currString + "," + infoToAdd;
                    newUserValue.put(MediaStore.Images.ImageColumns.DESCRIPTION, newDescription);

                    //update(@thisUri, with values from ContentValues ...)
                    int numUpdated  = getContentResolver().update(uri, newUserValue, selectionClause, selectionArgs);
                  
                    Log.i(TAG, "updated: " + numUpdated + " rows");
                }
               if(uripath.equals(path)) break;
            } while(cr.moveToNext());


        }

        if (cr != null) {
            cr.close();
        }
    }
}
