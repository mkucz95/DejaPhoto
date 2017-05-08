package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ChangeImage extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String PREVIOUS = "com.example.android.action.PREVIOUS";
    private static final String NEXT = "com.example.android.action.NEXT";
    private static final String KARMA = "com.example.android.action.KARMA";
    public static final String RELEASE = "com.example.android.action.RELEASE";

    // TODO: Rename parameters
    private static final String NEXT_PIC = "next";
    private static final String PREV_PIC = "previous";

    private static final String EXTRA_PARAM2 = "com.example.android.extra.PARAM2";

    public ChangeImage() {
        super("ChangeImage");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            String newHead;
            if (PREVIOUS.equals(action)) {
                 newHead = moveHead(NEXT_PIC);
                changeImgToDisplay(newHead);
            } else if (NEXT.equals(action)) {
                newHead = moveHead(PREV_PIC);
                changeImgToDisplay(newHead);
            }
            else if (KARMA.equals(action)) {
                //updateCycle
            }
            else if (RELEASE.equals(action)) {
                //updateCycle
           }
        }
    }

    private String moveHead(String direction){
        SharedPreferences headPref = getSharedPreferences("head", MODE_PRIVATE);
        String currHead = headPref.getString("head", "");

        SharedPreferences counterPref = getSharedPreferences("counter", MODE_PRIVATE);
        String counterString = counterPref.getString("counter", "");

        int headInt = Integer.parseInt(currHead);
        int counterInt = Integer.parseInt(counterString);

        //change the head based on which button was pressed
        if(direction.equalsIgnoreCase(PREV_PIC) && headInt == 0) headInt=counterInt;
        else if(direction.equalsIgnoreCase(PREV_PIC) && headInt != 0) headInt--;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && headInt == counterInt) headInt = 0;
        else if (direction.equalsIgnoreCase(NEXT_PIC) && headInt != counterInt) headInt++;

        String newHead = Integer.toString(headInt);
        SharedPreferences.Editor editor = headPref.edit();
        editor.putString("head", newHead); //add the new head as a number to the shared pref

        return newHead;
    }

    private void changeImgToDisplay(String newHead){
        //TODO return image path for image at the new head. get the path from the key-val pair
    }
}
