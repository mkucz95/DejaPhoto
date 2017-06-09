package com.example.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

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
public class FileManagerTest {

    String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-20.jpg";
    String path2 = "/storage/emulated/0/DejaPhoto/FILENAME-2.jpg";

    @Test
    public void addQueueTest() {
        FileManager fileManager = new FileManager();
        fileManager.addToQueue("test");

        boolean success = false;

        if (Global.uploadMetaData.get(Global.uploadMetaData.size() - 1).equals("test")) {
            success = true;
            Global.uploadMetaData.remove(Global.uploadMetaData.size() - 1);
        }

        assertEquals(true, success);

    }

    @Test
    public void csvTest1() {
        assertNull(FileManager.handleCSV(null));
    }

    @Test
    public void csvTest2() {
        String[] test = {"a", "b"};
        assertEquals(FileManager.handleCSV("a,b"), test);
    }

    @Test
    public void csvTest3() {
        String[] test = {"ab"};
        assertEquals(FileManager.handleCSV("ab"), test);
    }

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

}
