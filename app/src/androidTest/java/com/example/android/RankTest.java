package com.example.android;

import junit.framework.TestCase;
import com.example.android.Photo;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;

import java.util.ArrayList;

import static com.example.android.Rank.week;

/**
 * Created by mkucz on 5/11/2017.
 */

public class RankTest extends TestCase{



   // boolean[] testArr = {true, false};
    @Test
    public void testWeek() {
        assertEquals(week("Monday"),1);
        assertEquals(week("Tuesday"),2);
        assertEquals(week("Wednesday"),3);
        assertEquals(week("Thursday"),4);
        assertEquals(week("Friday"),5);
        assertEquals(week("dsfs"),6);

    }

    @Test
    public void testSort() {
        String localLat = "32.866499999999995";
         String localLng = "-117.22899833333334";

        String path = "/storage/emulated/0/1.JPG";
        String lat = "35.44296666666666";
        String longitude = "-118.5743111111111";
        String time = Long.toString(System.currentTimeMillis());
        Photo testPhoto1 = new Photo(path, time , lat, longitude);

        path = "/storage/emulated/0/2.JPG";
        lat = "60.44296666666666";
        longitude = "-130.5743111111111";
        time = Long.toString(System.currentTimeMillis());
        Photo testPhoto2 = new Photo(path, time , lat, longitude);

        path = "/storage/emulated/0/3.JPG";
        lat = "90.44296666666666";
        longitude = "-170.5743111111111";
        time = Long.toString(System.currentTimeMillis());
        Photo testPhoto3= new Photo(path, time , lat, longitude);

        ArrayList<Photo> t1 = new ArrayList<Photo>();
        t1.add(testPhoto3);
        t1.add(testPhoto2);
        t1.add(testPhoto1);

        Rank r1 = new Rank(localLat,localLng,false,true,false,false,t1);
        assertEquals(t1.get(2),testPhoto1);




    }
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
