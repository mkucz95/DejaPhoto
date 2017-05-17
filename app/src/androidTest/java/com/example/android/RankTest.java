package com.example.android;

import junit.framework.TestCase;
import com.example.android.Photo;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by mkucz on 5/11/2017.
 */

public class RankTest extends TestCase{
    String path = "/storage/emulated/0/IMG_1156.JPG";
    String lat = "34.44296666666666";
    String longitude = "-118.5743111111111";
    String time = Long.toString(System.currentTimeMillis());
    Photo testPhoto = new Photo(path, time , lat, longitude);


    boolean[] testArr = {true, false};


  //  @Test
//    public void test1(){
//        ArrayList<Photo> testList = new ArrayList<Photo>();
//        testList.add(testPhoto);
//
//        Rank rank1 = new Rank(testList,testArr, lat, longitude, );
//        String[] testresult = rank1.getPaths();
//
//        assertEquals(path, testresult[0]);
//    }
}
