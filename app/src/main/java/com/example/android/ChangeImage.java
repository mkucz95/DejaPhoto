package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * This intent service changes the image when called according to the passed parameter
 * it also moves the head of the display cycle to hold the number/position of the picture that
 * is displayed.
 * It calls the wallpaper service manager to actually change the wallpaper
 */
public class ChangeImage extends IntentService {
    private static final String ACTION_PREVIOUS = "com.example.android.PREVIOUS";
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    private static final String ACTION_NEW = "com.example.android.NEW";

    //parameters
    private static final String NEXT_PIC = "next";
    private static final String PREV_PIC = "previous";

    public ChangeImage() {
        super("ChangeImage");
    }


    /* Method: onHandleIntent
     * Param: Intent intent
     * Purpose: receives the action message and handle each
     * Return: none
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            int newHead;
            if (ACTION_PREVIOUS.equals(action)) { //move to previous pic
                newHead = moveHead(PREV_PIC);
                changeImgToDisplay(newHead);
            } else if (ACTION_NEXT.equals(action)) { //move to next pic
                newHead = moveHead(NEXT_PIC);
                changeImgToDisplay(newHead);
            } else if (ACTION_NEW.equals(action)) { //move to next pic
                displayFirstImage();
            }
            stopService(intent); //stop this intent service
        }
    }

    /* Method: displayFirstImage
     * Param: none
     * Purpose: check the time and start display
     * Return: none
     */
    private void displayFirstImage(){
        Log.i("Timers", "Displaying First Image");
        if(Global.undoTimer != null)
            Global.stopTimer();

        Global.restartTimer(getApplicationContext());
        int counter = Global.displayCycle.size()-1;
        if(counter!=-1){
            changeImgToDisplay(0);
        }
    }

    /* Method: moveHead
     * Param: String direction
     * Purpose: user a circular linked list to move the pointer to the head
     * Return: int
     */
    private int moveHead(String direction) {
        int counterInt = Global.displayCycle.size() - 1; //last element index
        int currHead = Global.head; //current index

        Log.d("ChangeImage", "currHead: " + currHead);
        Log.d("ChangeImage", "counterInt" + counterInt);

        if (counterInt == -1) { //there are no images in the list
            return -1;
        }

        //change the head based on which button was pressed
        if (direction.equalsIgnoreCase(PREV_PIC) && currHead == 0) {
            Log.i("changeImage", "prev clicked, currHead: 0, newHead: " + counterInt);

            currHead = counterInt;
        } else if (direction.equalsIgnoreCase(PREV_PIC) && currHead != 0) {
            Log.i("changeImage", "prev clicked, currHead: " + currHead + ", newHead: " + (currHead - 1));

            currHead--;
        } else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead == counterInt) {
            Log.i("changeImage", "next clicked, currHead: " + currHead + ", newHead: " + 0);

            currHead = 0;
        } else if (direction.equalsIgnoreCase(NEXT_PIC) && currHead != counterInt) {
            Log.i("changeImage", "prev clicked, currHead: " + currHead + ", newHead: " + (currHead + 1));
            currHead++;
        }
        Global.head = currHead;  //set the head for the next image
        return currHead;
    }

    /* Method: changeImgToDisplay
     * Param: int newHead
     * Purpose: changes the image by calling wallpaper service
     * Return: none
     */
    private void changeImgToDisplay(int newHead) {//changes the image by calling wallpaper service
        //send new intent to the wallpaper changer intent service
        //includes file path
        String newPath;
        Log.i("Timers", "Next Image Called");

        Log.i("ChangeImage", "newHead: " + newHead);
        Log.i("ChangeImage", "arrayList size: " + Global.displayCycle.size());
        Log.i("ChangeImage", "Change Interval: " + Global.changeInterval);

        if (newHead >= 0) {
            Photo photo = Global.displayCycle.get(newHead);
            newPath = photo.getPath();
        } else {
            newPath = "DEFAULTPICTURE";
        }

        Log.d("ChangeImage", "NEW PATH: " + newPath);

        Intent wallpaperIntent = new Intent(this, WallpaperChanger.class);
        wallpaperIntent.setAction(Intent.ACTION_SEND);
        wallpaperIntent.putExtra("image_path", newPath); //send path as extra on the intent
        wallpaperIntent.setType("text/plain");

        startService(wallpaperIntent); //change the wallpaper
    }
}
