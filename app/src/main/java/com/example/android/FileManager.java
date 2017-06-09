package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;

public class FileManager {
    private final String TAG = "FileManager";
    Context context;
    File file;

    public FileManager(Context context) {
            this.context = context;
    }


    public void scanSD(File file){
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScan.setData(contentUri);
        context.getApplicationContext().sendBroadcast(mediaScan);
    }

    public void saveFile(Bitmap imageToSave, String folder) {

        //New directory in DCIM/DejaPhoto
        File folderName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folder);

        if (!folderName.exists()) {
            Log.i(TAG, "Folder doesn't exist, creating it...");
            boolean rv = folderName.mkdir();
            Log.i(TAG, "Folder creation " + ( rv ? "success" : "failed"));
        } else {
            Log.i(TAG, "Folder already exists.");
        }

        String captured = "FILENAME-" + MainActivity.n + ".jpg";

        MainActivity.n++;

        File file = new File(folderName, captured);

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.d(TAG, "file" + file.exists());
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanSD(file);

        Log.d(TAG, "+++++++");
    }

    public String getImagePath(Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);


        cursor.close();

        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Log.i(TAG, "Get string at: " + cursor.getColumnIndex(MediaStore.Images.Media.DATA) + ": " + path);

        cursor.close();

        return path;
    }/*
     * Copies a file from the camera roll to specified directory
     */

    public void copyFile(File src, File dst) throws IOException {
        File file = new File(dst + File.separator + src.getName());
        file.createNewFile();
        Log.i("pictureSelect", "Dest file: " + file.getAbsolutePath());

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(file);


        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }


    public void setDisplayCycleData(boolean karma, String path){
        ArrayList<Photo> temp = Global.displayCycle;
        for(int i = 0; i<temp.size(); i++){
            Photo photo = temp.get(i);
            Log.i("setKarma", path + " compare to : " + photo.getPath());
            if(photo.getPath().equals(path)){
                if(karma) {
                    Log.i("setKarma", temp.get(i) + ": added karma");
                    photo.setKarma(photo.getKarma()+1);
                }
                else{
                    photo.setReleased(true);
                    deleteFile(temp.get(i).getPath()); //delete from file
                    temp.remove(i);
                }
            }
            Log.i("setKarma", photo.getPath() + ": karma:  "+ photo.getKarma());


        }
        Global.displayCycle = temp;
        for(Photo p: Global.displayCycle){
            Log.i("setKarma", p.getPath() + ": karma:  "+ p.getKarma());
        }
    }

    public void deleteFile(String path){
        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Log.i(TAG, "deleteFile--- DELETED: "+path);
            } else {
                Log.i(TAG, "deleteFile--- ERROR: "+path);
            }
        }
    }

    // resize the file to fit screen size before upload
    public static Bitmap resizeImage(Bitmap image) {
        Bitmap resizedImage = null;

        if(image != null) {
            int imageHeight = image.getHeight();
            if(imageHeight > Global.windowHeight) {
                imageHeight = Global.windowHeight;
            }

            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            if(imageWidth > Global.windowWidth) {
                imageWidth = Global.windowWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }

            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }

        return resizedImage;
    }

    //get bitmap of image from file
    public static Bitmap getBitmap(String path){
       Bitmap bitmap = null;
        try {
            Log.i("FileManager", "GetBitmap: " + path);
            FileInputStream imgIS = new FileInputStream(new File(path));
            BufferedInputStream bufIS = new BufferedInputStream(imgIS);
            bitmap = BitmapFactory.decodeStream(bufIS);
            Log.i("FileManager", "GetBitmap Finished: " + bitmap);
        } catch (FileNotFoundException e) { //catch fileinputstream exceptions
            e.printStackTrace();
        } //trying to get wallpaper from display cycle node
        return bitmap;
    }

    //remove friends images from phone
    public static void deleteFolder(String name){
        File dir = new File(Environment.getExternalStorageDirectory()+name);
        if(dir.isDirectory()){
            dir.delete();
        }
    }

    public static String getDirPath(String directory){
        String folder = Environment.getExternalStorageDirectory()+"/"+directory;
        return folder;
    }

    /*
    this method handles adding file paths to upload queue whether this is because changed location or changed karma
     */
    public void addToQueue(String path){
       ArrayList<String> temp = Global.uploadMetaData;

        boolean inQueue = false;
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).equals(path)){
                break; //already in the upload queue
            }
        }

        if(!inQueue){
            temp.add(path);
        }

        Global.uploadMetaData = temp;
    }
}