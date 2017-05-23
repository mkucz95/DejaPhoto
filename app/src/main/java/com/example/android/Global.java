package com.example.android;

import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Justin on 5/15/17.
 *
 * Used to keep track of various information for our programme
 */

public class Global {
    public static ArrayList<Photo> displayCycle = new ArrayList<>();

    public static int head = 0;

    public static String[] wholeTableProjection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};

    public static boolean dejaVuSetting = true, locationSetting = false,
            daySetting = false, timeSetting = false, karmaSetting = false,
            shareSetting = false, displayUser = false, displayFriend = false;

   //keeps track of alarms for the widget button when karma/release pressed
    public static boolean undoReleaseOn = false; //whether these alarms are on
    public static boolean undoKarmaOn = false;
    public static AutoWallpaperChange autoWallpaperChange;
    public static long changeInterval = 5000; //default is 5
    static Timer timer = new Timer();

    public static boolean[] getSettings(){
        boolean[] settings = {dejaVuSetting, locationSetting, daySetting, timeSetting, karmaSetting};
        return settings;
    }

    public static int currIndex = 0;

    public static String karmaPath;
    public static String releasePath;
}
