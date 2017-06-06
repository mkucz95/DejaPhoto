package com.example.android;

import android.util.Log;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by mkucz on 6/2/2017.
 * the DatabaseSync object is called when the timer finishes counting down
 */

public class DatabaseSync extends TimerTask {
    private final String TAG = "DatabaseSync";

    @Override
    public void run() {
        Log.i(TAG, "Begin Sync, currUser: "+Global.currUser);

        if (Global.currUser != null) {
            Log.i(TAG, "uploading images");

            StorageReference storageReference;

            PhotoStorage photoStorage;

            storageReference = PhotoStorage.getStorageRef(Global.currUser.email);

            Log.d("storageReference", "value" + storageReference);

            for (String photo : Global.uploadImageQueue) {
                photoStorage = new PhotoStorage(photo, storageReference);
                photoStorage.addElement();
            }

            if (Global.shareSetting) {  //if sharing is on upload images
                for (String photo : Global.uploadImageQueue) {
                    photoStorage = new PhotoStorage(photo, storageReference);
                    photoStorage.addElement();
                }
            }
            Log.i(TAG, "downloading images");
            if (PhotoStorage.dirExists("DejaPhotoFriends")) {
                ArrayList<String> friends = Friends.getFriends(Global.currUser.email);
                for (String friend : friends) {

                    PhotoStorage.downloadImages(PhotoStorage.getStorageRef(friend)
                            , "sdcard/DejaPhotoFriends");
                }
            }

            if (Global.displayFriend) { //if we want to see our own
                Log.i(TAG, "downloading images");
                if (PhotoStorage.dirExists("DejaPhotoFriends")) {
                    ArrayList<String> friends = Friends.getFriends(Global.currUser.email);
                    for (String friend : friends) {

                        PhotoStorage.downloadImages(PhotoStorage.getStorageRef(friend)
                                , "sdcard/DejaPhotoFriends");
                    }
                }
            }
        }
    }
}

