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

    //gs://dejaphoto-33.appspot.com/hlcphantom%40gmail%2Ccom/ucsd.jpg
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
    public boolean addElement() { //this method uploads the element to specified path in database
        Log.i(TAG, "addElement");

        Log.d(TAG, "bitmap: " + this.bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Log.d(TAG, "data" + data[0]);
        Log.d(TAG, "StorageRef" + "99999" + storageReference.toString());

        final StorageReference imageRef = getStorageRef(name);
        UploadTask uploadTask = imageRef.putBytes(data);
        Log.d(TAG, "newRef" + imageRef);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploaded = false;
                Log.d(TAG, "??????????");
                Log.d(TAG, "Exception " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploaded = true;
            }
        });

        /*UploadTask uploadTask = storageReference.putFile(fileUri);

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
        Log.d(TAG, "getStorageRef" + userEmail+ " ------- " + storageReference);

        return storageReference;
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
