package com.example.android;

import android.os.Environment;

import com.google.firebase.storage.StorageReference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Michael on 6/5/17.
 */

@RunWith(JUnit4.class)
public class PhotoStorageTest {

    @Test
    public void testGetRef1() {
        StorageReference storageReference = PhotoStorage.getStorageRef("user@gmail,com");

        assertEquals(storageReference.toString().contains("gs://dejaphoto-33.appspot.com/user%40gmail%2Ccom"), true);
    }

    @Test
    public void testGetRef2() {
        StorageReference storageReference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        assertEquals(storageReference.toString().contains("gs://dejaphoto-33.appspot.com/hlcphantom%40gmail%2Ccom"), true);
    }

    String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-1.jpg";
    String path2 = "/storage/emulated/0/DejaCopy/10_UCSD.jpg";

    @Test
    public void testUpload1() {
        StorageReference reference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        PhotoStorage photoStorage = new PhotoStorage(path1, reference);
        photoStorage.addElement();
        assertTrue(true);
    }

    @Test
    public void testUpload2() {
        StorageReference reference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        PhotoStorage photoStorage = new PhotoStorage(path2, reference);
        photoStorage.addElement();
        assertTrue(true);
    }

    @Test
    public void testGetStorageRef1() {
        StorageReference reference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        assertNotNull(reference);
    }

    @Test
    public void testDirExists1() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "WTF");
        assertTrue(!folder.exists());
    }

}
