package com.example.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

/**
 * Created by Michael on 6/5/17.
 */

@RunWith(JUnit4.class)
public class TestFileManager {

    String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-20.jpg";
    String path2 = "/storage/emulated/0/DejaPhoto/FILENAME-2.jpg";

    @Test
    public void bitmapTest1() {
        Bitmap bitmap = FileManager.getBitmap(path1);
        assertEquals(bitmap, null);
    }

    @Test
    public void bitmapTest2() {
        Bitmap bitmap = FileManager.getBitmap(path2);
        assertNotNull(bitmap);
    }

    @Test
    public void bitmapTest3() {
        Bitmap bitmap1 = FileManager.getBitmap(path1);
        Bitmap bitmap2 = FileManager.getBitmap(path2);

        assertNotSame(bitmap1, bitmap2);
    }

    @Test
    public void getUriTest() {
        Context context = null;
       FileManager fileManager = new FileManager(context);
        Uri uri = Uri.fromFile(new File(path2));
       String pathTest = fileManager.getImagePath(uri);

        assertEquals(path1, pathTest);
    }
}
