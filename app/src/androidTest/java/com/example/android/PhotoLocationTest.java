package com.example.android;

import android.content.Context;
import android.location.Geocoder;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by mkucz on 5/11/2017.
 */

public class PhotoLocationTest {
    Context context = InstrumentationRegistry.getTargetContext();
    String path = "/storage/emulated/0/IMG_1156.JPG";
    Geocoder gc = new Geocoder(context, Locale.getDefault());
    String lat = "34.44296666666666";
    String longitude = "-118.5743111111111";
    Double doubleLat = Double.parseDouble(lat);
    Double doubleLong = Double.parseDouble(longitude);


    PhotoLocation location = new PhotoLocation(path, gc);
    @Test
    public void test1(){

        assertEquals(location.Latitude, doubleLat);
    }

    @Test
    public void test2(){

        assertEquals(location.Longitude, doubleLong);
    }

}
