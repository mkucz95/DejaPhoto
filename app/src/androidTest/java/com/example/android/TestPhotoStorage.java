package com.example.android;

import com.google.firebase.storage.StorageReference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Michael on 6/5/17.
 */

@RunWith(JUnit4.class)
public class TestPhotoStorage {

    @Test
    public void testGetRef1() {
        StorageReference storageReference = PhotoStorage.getStorageRef("user@gmail,com");

        assertEquals(storageReference.toString().contains( "gs://dejaphoto-33.appspot.com/user%40gmail%2Ccom"), true);
    }

    @Test
    public void testGetRef2() {
        StorageReference storageReference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        assertEquals(storageReference.toString().contains( "gs://dejaphoto-33.appspot.com/hlcphantom%40gmail%2Ccom"), true);
    }

    String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-2.jpg";

    @Test
    public void testUpload1() {
        StorageReference reference = PhotoStorage.getStorageRef("hlcphantom@gmail,com");

        PhotoStorage photoStorage = new PhotoStorage(path1, reference);

        assertEquals(photoStorage.addElement(), true);
    }
}
