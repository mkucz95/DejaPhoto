package com.example.android;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.runners.JUnit4;


/**
 * Created by mkucz on 5/11/2017.
 */

@RunWith(JUnit4.class)
public class PhotoTest{

    String path = "/storage/emulated/0/IMG_1156.JPG";
    String lat = "34.44296666666666";
    String longitude = "-118.5743111111111";
    String time = Long.toString(System.currentTimeMillis());
    Photo newPhoto = new Photo(path, time , lat, longitude);
    Photo photo2 = new Photo(path,  time , lat, longitude);


    @Test
    public void test1(){
    Date date = new Date();
        java.sql.Date dateSql = new java.sql.Date(System.currentTimeMillis());

        SimpleDateFormat today = new SimpleDateFormat("EEEE");
        assertEquals(today.format(date), newPhoto.getWeekOfDate(dateSql));
    }
/*
    @Test
    public void testKarmaTrue(){
        assertEquals(true, newPhoto.decodeDescription(newPhoto.getDescription(), true));
    }

    @Test
    public void testReleasedTrue(){
        assertEquals(true, newPhoto.decodeDescription(newPhoto.getDescription(), false));
    }
    @Test
    public void testKarmaFalse(){
        assertEquals(false, photo2.decodeDescription(photo2.getDescription(), true));
    }

    @Test
    public void testReleasedFalse(){
        assertEquals(false, photo2.decodeDescription(photo2.getDescription(), false));
    }*/
}
