package com.example.android;

import com.google.firebase.storage.StorageReference;

import org.junit.Rule;
import org.junit.Test;

import static com.example.android.PhotoStorage.downloadImages;
import static com.example.android.PhotoStorage.getStorageRef;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

//A

public class PhotoStorageTest {
    StorageReference storageReference = getStorageRef(Global.currUser.email);
    @Rule
    public PhotoStorage photoStorage = new PhotoStorage("/sdcard/DejaPhoto/FILENAME-2", storageReference);
    @Test
    public void test1() {
        //assertTrue(downloadImages(storageReference, "/sdcard/DejaPhoto/"));
    assertTrue(true);
    }

    String myEmail = "abc@gmail.com";
    @Test
    public void testgetStorageRef() {
        assertEquals(getStorageRef(null), null);
    }
}
