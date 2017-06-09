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
 * <p>
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
        for (int i = 0; i < projection.length; i++) {
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

        if (projection.length != Global.wholeTableProjection.length) {
            Log.i(TAG, "wrong projection passed to sqlite helper : Projection Length: " + projection.length);
            return;
        }

        if (null == cr) {
            Log.i(TAG, "ERROR null==cr");
        } else if (cr.getCount() < 1) {
            Log.i(TAG, "NO MEDIA FOUND");
        } else { //handle returned data
            Log.i(TAG, "MEDIA PRESENT");
            cr.moveToFirst();

            do { //go through all the images
                Photo photo;
                if (matchesCases(cr.getString(colIndex[0]), Global.displayFriend, Global.displayUser)) {

                    photo = new Photo(cr.getString(colIndex[0]), cr.getString(colIndex[1]),
                            cr.getString(colIndex[2]), cr.getString(colIndex[3]));

                    String[] customInfo = FileManager.handleCSV(cr.getString(cr.getColumnIndex
                            (MediaStore.Images.ImageColumns.DESCRIPTION)));

                    if (customInfo != null) {
                        handleCustomInfo(photo, customInfo);
                    }

                    paths.add(photo);
                    Log.i(TAG, "" + photo.getPath());
                } else {
                    Log.i(TAG, "Photo not in folders: " + cr.getString(colIndex[0]));
                }
            } while (cr.moveToNext());
        }

        if (cr != null) {
            cr.close();
        }

        Global.displayCycle = paths;
    }

    //check to see if custom info exists and set to photo obj
    private void handleCustomInfo(Photo photo, String[] customInfo) {
        if (customInfo != null) {
            if (customInfo[1] != null) {
                photo.setKarma(Integer.parseInt(customInfo[1]));
            }
            if (customInfo[0] != null) {
                photo.photoLocationString = customInfo[0];
            }
        }
    }

    public String getSingleLine(Uri uri, String[] projection, String path, Context context) {
        String info = "";

        this.cr = context.getContentResolver().query(uri, projection, null, null, null);

        if (null == cr) {
            Log.i(TAG, "ERROR null==cr");
        } else if (cr.getCount() < 1) {
            Log.i(TAG, "NO MEDIA FOUND");
        } else { //handle returned data
            Log.i(TAG, "MEDIA PRESENT");
            cr.moveToFirst();

            do { //go through all the images
                if (path.equals(cr.getString(0))) {
                    info = cr.getString(1);

                    Log.d(TAG, "info recieved: " + info);
                    break;
                }
            } while (cr.moveToNext());
        }

        if (cr != null) {
            cr.close();
        }

        return info;
    }

    /*
    This method puts data in a certain field in sqlite database
     */
    public int storeSQLData(String data, String colToAdd, String path, Context context) {
            String[] selectionArgs = {path};
            String selectionClause = MediaStore.Images.Media.DATA + " = ?";

        Log.i(TAG, "selectionArgs: "+selectionArgs[0]);
        Log.i(TAG, "selectionClause: "+selectionClause);

        ContentValues newUserValue = new ContentValues();
            newUserValue.put(colToAdd, data);

        Log.i(TAG, "contentVals: "+newUserValue);

        //update(@thisUri, with values from ContentValues ...)
            int numUpdated = Global.context.getContentResolver().update(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, newUserValue,
                    selectionClause, selectionArgs);

            Log.i(TAG, "updated: " + numUpdated + " rows");
            return numUpdated;
    }

    public static boolean matchesCases(String string, boolean friends, boolean own) {
        if (own && friends) {
            if (string.contains("DejaPhoto") || string.contains("DejaCopy") || string.contains("DejaFriends"))
                return true;
            else return false;

        } else if (own) {
            if (string.contains("DejaPhoto") || string.contains("DejaCopy"))
                return true;
            else return false;
        } else {
            if (string.contains("DejaFriends"))
                return true;
            else return false;
        }
    }

    public static String getImagePath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);

        cursor.close();

        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Log.i("SQLite Helper", "Get string at: " + cursor.getColumnIndex(MediaStore.Images.Media.DATA) + ": " + path);

        cursor.close();

        return path;
    }


}
