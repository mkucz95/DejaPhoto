package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by mkucz on 6/2/2017.
 * the DatabaseSync object is called when the timer finishes counting down
 */

public class DatabaseSync extends TimerTask {
    private final String TAG = "DatabaseSync";
    private static final String TARGET = "DejaPhotoFriends";

    @Override
    public void run() {
        Log.i(TAG, "Begin Sync, currUser: "+ Global.currUser);

        if (Global.currUser != null) {
            if (Global.shareSetting) {  //if sharing is on upload images
                Log.i(TAG, "uploading images");
                uploadQueue();
                uploadMetaData();
            }

            if (Global.displayFriend) { //if we want to see our own
                Log.i(TAG, "downloading images");

                downloadFriends();
                    }
            }
        }

    public void uploadQueue(){
        Log.i(TAG, "uploading queue");
        for(int i = 0; i < Global.uploadImageQueue.size(); i++ ) {
            PhotoStorage newPhoto =
                    new PhotoStorage(Global.uploadImageQueue.get(i),
                            PhotoStorage.getStorageRef(Global.currUser.email));
            newPhoto.addElement();
        }
    }

    public void uploadMetaData(){
        for(int i = 0; i < Global.uploadMetaData.size(); i++ ) {
            //TODO iteration 2
        }
    }

    public void downloadFriends(){
        //delete friends folder so that we can get new copy from database
        File friendsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), TARGET);
        if(friendsFolder.exists()) friendsFolder.delete();


        ArrayList<String> friendEmails = Friends.getFriends(Global.currUser.email);

        for(int i = 0; i<friendEmails.size(); i++) {
            for(DataSnapshot snapshot: Global.photoSnapshot.getChildren()){
                if(snapshot.getKey().equals(friendEmails.get(i))) {
                    Log.i(TAG, "snapshot: "+snapshot.getKey());
                    manageDownload(snapshot, friendEmails.get(i));
                }
            }
        }
    }

    //manage calling the downloads for each image of each user
    public void manageDownload(DataSnapshot snapshot, String user){
        for(DataSnapshot image: snapshot.getChildren()){

            String fileName = image.getKey().replace(",", ".");

            Log.i(TAG, "filename downloading: "+fileName);

            StorageReference storageReference = PhotoStorage.getStorageRef(user).child(fileName);

            PhotoStorage.downloadImage(storageReference, TARGET, fileName);
        }
    }

}

