package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * this class helps us manage uploads to the database and database syncing when needed
 */

public class DatabaseMediator extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATE_SHARE = "com.example.android.action.UPDATE_SHARE";
    private static final String ACTION_QUEUE = "com.example.android.action.uploadFromQueue";
    private static final String ACTION_SYNC = "com.example.android.action.SYNC";

    public DatabaseMediator() {
        super("DatabaseMediator");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
           if (ACTION_QUEUE.equals(action)) {
                  uploadQueue();
            }
            else if(ACTION_SYNC.equals(action)){
                uploadQueue();
                uploadMetaData();
                downloadFriends();
            }

            else if(ACTION_UPDATE_SHARE.equals(action)){
               //clean our images from storage
               syncSettings();
           }
        }
    }

    public void uploadQueue(){
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
        ArrayList<String> friendEmails = Friends.getFriends(Global.currUser.email);

        for(int i = 0; i<friendEmails.size(); i++) {
            PhotoStorage.downloadImages(PhotoStorage.getStorageRef(Global.currUser.email).child(friendEmails.get(i)),
                    Global.friendFolderPath);
        }
    }

    public void updateOwnPictures(){
        for(int i = 0; i < Global.uploadMetaData.size(); i++ ) {
            //TODO iteration 2
        }
    }

    public void syncSettings(){
        String user = Global.currUser.email;

        //PhotoStorage.removeStorage();
    }

}
