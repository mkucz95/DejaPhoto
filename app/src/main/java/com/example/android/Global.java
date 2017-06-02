package com.example.android;

import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;

/**
 * Created by Justin on 5/15/17.
 *
 * Used to keep track of various information for our programme
 */

public class Global {
    //DISPLAY CYCLE
    public static ArrayList<Photo> displayCycle = new ArrayList<>();
    public static int head = 0;

    public static String[] wholeTableProjection = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE};

    public static boolean dejaVuSetting = true, locationSetting = false,
            daySetting = false, timeSetting = false, karmaSetting = false;

   //keeps track of alarms for the widget button when karma/release pressed
    //UNDO BUTTON PRESS
    public static boolean undoReleaseOn = false; //whether these alarms are on
    public static boolean undoKarmaOn = false;
    public static AutoWallpaperChange autoWallpaperChange;
    public static long changeInterval = 5000; //default is 5
    static Timer timer = new Timer();

    //RANK SETTINGS
    public static boolean[] getSettings(){
        boolean[] settings = {dejaVuSetting, locationSetting, daySetting, timeSetting, karmaSetting};
        return settings;
    }

    public static int currIndex = 0;

    public static String karmaPath;
    public static String releasePath;

    //DATABASE SETTINGS
    public static int syncInterval = 300;
    public static boolean shareSetting = true, displayUser = true, displayFriend = true;

    //CURRENT USER DATA
    public static DataSnapshot userSnapshot;
    public static User currUser;


    public static ArrayList<Photo> uploadImageQueue = new ArrayList<>();
    public static ArrayList<String> uploadMetaData = new ArrayList<>();

    public static String friendFolderPath = ""; //TODO


}
