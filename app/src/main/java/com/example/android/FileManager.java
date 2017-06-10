package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileManager {
    private final String TAG = "FileManager";
    Context context;
    File file;

    public FileManager(Context context) {
        this.context = context;
    }

    public FileManager() {
    }

    //used after downloading images so that we can see them in gallery
    public void scanSD(File file) {
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScan.setData(contentUri);
        context.getApplicationContext().sendBroadcast(mediaScan);
    }

    //saves images to a specified folder
    public void saveFile(Bitmap imageToSave, String folder) {
        File folderName = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folder);

        if (!folderName.exists()) {
            Log.i(TAG, "Folder doesn't exist, creating it...");
            boolean rv = folderName.mkdir();
            Log.i(TAG, "Folder creation " + (rv ? "success" : "failed"));
        } else {
            Log.i(TAG, "Folder already exists.");
        }

        String captured = Global.currUser.email + "Deja_" + Global.imageNumber + ".jpg";
        Global.imageNumber++;

        File file = new File(folderName, captured);

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Global.uploadImageQueue.add(file.getAbsolutePath());
            Log.d(TAG, "filePath added: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanSD(file);

        Log.d(TAG, "+++++++");
    }

    //copies files over to new destination-- used by photo picker
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
        scanSD(file);

    }

    public void setDisplayCycleData(boolean addKarma, int karma, String path) {
        ArrayList<Photo> temp = Global.displayCycle;
        for (int i = 0; i < temp.size(); i++) {
            Photo photo = temp.get(i);
            Log.i("setKarma", path + " compare to : " + photo.getPath());
            if (photo.getPath().equals(path)) {
                if (addKarma) {
                    Log.i("setKarma", temp.get(i) + ": added karma");
                    photo.setKarma(karma);
                } else {
                    photo.setReleased(true);
                    deleteFile(temp.get(i).getPath()); //delete from file
                    temp.remove(i);
                }
            }
            Log.i("setKarma", photo.getPath() + ": karma:  " + photo.getKarma());

        }
        Global.displayCycle = temp;
        for (Photo p : Global.displayCycle) {
            Log.i("setKarma", p.getPath() + ": karma:  " + p.getKarma());
        }
    }

    public void deleteFile(String path) {
        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Log.i(TAG, "deleteFile--- DELETED: " + path);
            } else {
                Log.i(TAG, "deleteFile--- ERROR: " + path);
            }
        }
    }

    // resize the file to fit screen size before upload
    public static Bitmap resizeImage(Bitmap image) {
        Bitmap resizedImage = null;

        if (image != null) {
            int imageHeight = image.getHeight();
            if (imageHeight > Global.windowHeight) {
                imageHeight = Global.windowHeight;
            }

            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            if (imageWidth > Global.windowWidth) {
                imageWidth = Global.windowWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }

            resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
        }

        return resizedImage;
    }

    //get bitmap of image from file
    public static Bitmap getBitmap(String path) {
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
    public static void deleteFolder(String name) {
        File dir = new File(Environment.getExternalStorageDirectory() + name);
        if (dir.isDirectory()) {
            dir.delete();
        }
    }

    /*
    this method handles adding file paths to upload queue whether this is because changed location or changed karma
     */
    public void addToQueue(String path) {
        ArrayList<String> temp = Global.uploadMetaData;

        boolean inQueue = checkInQueue(path, temp);

        if (!inQueue) {
            temp.add(path);
        }

        Global.uploadMetaData = temp;
    }

    private boolean checkInQueue(String path, ArrayList<String> temp) {
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).equals(path)) {
                return true;
            }
        }
        return false;
    }

    //write karma to file
    public static void addKarma(String path, Context context) {
        Log.d("FileManager", "addKarma- " + path);

        SQLiteHelper helper = new SQLiteHelper();

        String raw = helper.getSingleLine(Global.mediaUri, Global.descriptionProjection, path, context);
        String[] info = handleCSV(raw);
        int currKarma;

        String currLocation;

        if(info != null) {
            if (info[1] != null) {
                currKarma = Integer.parseInt(info[1]);
                currKarma++;
            } else {
                currKarma = 0;
            }
            if(info[0] != null) {
                helper.storeSQLData(info[0] + "," + currKarma,
                        MediaStore.Images.ImageColumns.DESCRIPTION, path, context);
            }
            else{
                helper.storeSQLData("," + currKarma,
                        MediaStore.Images.ImageColumns.DESCRIPTION, path, context);
            }
        }
        else{
            currKarma = 0;
            currLocation = "";
            helper.storeSQLData(currLocation + "," + currKarma,
                    MediaStore.Images.ImageColumns.DESCRIPTION, path, context);
        }



    }

    //write custom location to file
    public static void changeLoc(String path, String customLoc, Context context) {
        Log.d("FileManager", "addLoc- " + path + " -- " + customLoc);

        SQLiteHelper helper = new SQLiteHelper();
        String raw = helper.getSingleLine(Global.mediaUri, Global.descriptionProjection, path, context);
        String[] info = handleCSV(raw);

        if (info[1] != null)
            helper.storeSQLData(customLoc + "," + info[1],
                    MediaStore.Images.ImageColumns.DESCRIPTION, path, context);

        else helper.storeSQLData(customLoc + ",0",
                MediaStore.Images.ImageColumns.DESCRIPTION, path, context);
    }

    public static String[] handleCSV(String info) {
        if (info != null)
            return info.split(",");

        else return null;
    }
}