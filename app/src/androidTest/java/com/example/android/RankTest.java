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

//TODO finish test rewrite

public class RankTest extends TestCase{
    String currTime = "1496885534406";
    String localLat = "32.866499999999995";
    String localLng = "-117.22899833333334";
    ArrayList<Photo> t1 = new ArrayList<>();
    Photo testPhoto1;
    Photo testPhoto2;
    Photo testPhoto3;
    Photo testPhoto4;
    Photo testPhoto5;

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
        String path = "/storage/emulated/0/1.JPG";
        String lat = "35.44296666666666";
        String longitude = "-118.5743111111111";
        String time = Long.toString(System.currentTimeMillis());
        testPhoto1 = new Photo(path, time , lat, longitude);

        path = "/storage/emulated/0/2.JPG";
        lat = "60.44296666666666";
        longitude = "-130.5743111111111";
        time = Long.toString(System.currentTimeMillis());
        testPhoto2 = new Photo(path, time , lat, longitude);

        path = "/storage/emulated/0/3.JPG";
        lat = "90.44296666666666";
        longitude = "-170.5743111111111";
        time = Long.toString(System.currentTimeMillis());
        testPhoto3= new Photo(path, time , lat, longitude);

        path = "/storage/emulated/0/3.JPG";
        lat = "90.44296666666666";
        longitude = "-170.5743111111111";
        time = Long.toString(System.currentTimeMillis());

        testPhoto4= new Photo(path, time , lat, longitude);
        testPhoto4.setKarma(testPhoto4.getKarma()+1);

        t1.add(testPhoto1);


        t1.clear();
        path = "/storage/emulated/0/2.JPG";
        lat = "60.44296666666666";
        longitude = "-130.5743111111111";
        time = Long.toString(System.currentTimeMillis()+ 5534406);
        testPhoto5 = new Photo(path, time , lat, longitude);
    }

    @Test
    public void timeTest(){
        Rank r3 =  new Rank(localLat,localLng,true,false,false,false,t1);
        t1.add(testPhoto1);
        t1.add(testPhoto5);
        assertEquals(t1.get(0),testPhoto1);
    }

    @Test
    public void karmaTest(){
        Rank r2 = new Rank(localLat, localLng,false,false,false,true,t1 );
        assertEquals(t1.get(1),testPhoto3);
    }

    @Test
    public void localTest(){
        //test local
        Rank r1 = new Rank(localLat,localLng,false,true,false,false,t1);
        assertEquals(t1.get(2),testPhoto1);

        t1.clear();
        t1.add(testPhoto3);
        t1.add(testPhoto4);
    }
}
