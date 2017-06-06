
package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.android.AddFriendsActivity;
import com.example.android.Global;
import com.example.android.PhotoStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wl36901 on 2017/6/4.
 */

@RunWith(JUnit4.class)
public class TestUpload {
    String path = "/sdcard/DejaPhoto/FILENAME-2/";
   // String path = "/storage/emulated/0/1_gliderport.jpg";

    @Test
    public void test1() {
        StorageReference reference = PhotoStorage.getStorageRef("hlcphantom@gmail.com");
        PhotoStorage photoStorage = new PhotoStorage(path, reference);

        assertTrue(photoStorage.addElement());
    }
}
