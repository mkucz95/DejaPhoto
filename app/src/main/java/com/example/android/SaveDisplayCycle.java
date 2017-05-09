package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *     this service takes in image paths as strings and saves them to the sharedpreferences
 */
public class SaveDisplayCycle extends IntentService {
//actions
   private static final String ACTION_SAVE_SHAREDPREF = "com.example.android.SAVE_SHAREDPREF";

//parameters
    private static final String PIC_PATH = "pic_path";

    public SaveDisplayCycle() {
        super("SaveDisplayCycle");
    }//start this service

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_SAVE_SHAREDPREF.equals(action)) {
                final String picPath = intent.getStringExtra(PIC_PATH);
                int counter = getNextImgCounter(); //increments counter ie ('0', "C") ("1", ":C/")
                handleSharedPref(picPath, counter);
            }

            stopService(intent); //stop this intent service
        }
    }

    private void handleSharedPref(String picPath, int counter) {
        /*
        * we need to keep track of a counter so that we can put the images to a new location in the
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
        SharedPreferences counterPreferences = getSharedPreferences("counter", MODE_PRIVATE);
        String currImgCount = counterPreferences.getString("counter", ""); //get current level of counter

        int intImgCount = Integer.parseInt(currImgCount);
        int nextImgCount = ++intImgCount;

        //save the new number of images into shared preferences
        SharedPreferences.Editor counterEditor = counterPreferences.edit();
        counterEditor.putString("counter", Integer.toString(nextImgCount));
        counterEditor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("head", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("head", Integer.toString(nextImgCount)); //move head to last element in list

        return nextImgCount;
    }
}
