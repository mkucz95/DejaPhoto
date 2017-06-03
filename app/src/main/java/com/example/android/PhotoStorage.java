package com.example.android;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by mkucz on 5/29/2017.
 *
 * code from https://firebase.google.com/docs/storage/android/upload-files
 */

public class PhotoStorage implements IDataElement {
    String imagePath;
    StorageReference storageReference;
    Uri fileUri;
   static boolean uploaded;
    static boolean downloaded = false;
    static boolean removed = false;
    static Bitmap bitmap;

    private static final String TAG = "PhotoStorage";

    public PhotoStorage() {//default constructor
    }

    public PhotoStorage(String path, StorageReference reference){
        this.imagePath = path;
        this.storageReference = reference;
        this.fileUri = Uri.fromFile(new File(imagePath));
        this.bitmap = FileManager.resizeImage(imagePath);
    }

    @Override
    public boolean checkExist(String check) {
        return false;
    }

    @Override
    public boolean addElement() { //this method uploads the element to specified path in database
        Log.i(TAG, "addElement");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploaded = false;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploaded = true;
            }
        });

      /*  UploadTask uploadTask = storageReference.putFile(fileUri);

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
        });*/
        return uploaded;
    }

    @Override
    public DatabaseReference getRef() { return null; }

    public static boolean downloadImages(StorageReference reference, String targetPath){
        Log.i(TAG, "downloadImages from: "+reference);

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
        Log.i(TAG, "remove : "+reference);
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

    //return storage reference
    public static StorageReference getStorageRef(String userEmail) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        return storageReference.child("photos").child(userEmail);
    }

 /*   public static void uploadImages(String flag){
        //implement to upload
        if(flag.equals("all")){
            //for each image that needs to be uploaded call add element
        }
        else if (flag.equals("notAll")){
            //if we add 2 new pictures, only upload those two
        }
    }*/

    public static boolean dirExists(String directory){
        File folder = new File(Environment.getExternalStorageDirectory()+"/"+directory);
        return folder.exists();
    }

}
