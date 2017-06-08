package com.example.android;

import com.example.android.Global;
import com.example.android.PhotoStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by wl36901 on 2017/6/6.
 */

public class TestEqualUploadAndDownload {
    StorageReference storageReference = PhotoStorage.getStorageRef(Global.currUser.email);
    @Rule
    public PhotoStorage photoStorage = new PhotoStorage("/sdcard/DejaPhoto/FILENAME-2", storageReference);

    @Test
    public void test1(){


    }
}
