package com.example.android;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * A service that is called when karma is increased, picture released or user moves further
 */
public class UpdateImageInfo extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_KARMA = "com.example.android.action.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.action.RELEASE";

    public UpdateImageInfo() {
        super("UpdateImageInfo");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

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

        if(null==cr) {
            System.out.println("ERROR null==cr in modifyImage");
        }else if( cr.getCount()<1) {
            System.out.println("ERROR no image to modify in modifyImage");
        } else { //handle returned data
            cr.moveToFirst();
            int pathIndex = cr.getColumnIndex(MediaStore.MediaColumns.DATA);
            int description = cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION);

            while(cr.moveToNext()) { //go through all the images
                /*String released = cr.getString(description);
                if(released == "released") continue; //read release from image description
                */
                String uripath = cr.getString(pathIndex);  //get the path/date
                if(uripath == path){
                    //TODO ADD KARMA OR RELEASE TO IMAGE
                    // Defines an object to contain the updated values

                    ContentValues newUserValue = new ContentValues();

                    // Defines selection criteria for the rows you want to update
                    String selectionClause = ""; //UserDictionary.Words.LOCALE +  "LIKE ?";
                    String[] mSelectionArgs = {""}; //{"en_%"};

                    // Defines a variable to contain the number of updated rows
                    int rowsUpdated = 1;

                    /*
                     * Sets the updated value and updates the selected words.
                     */

                    newUserValue.putNull(MediaStore.Images.ImageColumns.DESCRIPTION);

                    rowsUpdated = getContentResolver().update(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,   // the user dictionary content URI
                            newUserValue,                       // the columns to update
                            selectionClause,                    // the column to select on
                            mSelectionArgs                     // the value to compare to
                    );
                }
            }
        }

        if (cr != null) {
            cr.close();
        }
    }
}
