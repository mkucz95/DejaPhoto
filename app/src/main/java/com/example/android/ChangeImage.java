package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

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

        int counterInt=0;
        int currHead = 0;
        currHead= headPref.getInt("head", currHead);
        counterInt = counterPref.getInt("counter", counterInt );


        System.out.println("THIS IS HEAD VAL: "+ currHead);
        System.out.println("THIS IS COUNTER VAL: "+ counterInt);

        if(counterInt==0){ //there are no images in the list
            return -1;
        }

        //change the head based on which button was pressed
        if(direction.equalsIgnoreCase(PREV_PIC) && currHead == 0) currHead=counterInt;
        else if(direction.equalsIgnoreCase(PREV_PIC) && currHead != 0) currHead--;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead == counterInt) currHead = 0;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead != counterInt) currHead++;

        int newHead = currHead;
        SharedPreferences.Editor editor = headPref.edit();
        editor.putInt("head", newHead); //add the new head as a number to the shared pref
        editor.apply();

        return newHead;
    }

    private void changeImgToDisplay(int newHead){//changes the image by calling wallpaper service
        //send new intent to the wallpaper changer intent service
        //includes file path
        String newPath;
        if(newHead>0) {
            String head = Integer.toString(newHead);
            SharedPreferences sharedPreferences = getSharedPreferences("head", MODE_PRIVATE);
            newPath = sharedPreferences.getString(head, "");
            System.out.println("THIS SHOULD BE SYSTEM PATH" + newPath);
        } else{
            newPath = "DEFAULTPICTURE";
        }

        Intent wallpaperIntent = new Intent(this, WallpaperChanger.class);
        wallpaperIntent.setAction(Intent.ACTION_SEND);
        wallpaperIntent.putExtra("image_path", newPath);
        wallpaperIntent.setType("text/plain");

        startService(wallpaperIntent); //change the wallpaper
    }
}
