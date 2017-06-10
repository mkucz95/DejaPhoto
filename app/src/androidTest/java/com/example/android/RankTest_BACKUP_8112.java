package com.example.android;

import junit.framework.TestCase;
import com.example.android.Photo;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static com.example.android.Global.context;
import static com.example.android.Rank.week;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 5/11/2017.
 */

//TODO finish test rewrite

public class RankTest {
    Context context;

    String currTime = "1496885534406";
    String localLat = "32.866499999999995";
    String localLng = "-117.22899833333334";
    ArrayList<Photo> t1 = new ArrayList<>();



    String path = "/storage/emulated/0/1.JPG";
    String lat = "35.44296666666666";
    String longitude = "-118.5743111111111";
    String time = Long.toString(System.currentTimeMillis());
    Photo testPhoto1 = new Photo(path, time, lat, longitude);

    String path2 = "/storage/emulated/0/2.JPG";
    String lat2 = "60.44296666666666";
    String longitude2 = "-130.5743111111111";
    String time2 = Long.toString(System.currentTimeMillis());
    Photo testPhoto2 = new Photo(path, time, lat, longitude);

    String path3 = "/storage/emulated/0/3.JPG";
    String lat3 = "90.44296666666666";
    String longitude3 = "-170.5743111111111";
    String time3 = Long.toString(System.currentTimeMillis());
    Photo testPhoto3 = new Photo(path, time, lat, longitude);

    String path4 = "/storage/emulated/0/3.JPG";
    String lat4 = "90.44296666666666";
    String longitude4 = "-170.5743111111111";
    String time4 = Long.toString(System.currentTimeMillis());

    Photo testPhoto4 = new Photo(path, time, lat, longitude);


     ;
    String path5 = "/storage/emulated/0/2.JPG";
    String lat5 = "60.44296666666666";
    String longitude5 = "-130.5743111111111";
    String time5 = Long.toString(System.currentTimeMillis() + 5534406);
    Photo testPhoto5 = new Photo(path, time, lat, longitude);

    @Test
    public void testWeek() {
        assertEquals(week("Monday"), 1);
        assertEquals(week("Tuesday"), 2);
        assertEquals(week("Wednesday"), 3);
        assertEquals(week("Thursday"), 4);
        assertEquals(week("Friday"), 5);
        assertEquals(week("dsfs"), 6);

    }

    @Test
    public void testLocal() {

        t1.add(testPhoto1);
        t1.add(testPhoto2);
        t1.add(testPhoto3);
<<<<<<< HEAD
       // Rank r3 = new Rank(localLat, localLng, false, true, false, false, t1);
        assertEquals(t1.get(2),testPhoto1);


=======
        Rank r3 = new Rank(localLat, localLng, false, true, false, false, t1, context);
        assertEquals(t1.get(0),testPhoto1);
>>>>>>> a127613694996f201f4a0c337948d8be09bef787
    }

    @Test
    public void timeTest() {
        t1.clear();
        t1.add(testPhoto5);
        t1.add(testPhoto1);
        t1.add(testPhoto2);
        t1.add(testPhoto3);
       // t1.add(testPhoto5);
<<<<<<< HEAD
        //Rank r3 = new Rank(localLat, localLng, true, false, false, false, t1);
=======
        Rank r3 = new Rank(localLat, localLng, true, false, false, false, t1, context);
>>>>>>> a127613694996f201f4a0c337948d8be09bef787

        assertEquals(t1.get(0), testPhoto5);
    }

    @Test
    public void timeAndLocalTest() {
    //test time and local
        t1.clear();
        t1.add(testPhoto1);
        t1.add(testPhoto5);
        t1.add(testPhoto3);
      //  t1.add(testPhoto4);
<<<<<<< HEAD
       // Rank r4 = new Rank(localLat, localLng, true, true, false, false, t1);
=======
        Rank r4 = new Rank(localLat, localLng, true, true, false, false, t1, context);
>>>>>>> a127613694996f201f4a0c337948d8be09bef787

    assertEquals(testPhoto1, t1.get(0));
    }

    @Test
    public void karmaTest(){
        testPhoto4.setKarma(testPhoto4.getKarma() + 1);
        t1.clear();
        t1.add(testPhoto1);
        t1.add(testPhoto4);
        t1.add(testPhoto3);
<<<<<<< HEAD
        //Rank r2 = new Rank(localLat, localLng,false,false,false,true,t1 );
=======
        Rank r2 = new Rank(localLat, localLng,false,false,false,true,t1, context);
>>>>>>> a127613694996f201f4a0c337948d8be09bef787
        assertEquals(t1.get(0),testPhoto4);
    }
}
