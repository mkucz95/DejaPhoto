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

    public static boolean dejaVuSetting = true, locationSetting = false,
            daySetting = false, timeSetting = false, karmaSetting = false;

    public static int timeInterval = 15;

   //keeps track of alarms for the widget button when karma/release pressed
    public static boolean undoReleaseOn = false; //whether these alarms are on
    public static boolean undoKarmaOn = false;

    public static boolean[] getSettings(){
        boolean[] settings = {dejaVuSetting, locationSetting, daySetting, timeSetting, karmaSetting};
        return settings;
    }
}
