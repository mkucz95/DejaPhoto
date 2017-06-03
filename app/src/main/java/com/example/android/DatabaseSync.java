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
    //private final String TAG = "DatabaseSync.java";

    @Override
    public void run() {
        /*Log.i(TAG, "uploading images");

        StorageReference storageReference;

        PhotoStorage photoStorage;

<<<<<<< HEAD
        storageReference = PhotoStorage.getStorageRef(Global.currUser.email);

        Log.d("storageReference", "value" + storageReference);

        for(Photo photo: Global.uploadImageQueue){
            photoStorage = new PhotoStorage(photo.getPath(), storageReference);
            photoStorage.addElement();
        }
=======
if(Global.shareSetting) {  //if sharing is on
    for (Photo photo : Global.uploadImageQueue) {
        photoStorage = new PhotoStorage(photo.getPath(), storageReference);
        photoStorage.addElement();
    }
}
>>>>>>> ee1e6dbc079d684ae2c87c14508f898b759482bb

        for(Photo photo: Global.uploadMetaData){
            photoStorage = new PhotoStorage(photo.getPath(), storageReference);
            photoStorage.addElement();
        }

<<<<<<< HEAD
        Log.i(TAG, "downloading images");
        if(PhotoStorage.dirExists("DejaPhotoFriends")){
            ArrayList<String> friends = Friends.getFriends(Global.currUser.email);
            for(String friend: friends) {

                PhotoStorage.downloadImages( PhotoStorage.getStorageRef(friend)
                        , "sdcard/DejaPhotoFriends");
            }
        }*/

        /*if(Global.displayFriend) { //if we want to see our own
          Log.i(TAG, "downloading images");
          if (PhotoStorage.dirExists("DejaPhotoFriends")) {
              ArrayList<String> friends = Friends.getFriends(Global.currUser.email);
              for (String friend : friends) {

                  PhotoStorage.downloadImages(PhotoStorage.getStorageRef(friend)
                          , "sdcard/DejaPhotoFriends");
              }
          }
      }*/
    }
}

