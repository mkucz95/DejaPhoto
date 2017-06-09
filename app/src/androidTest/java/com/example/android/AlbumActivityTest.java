package com.example.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.widget.Switch;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static android.os.Build.VERSION_CODES.M;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 5/11/2017.
 */

public class AlbumActivityTest {
    @Rule
    public ActivityTestRule<AlbumActivity> albumActivityActivity = new ActivityTestRule<AlbumActivity>
            (AlbumActivity.class);


    AlbumActivity ab = albumActivityActivity.getActivity();


   @Test
   public void testSetDejaAlbum() {
        Boolean da = true;
        Boolean ca = true;
        Boolean da2 = false;
        Boolean ca2 = false;
      //  ab.setSharedPreferences();
//        ab.setDejaAlbum(da2,ca2);
//
//        assertEquals(da,da2);
//        assertEquals(ca,ca2);

    }

}
