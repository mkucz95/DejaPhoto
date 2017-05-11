package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * This intent service creates a new rank object which has the correct order of the pictures
 * that are supposed to be displayed. this is then sent to the build display cycle
 */
public class Rerank extends IntentService {
    private static final String ACTION_RERANK_BUILD = "com.example.android.RERANK_BUILD";

    public Rerank() {
        super("Rerank");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
         Rank newRank = new Rank();
           String[] newPaths =  newRank.getPaths();

            Intent cycleIntent = new Intent(this, BuildDisplayCycle.class);
            cycleIntent.setAction(ACTION_RERANK_BUILD);
            cycleIntent.putExtra("new_cycle", newPaths); //send new string array

            stopService(intent);
        }
    }

}
