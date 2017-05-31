package com.example.android;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Created by mkucz on 5/29/2017.
 */

public class PhotoStorage implements IDataElement {
    String imagePath;
    StorageReference storageReference;
    Uri fileUri;
   static boolean uploaded;
    static boolean downloaded = false;
    static boolean removed = false;

    private static final String TAG = "PhotoStorage";

    public PhotoStorage() {//default constructor
    }

    public PhotoStorage(String path, StorageReference reference){
        this.imagePath = path;
        this.storageReference = reference;
        this.fileUri = Uri.fromFile(new File(imagePath));
    }

    @Override
    public boolean checkExist(String check) {
        return false;
    }

    @Override
    public boolean addElement() { //this method uploads the element to specified path in database
        UploadTask uploadTask = storageReference.putFile(fileUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                uploaded = false;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploaded = true;
            }
        });
        return uploaded;
    }

    @Override
    public DatabaseReference getRef(String[] info) {
        return null;
    }

    public static boolean downloadImages(StorageReference reference, String targetPath){
        try {
         File  localFile = File.createTempFile(targetPath, "jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    downloaded = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    downloaded=false;
                }
            });
       } catch (IOException e){
           Log.e(TAG, "directory not found--- downloadImages");
       }
       return downloaded;
    }

    //remove all pictures at location
    public static boolean remove(StorageReference reference) {
        reference.delete();
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                removed = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                removed = false;
            }
        });
        return removed;
    }

}
