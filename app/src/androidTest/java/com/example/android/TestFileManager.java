package com.example.android;

import android.graphics.Bitmap;
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
    public void test1() {
        Bitmap bitmap = FileManager.getBitmap(path1);
        assertEquals(bitmap, null);
    }

    @Test
    public void test2() {
        Bitmap bitmap = FileManager.getBitmap(path2);
        assertNotNull(bitmap);
    }

    @Test
    public void test3() {
        Bitmap bitmap1 = FileManager.getBitmap(path1);
        Bitmap bitmap2 = FileManager.getBitmap(path2);

        assertNotSame(bitmap1, bitmap2);
    }
}
