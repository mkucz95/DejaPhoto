package com.example.android;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Justin on 5/15/17.
 * <p>
 * Used to keep track of various information for our programme
 */

public class Global {
    //DISPLAY CYCLE
    public static ArrayList<Photo> displayCycle = new ArrayList<>();
    public static int head = 0;

    //SQLITE HELPERS
    public static String[] wholeTableProjection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE, MediaStore.Images.ImageColumns.DESCRIPTION};

    public static String[] descriptionProjection = {MediaStore.Images.Media.DATA,
            MediaStore.Images.ImageColumns.DESCRIPTION};

    public static Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    public static boolean dejaVuSetting = true, locationSetting = false,
            daySetting = false, timeSetting = false, karmaSetting = false;

    //keeps track of alarms for the widget button when karma/release pressed
    //UNDO BUTTON PRESS
    public static boolean undoReleaseOn = false; //whether these alarms are on
    public static boolean undoKarmaOn = false;
    public static AutoWallpaperChangeTask autoWallpaperChange;
    public static long changeInterval = 10000; //default is 10 seconds -- changed for testing
    static Timer undoTimer = new Timer();

    public static int karmaNum = 0;

    //RANK SETTINGS
    public static boolean[] getSettings() {
        boolean[] settings = {dejaVuSetting, locationSetting, daySetting, timeSetting, karmaSetting};
        return settings;
    }

    public static int windowWidth = 0;
    public static int windowHeight = 0;

    public static int currIndex = 0;

    public static String karmaPath;
    public static String releasePath;

    //DATABASE SETTINGS
    public static int syncInterval = 300;
    public static TimerTask syncTimerTask;
    static Timer syncTimer = new Timer();
    public static boolean shareSetting = true, displayUser = true, displayFriend = true;
    public static boolean setDisplay = false;

    //CURRENT USER DATA
    public static DataSnapshot userSnapshot;
    public static DataSnapshot photoSnapshot;
    public static User currUser;

    //CAMERA
    public static int imageNumber = 0;

    public static Context context;

    public static ArrayList<String> uploadImageQueue = new ArrayList<>();
    public static ArrayList<String> uploadMetaData = new ArrayList<>();

    public static String friendFolderPath = ""; //TODO

    //Method to check if string is whitespace
    public static boolean isBlank(String str) {
        Log.i("isBlank", "STRING: '" + str + "'");
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static void restartTimer(Context context){
        Log.i("Timers", "Restarting timer with interval " + Global.changeInterval);
        Global.autoWallpaperChange = new AutoWallpaperChangeTask(context);
        Global.undoTimer = new Timer();
        Global.undoTimer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);
    }

    public static void stopTimer(Context context){
        Log.i("Timers", "Stopping Timers");
        Global.autoWallpaperChange.cancel();
        Global.undoTimer.cancel();
        Global.undoTimer.purge();
    }

}
