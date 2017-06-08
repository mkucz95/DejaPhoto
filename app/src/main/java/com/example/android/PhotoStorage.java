package com.example.android;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * Created by mkucz on 5/29/2017.
 *
 * code from https://firebase.google.com/docs/storage/android/upload-files
 */

public class PhotoStorage implements IDataElement {
    String imagePath;
    String name;
    StorageReference storageReference;
    Uri fileUri;
    static boolean uploaded = false;
    static boolean downloaded = false;
    static boolean removed = false;
    private Bitmap bitmap;

    private static final String TAG = "PhotoStorage";

    public PhotoStorage() {//default constructor
    }

    public PhotoStorage(String path, StorageReference reference){
        this.imagePath = path;
        this.storageReference = reference;
        this.fileUri = Uri.fromFile(new File(imagePath));
        this.bitmap = FileManager.getBitmap(imagePath);
        this.name = (new File(imagePath)).getName();
    }

    @Override
    public boolean checkExist(String check) {
        return false;
    }

    @Override
    public void addElement() { //this method uploads the element to specified path in database
        Log.i(TAG, "addElement");

        Log.d(TAG, "bitmap: " + this.bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Log.d(TAG, "data" + data[0]);
        Log.d(TAG, "StorageRef" + storageReference.toString());

        final StorageReference imageRef = storageReference.child(name);

        UploadTask uploadTask = imageRef.putBytes(data);
        Log.d(TAG, "newRef" + imageRef);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, "Exception " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess");
                @SuppressWarnings("VisibleForTests")
                Uri url = taskSnapshot.getDownloadUrl();
            }
        });
        Log.d(TAG, "task: " + uploadTask.isComplete());
    }

    @Override
    public DatabaseReference getRef() { return null; }

    public static void downloadImages(StorageReference reference, String targetPath){
        Log.i(TAG, "downloadImages from: "+reference);

        //iterator through reference's children
        downloadSingleImage(reference, targetPath);
    }

    //reference for single image, target path is folder to save into
    private static void downloadSingleImage(StorageReference reference, String targetPath) {
        Log.d(TAG, "downloading: "+ reference );

        try {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+
                    targetPath);

            File localFile = File.createTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+
                    targetPath, "jpg");

            if (!folder.exists()) {
                Log.i(TAG, "Folder doesn't exist, creating it...");
                boolean rv = folder.mkdir();
                Log.i(TAG, "Folder creation " + ( rv ? "success" : "failed"));
            } else {
                Log.i(TAG, "Folder already exists.");
            }

            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "download Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "download Failure");
                }
            });
       } catch (IOException e){
           Log.e(TAG, "directory not found-- downloadImages");
       }
    }

    //remove all pictures at location
    public static void removeStorage(final StorageReference reference) {
        Log.i(TAG, "remove : "+reference);
        reference.delete();
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "remove success on: "+reference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "remove success on: "+reference);
            }
        });
    }


    //return storage reference
    public static StorageReference getStorageRef(String userEmail) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        Log.d(TAG, "getStorageRef" + userEmail+ " ------- " + storageReference);

        return storageReference.child(userEmail);
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

    public static void testUpload(){
        String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-2.jpg";
    }
}
