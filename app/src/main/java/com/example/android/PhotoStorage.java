package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

/**
 * Created by mkucz on 5/29/2017.
 * <p>
 * code from https://firebase.google.com/docs/storage/android/oad-files
 */

public class PhotoStorage implements IDataElement {
    String imagePath;
    static String name;
    StorageReference storageReference;
    Uri fileUri;
    static boolean uploaded = false;
    static boolean downloaded = false;
    static boolean removed = false;
    private Bitmap bitmap;
    Context context;

    private static final String TAG = "PhotoStorage";

    public PhotoStorage() {//default constructor
    }

    public PhotoStorage(String path, StorageReference reference) {

        this.imagePath = tempPath(path);
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
                PhotoStorage.uploadPath(taskSnapshot);
            }
        });
        Log.d(TAG, "task: " + uploadTask.isComplete());
    }

    private static void uploadPath(UploadTask.TaskSnapshot taskSnapshot) {

        Log.d(TAG, "uploadPath: " + taskSnapshot);

        @SuppressWarnings("VisibleForTests")
        String name = taskSnapshot.getMetadata().getName().replace(".", ",");

        Log.d(TAG, "name of file: " + name);


        Global.currUser.getRef().getRoot().child("photos").child(Global.currUser.email).child(name).setValue(true);
        Global.currUser.getRef().getRoot().child("photos").child(Global.currUser.email).child(name).child("karma").setValue(0);
        Global.currUser.getRef().getRoot().child("photos").child(Global.currUser.email).child(name).child("location").setValue("");
    }

    @Override
    public DatabaseReference getRef() {
        return null;
    }

    //reference for single image, target path is folder to save into
    public static void downloadImage(StorageReference reference, String targetPath, String fileName) {
        Log.d(TAG, "downloading: " + reference);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), targetPath);

        final StorageReference imageRef = reference;

        final File localFile = new File(folder, fileName);
        if (!folder.exists()) {
            Log.i(TAG, "Folder doesn't exist, creating it...");
            boolean rv = folder.mkdir();
            Log.i(TAG, "Folder creation " + (rv ? "success" : "failed"));
        } else {
            Log.i(TAG, "Folder already exists.");
        }

        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "download Success");
                Log.d(TAG, "Path" + localFile.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "download Failure");
            }
        });

        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(localFile);
        mediaScan.setData(contentUri);
        Global.context.getApplicationContext().sendBroadcast(mediaScan);
    }

    //return storage reference
    public static StorageReference getStorageRef(String userEmail) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        Log.d(TAG, "getStorageRef" + userEmail + " ------- " + storageReference);

        return storageReference.child(userEmail);
    }

    public static boolean dirExists(String directory) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + directory);
        return folder.exists();
    }

    public static void setDatabaseListener(final DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get map of users in datasnapshot
                Global.photoSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle database error
            }
        });
    }

    private String tempPath(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);

        if (path.contains("DejaCopy")) {
            Log.i(TAG, path + " contains 'DejaCopy'");
            path = "/storage/emulated/0/" + filename;
            Log.i(TAG, "New path: " + path);
        } else {
            Log.i(TAG, "New path not needed");
        }

        return path;
    }

}
