package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 *     This intent service changes the image when called according to the passed parameter
 *     it also moves the head of the display cycle to hold the number/position of the picture that
 *     is displayed.
 *     It calls the wallpaper service manager to actually change the wallpaper
 */
public class ChangeImage extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_PREVIOUS = "com.example.android.PREVIOUS";
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    public static final String ACTION_RELEASE = "com.example.android.RELEASE";

//parameters
    private static final String NEXT_PIC = "next";
    private static final String PREV_PIC = "previous";

    public ChangeImage() {
        super("ChangeImage");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            int newHead;
            if (ACTION_PREVIOUS.equals(action)) { //move to previous pic
                 newHead = moveHead(NEXT_PIC);
                changeImgToDisplay(newHead);
            } else if (ACTION_NEXT.equals(action)) { //move to next pic
                newHead = moveHead(PREV_PIC);
                changeImgToDisplay(newHead);
            }
            else if (ACTION_KARMA.equals(action)) {
                //updateCycle TODO
            }
            else if (ACTION_RELEASE.equals(action)) {
                //updateCycle TODO
           }
            stopService(intent); //stop this intent service
        }
    }

    private int moveHead(String direction){

        SharedPreferences headPref = getSharedPreferences("head", MODE_PRIVATE);
        SharedPreferences counterPref = getSharedPreferences("counter", MODE_PRIVATE);

        int counterInt = 0;
        int currHead = 0;
        currHead= headPref.getInt("head", currHead);
        counterInt = counterPref.getInt("counter", counterInt);


        Log.d("WallpaperChanger", "currHead: "+ currHead);
        Log.d("WallpaperChanger","counterInt"+ counterInt);

        if(counterInt==-1){ //there are no images in the list
            return -1;
        }

        //change the head based on which button was pressed
        if(direction.equalsIgnoreCase(PREV_PIC) && currHead == 0) currHead=counterInt;
        else if(direction.equalsIgnoreCase(PREV_PIC) && currHead != 0) currHead--;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead == counterInt) currHead = 0;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead != counterInt) currHead++;

        int newHead = currHead;
        SharedPreferences.Editor editor = headPref.edit();
        editor.clear();
        editor.putInt("head", newHead); //add the new head as a number to the shared pref
        editor.apply();

        return newHead;
    }

    private void changeImgToDisplay(int newHead){//changes the image by calling wallpaper service
        //send new intent to the wallpaper changer intent service
        //includes file path
        String accessPoint = Integer.toString(newHead);
        String newPath = "";

        if(newHead>=0) {
            SharedPreferences sharedPreferences = getSharedPreferences("display_cycle", MODE_PRIVATE);
            newPath = sharedPreferences.getString(accessPoint, "defaultValue"); //get path from display cycle

            System.out.println("Access point: " + accessPoint);
            Log.d("ChangeImage", "Path Recieved" + newPath);
        } else{
            newPath = "DEFAULTPICTURE";
        }

        Intent wallpaperIntent = new Intent(this, WallpaperChanger.class);

        Log.d("WallpaperChanger", "NEW PATH: " + newPath);
        wallpaperIntent.setAction(Intent.ACTION_SEND);
        wallpaperIntent.putExtra("image_path", newPath); //send path as extra on the intent
        wallpaperIntent.setType("text/plain");

        startService(wallpaperIntent); //change the wallpaper
    }
}
