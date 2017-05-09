package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BuildDisplayCycle extends IntentService {
    private static final String BUILD_CYCLE = "com.example.android.action.BUILD";

    public BuildDisplayCycle() {
        super("BuildDisplayCycle");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
           boolean sourceFolder = intent.getExtras().getBoolean("source");

            if (BUILD_CYCLE.equals(action)) {
                buildFromFile(sourceFolder);
            }

            stopService(intent);
        }
    }

    private void buildFromFile(boolean sourceFolder) {
        if (sourceFolder) {
            File dcimDirectory = new File(Environment.getExternalStorageDirectory(), "DCIM"); //get path to DCIM folder
            File cameraDirectory = new File(dcimDirectory.getAbsolutePath() + "/Camera"); //TODO
            Intent intent = new Intent(getApplicationContext(), SaveDisplayCycle.class);
            intent.setAction("SAVE_SHAREDPREF");

            File[] dcimPhotos = cameraDirectory.listFiles();
            if (dcimPhotos != null) { //DCIM contains photos
                for (File currPicture : dcimPhotos) { //add each photo's path to cycle as a node
                    intent.putExtra("pic_path", currPicture.getAbsolutePath()); //todo, maybe have to clear intent every time
                    startService(intent);
                }
            } else {
                intent.putExtra("pic_path", "DEFAULTPICTURE"); //when the folder contains no pictures
                startService(intent);
            }
        } else {
        }
    }
}
