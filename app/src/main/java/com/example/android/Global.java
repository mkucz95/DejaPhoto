package com.example.android;

import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Justin on 5/15/17.
 */

public class Global {
    public static ArrayList<Photo> displayCycle = new ArrayList<>();

    public static int head = 0;

    public static String[] wholeTableProjection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};

    public static boolean undoReleaseOn = false; //whether these alarms are on
    public static boolean undoKarmaOn = false;

    public int cycleLength(){
        return displayCycle.size();
    }

}
