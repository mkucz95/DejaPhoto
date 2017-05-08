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
public class SaveDisplayCycle extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String SAVE_SHAREDPREF = "SAVE_SHAREDPREF";

    // TODO: Rename parameters
    private static final String PIC_PATH = "pic_path";

    public SaveDisplayCycle() {
        super("SaveDisplayCycle");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (SAVE_SHAREDPREF.equals(action)) {
                final String picPath = intent.getStringExtra(PIC_PATH);
                int counter = getNextImgCounter(); //increments counter
                handleSharedPref(picPath, counter);
            }

            stopService(intent); //stop this intent service
        }
    }

    private void handleSharedPref(String picPath, int counter) {
        /*
        * we need to keep track of a coutner so that we can put the images to a new location in the
        * shared preferences. a different number corresponds to each uri
         */
        String counterString = Integer.toString(counter);

        //add the key-value pair of picPath/counter to shared preferences
        SharedPreferences displayCyclePreferences = getSharedPreferences("display_cycle", MODE_PRIVATE);
        //name of the preference is display cycle
        SharedPreferences.Editor displayCycleEditor = displayCyclePreferences.edit();


        //save the coutner as a key string (will be searched by this string
        //the value of the pair is the absolute path to the image
        displayCycleEditor.putString(counterString, picPath);
        displayCycleEditor.apply();
    }

    public int getNextImgCounter(){
        int currImgCount=0;
        SharedPreferences counterPreferences = getSharedPreferences("counter", MODE_PRIVATE);
        currImgCount = counterPreferences.getInt("counter", currImgCount); //get current level of counter

        int nextImgCount = ++currImgCount;

        //save the new number of images into shared preferences
        SharedPreferences.Editor counterEditor = counterPreferences.edit();
        counterEditor.putInt("counter", nextImgCount);
        counterEditor.apply();

        return nextImgCount;
    }
}
