package com.example.android;

import android.support.annotation.NonNull;

import com.example.android.Global;
import com.example.android.PhotoStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static com.example.android.PhotoStorage.downloadImages;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wl36901 on 2017/6/6.
 */

public class TestDownload {
    StorageReference storageReference = PhotoStorage.getStorageRef(Global.currUser.email);
    @Rule
    public PhotoStorage photoStorage = new PhotoStorage("/sdcard/DejaPhoto/FILENAME-2", storageReference);
    @Test
    public void test1() {
        //assertTrue(downloadImages(storageReference, "/sdcard/DejaPhoto/"));

    }
}
