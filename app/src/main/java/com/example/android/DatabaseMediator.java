package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
    private static final String TARGET = "DejaPhotoFriends";
    private final String TAG = "DatabaseMediator";

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
               Log.i(TAG, "action_sync");
            }

            else if(ACTION_UPDATE_SHARE.equals(action)){
               //clean our images from realtime storage
               DatabaseReference reference = Global.currUser.userPhotosRef();
               if(reference != null) reference.removeValue();
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
        Log.i(TAG, "downloadFriends");

        ArrayList<String> friendEmails = Friends.getFriends(Global.currUser.email);

        for(int i = 0; i<friendEmails.size(); i++) {
                for(DataSnapshot snapshot: Global.photoSnapshot.getChildren()){
                        if(snapshot.getKey().equals(friendEmails.get(i))) {
                            Log.i(TAG, "snapshot: " + snapshot +" matches "+ friendEmails.get(i));
                            manageDownload(snapshot, friendEmails.get(i));
                        }
            }
        }
    }

    //manage calling the downloads for each image of each user
    public void manageDownload(DataSnapshot snapshot, String user){
        Log.i(TAG, "manage download - user: "+user + "--- snapshot: "+snapshot);
        for(DataSnapshot image: snapshot.getChildren()){
            String fileName = image.getKey().replace(",", ".");
            StorageReference storageReference = PhotoStorage.getStorageRef(user).child(fileName);

            PhotoStorage.downloadImage(storageReference, TARGET);
        }
    }
}
