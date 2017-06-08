package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Justin on 5/15/17.
 *
 * This class helps us iterate and read information from the device storage (SQLite)
 */

public class SQLiteHelper {
    /*
    This class helps us use and/or manipulate SQLite data
     */

    Cursor cr;
    private final String TAG = "SQLiteHelper";

    /*
    This method iterates through a specific column given from Uri in Android SQLite and saves each
    data field to an array list in the order that it reads it.
     */

    public void iterateAllMedia(Uri uri, String[] projection, Context context) {
        this.cr = context.getContentResolver().query(uri, projection, null, null, null);
        Log.i(TAG, "In iterateMedia");


        int[] colIndex = new int[projection.length];
        for(int i = 0; i < projection.length; i++){
                 colIndex[i] = cr.getColumnIndex(projection[i]);
        }

        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */

        ArrayList<Photo> paths = new ArrayList<>();
        Log.i(TAG, "Paths arrayList created");

        if(projection.length != 4) {
            Log.i(TAG, "wrong projection passed to sqlite helper : Projection Length: " + projection.length);
            return;
        }

        if(null == cr) {
            Log.i(TAG, "ERROR null==cr");
        }else if( cr.getCount()<1) {
            Log.i(TAG, "NO MEDIA FOUND");
        } else { //handle returned data
            Log.i(TAG, "MEDIA PRESENT");
            cr.moveToFirst();

            do{ //go through all the images
                Photo photo;
                if( matchesCases(cr.getString(colIndex[0]), Global.displayFriend, Global.displayUser) ) {

                    photo = new Photo(cr.getString(colIndex[0]), cr.getString(colIndex[1]),
                            cr.getString(colIndex[2]), cr.getString(colIndex[3]));

                    paths.add(photo);
                    Log.i(TAG, ""+photo.getPath());
                }
                else{
                    Log.i(TAG, "Photo not in folders: "+cr.getString(colIndex[0]));

                }


            } while(cr.moveToNext());
        }

        if (cr != null) {
            cr.close();
        }

        Global.displayCycle = paths;
    }


    /*
    This method puts data in a certain field in sqlite database
     */

    public int storeSQLData(String data, String colToAdd, String path, Context context){

        if(null==cr) {
            Log.e(TAG, "ERROR null=cr in write");
        }else if( cr.getCount()<1) {
            Log.e(TAG, "ERROR no elements in table");
        } else { //handle returned data
            cr.moveToFirst();
            int pathIndex = cr.getColumnIndex(MediaStore.MediaColumns.DATA);

            Log.i(TAG, "looking for image");


                    String[] selectionArgs = {path};
                    String selectionClause =  MediaStore.Images.Media.DATA + " = ?";

                    ContentValues newUserValue = new ContentValues();
                    newUserValue.put(colToAdd, data);

                    //update(@thisUri, with values from ContentValues ...)
                    int numUpdated  = context.getContentResolver().update(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, newUserValue,
                            selectionClause, selectionArgs);

                    Log.i(TAG, "updated: " + numUpdated + " rows");
            return numUpdated;
                }
        if (cr != null) {
            cr.close();
        }
        return -1;
    }

    public static boolean matchesCases(String string, boolean friends, boolean own){
        if(own && friends){
            if(string.contains("DejaPhoto") || string.contains("DejaCopy") || string.contains("DejaFriends"))
                return true;
            else return false;

        }else if(own){
            if(string.contains("DejaPhoto") || string.contains("DejaCopy"))
                return true;
            else return false;
        }else {
            if(string.contains("DejaFriends"))
                return true;
            else return false;
        }
    }

}
