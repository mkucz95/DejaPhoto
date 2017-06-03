package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
    private final String TAG = "FileManager";
    Context context;

    public FileManager(Context context) {
            this.context = context;
    }

    public void saveFile(Bitmap imageToSave) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/DejaPhoto");

        if (!direct.exists()) {
            File directory = new File("/sdcard/DejaPhoto/");
            directory.mkdirs();
            Log.d(TAG, "album" + directory.exists());
        }


        String captured = "FILENAME-" + MainActivity.n + ".jpg";

        MainActivity.n++;

        File file = new File(new File("/sdcard/DejaPhoto/"), captured);

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.d(TAG, "file" + file.exists());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScan.setData(contentUri);
        context.getApplicationContext().sendBroadcast(mediaScan);

        Log.d(TAG, "+++++++");
    }/*
     * returns image path from uri using cursor
     */

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
}