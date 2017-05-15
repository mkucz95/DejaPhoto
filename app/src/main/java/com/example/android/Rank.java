package com.example.android;


import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//
//import static java.lang.Math.pow;
//import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class Rank {
    private double localLat;
    private double localLng;

    /*we still not get the two varible value*/

    boolean isLocaOn = false;
    boolean isTimeOn = false;
    boolean isKarma = false;
    boolean isWeekOn = false;

    private ArrayList<Photo> photo = new ArrayList<>();
    //each photo is populated with all the information we need
    private boolean[] settings; //location, time, day, karma


    public Rank(ArrayList<Photo> list, boolean[] settings, String localLat, String localLong, boolean b1, boolean b2, boolean
            b3, boolean b4) {
        this.photo = list;
        this.settings = settings;
        setMyLocation(localLat, localLong);
        isTimeOn = b1;
        isLocaOn = b2;
        isWeekOn = b3;
        isKarma = b4;


        sort(); //sort the array list
        Log.d("finish sort", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Photo x : photo) {
            float[] a = new float[1];
            if (x.getLatLong()[0] != null && x.getLatLong()[1] != null) {
                Location.distanceBetween(this.localLat, this.localLng, Double.parseDouble(x.getLatLong()[0]), Double.parseDouble(x.getLatLong()[1]), a);
                double distance = a[0] / 3.28;
         //       Log.i("rankClass", photo.size() + "");
                Log.i("rankClass", "Distance from: " + distance);
                Log.i("time",x.getDayOfWeek());
            } else
                Log.i("rankClass", "Distance from: null");


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(x.getDateTaken()));
            String time = formatter.format(calendar.getTime());
        //    Log.d("rankClass", x.getDayOfWeek() + " , " + time);
          //  Log.d("rankClass", x.getLatLong()[0] + " , " + x.getLatLong()[1]);

        }

    }


    public void sort() {

        Photo temp = new Photo();
        long curMiliSecond = System.currentTimeMillis();
        long hour = Long.parseLong(getHour(curMiliSecond));
        SimpleDateFormat sdf1 = new SimpleDateFormat();


        java.sql.Date date = new java.sql.Date(curMiliSecond);                         //long to Date type
        String dayOfWeek = getWeekOfDate(date);              // Date type to  day of week
        int dayInt = week(dayOfWeek);                       //the integer for the day of week

        int dayIntDiff1 = 0;      //dayPhoto1 compare to dayInt
        int dayIntDiff2 = 0;

        double photo1Lat = 0;
        double photo1Lng = 0;
        double photo2Lat = 0;
        double photo2Lng = 0;
        Log.d("photosize",photo.size()+"");
        for (int i = 0; i < photo.size() - 1; i++) {
            for (int j = 1; j < photo.size(); j++) {
                Log.d("jis ",j+" i is "+i);
                String[] photo1GPS = photo.get(j - 1).getLatLong();
                String[] photo2GPS = photo.get(j).getLatLong();

                try {
                    photo1Lat = Double.parseDouble(photo1GPS[0]);
                    photo1Lng = Double.parseDouble(photo1GPS[1]);
                    photo2Lat = Double.parseDouble(photo2GPS[0]);
                    photo2Lng = Double.parseDouble(photo2GPS[1]);
                } catch (Exception e) {
                    //no location information in the pictures
//                    return;
                    Log.d("catch", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }


                int dayPhoto1 = week(photo.get(j - 1).getDayOfWeek());       //photo1 's interger for the day of week
                int dayPhoto2 = week(photo.get(j).getDateTaken());

                float[] dist = new float[1];

                Log.d("test location", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                Location.distanceBetween(localLat, localLng, photo1Lat, photo1Lng, dist);
                double distance1 = dist[0] / 3.28;   //meter to feet
                Log.d("testdistance 1", distance1 + " j is " + j);
                Location.distanceBetween(localLat, localLng, photo2Lat, photo2Lng, dist);
                double distance2 = dist[0] / 3.28;
                Log.d("testdistance2", distance2 + "");

                /*find the real different on the day of the week*/
                if (abs(dayPhoto1 - dayInt) > 3)
                    dayIntDiff1 = 7 - abs(dayPhoto1 - dayInt);
                else dayIntDiff1 = abs(dayPhoto1 - dayInt);

                if (abs(dayPhoto2 - dayInt) > 3)
                    dayIntDiff1 = 7 - abs(dayPhoto2 - dayInt);
                else dayIntDiff2 = abs(dayPhoto2 - dayInt);
                int changeInt = 0;
                if (isKarma) {
                    if (!photo.get(j - 1).isKarma() && photo.get(j).isKarma())
                        changeInt = changeInt + 1;
                    else if (photo.get(j - 1).isKarma() && !photo.get(j).isKarma())
                        changeInt = changeInt - 1;
                }
                if (isLocaOn) {
                    if (distance1 > 1000 && distance2 <= 1000)
                        changeInt = changeInt + 2;
                    else if (distance1 <= 1000 && distance2 > 1000)
                        changeInt = changeInt - 2;
                    else if (distance1 > 1000 && distance2 > 1000 && distance1 > distance2)
                        changeInt = changeInt + 2;
                    else if (distance1 > 1000 && distance2 > 1000 && distance1 < distance2)
                        changeInt = changeInt - 2;
                }
                if (isWeekOn) {
                    if (dayIntDiff1 < dayIntDiff2)
                        changeInt = changeInt - 2;
                    else if (dayIntDiff1 > dayIntDiff2)
                        changeInt = changeInt + 2;
                }
                if (isTimeOn) {
                    if (abs(photo.get(j - 1).getHour() - hour) <= 2 && abs(photo.get(j).getHour() - hour) > 2)
                        changeInt = changeInt - 2;
                    else if (abs(photo.get(j - 1).getHour() - hour) > 2 && abs(photo.get(j).getHour() - hour) <= 2)
                        changeInt = changeInt + 2;
                    else if ((abs(photo.get(j - 1).getHour() - hour) > 2 && abs(photo.get(j).getHour() - hour) > 2)
                            && abs(photo.get(j - 1).getHour() - hour) < abs(photo.get(j).getHour() - hour))
                        changeInt = changeInt - 2;
                    else if ((abs(photo.get(j - 1).getHour() - hour) > 2 && abs(photo.get(j).getHour() - hour) > 2)
                            && abs(photo.get(j - 1).getHour() - hour) > abs(photo.get(j).getHour() - hour))
                        changeInt = changeInt + 2;
                }
                if (changeInt > 0) {
                    temp = photo.get(j - 1);
                    photo.set((j - 1), photo.get(j));
                    photo.set(j, temp);
                }
            }
        }
    }


    public String[] getPaths() { //ONLY CALL AFTER FULLY RERANKED!!!
        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < this.photo.size(); i++) {
            if (!this.photo.get(i).isReleased()) paths.add(this.photo.get(i).getPath());
            Log.i("RankClass", "~~~~~~~~~~~~~~~~~~~~~~~this is path: " + this.photo.get(i).getPath() + "=================released?--- " + this.photo.get(i).isReleased());
        } //add only images without released in their fields

        String[] pathArray = paths.toArray(new String[paths.size()]);

        return pathArray;
    }

    public void setMyLocation(String localLat, String localLong) {
        if (localLat == "") this.localLat = 0;
        else this.localLat = Double.parseDouble(localLat);
        if (localLong == "") this.localLng = 0;
        else this.localLng = Double.parseDouble(localLong);
    }


    //long time from 1970 transfer day of week
    public static String getWeekOfDate(java.sql.Date dt) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getHour(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(l);

    }


    //day of week -> int for compare
    public static int week(String s) {
        if (s.equals("Sunday"))
            return 0;
        else if (s.equals("Monday"))
            return 1;
        else if (s.equals("Tuesday"))
            return 2;
        else if (s.equals("Wednesday"))
            return 3;
        else if (s.equals("Thursday"))
            return 4;
        else if (s.equals("Friday"))
            return 5;
        else
            return 6;

    }
}