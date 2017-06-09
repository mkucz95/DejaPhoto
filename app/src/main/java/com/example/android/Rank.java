package com.example.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//
//import static java.lang.Math.pow;
//import static java.lang.Math.sqrt;
import static android.content.ContentValues.TAG;
import static java.lang.StrictMath.abs;

public class Rank {
    private double localLat, localLng;

    boolean isLocaOn = false, isTimeOn = false, isKarma = false, isWeekOn = false;

    private ArrayList<Photo> photo;
    //each photo is populated with all the information we need

    private boolean[] settings; //location, time, day, karma

    public Rank(String localLat, String localLong, boolean b1, boolean b2, boolean
            b3, boolean b4, ArrayList<Photo> cycle, Context context) {
        //photo = Global.displayCycle;
        photo = cycle;

        settings = Global.getSettings();
        setMyLocation(localLat, localLong);
        Log.i("rankClass", "My Location : " + localLat + ", " + localLong);

        isLocaOn = settings[1];
        isWeekOn = settings[2];
        isTimeOn = settings[3];
        isKarma = settings[4];
        Log.i("setKarma", "KarmaSetting: " + Global.getSettings()[4]);

        for (Photo p : Global.displayCycle) {
            Log.i("setKarma", p.getPath() + " == karma == " + p.getKarma());
        }
        isLocaOn = b2;
        isWeekOn = b3;
        isTimeOn = b1;
        isKarma = b4;
        Log.i("location~~~", localLat + "");
        Log.i("location~~~", localLng + "");
        sort(context); //sort the array list

        for (Photo x : photo) {
            float[] a = new float[1];
            if (x.getLatLong()[0] != null && x.getLatLong()[1] != null) {
                Location.distanceBetween(this.localLat, this.localLng,
                        Double.parseDouble(x.getLatLong()[0]),
                        Double.parseDouble(x.getLatLong()[1]), a);

                double distance = a[0] / 3.28;
                //       Log.i("rankClass", photo.size() + "");
                Log.i("distanceRank", x.getPath() + " is " + distance + " feet away from your location");
            } else {
                //Log.i("distanceRank", "Distance from: null");
            }
            Log.i("distanceRank", x.getPath() + " : " + x.getDateTaken());
            Log.i("distanceRank", x.getPath() + " : " + x.getDayOfWeek());
            Log.i("distanceRank", "Karma: " + x.getKarma());
        }
        for (Photo y : Global.displayCycle) {
            Log.i("Global Photos: ", y.getPath());
        }
    }


    public void sort(Context context) {
        Photo temp;
        long curMiliSecond = System.currentTimeMillis();
        Log.d("printertime", curMiliSecond + "");

        Log.i("rankClass", "Sorting...");

        java.sql.Date date = new java.sql.Date(curMiliSecond);
        //long to Date type

        String dayOfWeek = getWeekOfDate(date);
        // Date type to  day of week

        int dayInt = week(dayOfWeek);
        //the integer for the day of week

        int dayIntDiff1, dayIntDiff2; //to compare difference between days

        double photo1Lat = 0, photo1Lng = 0, photo2Lat = 0, photo2Lng = 0;
        Log.i("distanceRank", "Number of photos: " + photo.size());
        for (int i = 0; i < photo.size(); i++) {
            Log.i("distanceRank", "iteration " + i + "----------");
            for (Photo p : photo) {
                Log.i("distanceRank", p.getPath());
            }
            for (int j = 1; j < photo.size(); j++) {
                Log.i("jis ", j + " i is " + i);

                Geocoder gc = new Geocoder(context, Locale.getDefault());//Locale.getDefault()follow the system's language

                PhotoLocation photo1Location = new PhotoLocation(photo.get(j - 1).getPath(), gc, false);
                PhotoLocation photo2Location = new PhotoLocation(photo.get(j).getPath(), gc, false);

                try {
                    photo1Lat = photo1Location.Latitude;
                    photo1Lng = photo1Location.Longitude;
                    photo2Lat = photo2Location.Latitude;
                    photo2Lng = photo2Location.Longitude;
                } catch (Exception e) {
                }


                int dayPhoto1 = week(photo.get(j - 1).getDayOfWeek());
                //photo1 's interger for the day of week
                int dayPhoto2 = week(photo.get(j).getDateTaken());

                int karma1 = photo.get(j - 1).getKarma();
                Log.i("KarmaCheck", "Photo1" + karma1);
                int karma2 = photo.get(j).getKarma();
                Log.i("KarmaCheck", "Photo2" + karma2);

                float[] dist = new float[1];

                Location.distanceBetween(localLat, localLng, photo1Lat, photo1Lng, dist);

                double distance1 = dist[0] / 3.28;   //meter to feet -- Distance between photo1 and current location
                Log.i("RankCheck ", "Distance from " + photo.get(j - 1).getPath() + " : " + distance1 + "ft");
                Log.i("location!!!", distance1 + "");
                float[] dist1 = new float[1];
                Location.distanceBetween(localLat, localLng, photo2Lat, photo2Lng, dist1);
                double distance2 = dist1[0] / 3.28; // Distance in ft between photo2 and current location
                Log.i("RankCheck", "Distance from " + photo.get(j).getPath() + " : " + distance2 + "ft");
                Log.i("location!!!", distance2 + "");
                /*find the real different on the day of the week*/
                if (abs(dayPhoto1 - dayInt) > 3)
                    dayIntDiff1 = 7 - abs(dayPhoto1 - dayInt);
                else dayIntDiff1 = abs(dayPhoto1 - dayInt);

                if (abs(dayPhoto2 - dayInt) > 3)
                    dayIntDiff2 = 7 - abs(dayPhoto2 - dayInt);
                else dayIntDiff2 = abs(dayPhoto2 - dayInt);
                int changeInt = 0;

                Log.i("distanceRank", "Karma is on: " + isKarma);
                if (isKarma) {
                    Log.d("distanceRank", "Karma setting on");
                    if (karma1 < karma2)  //  !karma1 && karma2
                        changeInt = changeInt + 1;
                    else if (karma1 > karma2) // karma2 && karma1
                        changeInt = changeInt - 1;
                    else //karma1 == karma2
                        Log.d("distanceRank", "karma equal");
                }
                if (isLocaOn) {
                    Log.i("distanceRank", "sorting by location...");


                    if (distance1 < distance2) {
                        changeInt = changeInt - 2;
                    } else if (distance1 > distance2) {
                        changeInt = changeInt + 2;
                    }

                    Log.i("distanceRank", "ChangeInt after location: " + changeInt);


                }
                if (isWeekOn) {
                    Log.i("distanceRank", "Day of week setting on");
                    Log.i("distanceRank", "Photo 1 day: " + dayIntDiff1 + ", Photo 2 day: "
                            + dayIntDiff2);
                    if (dayIntDiff1 < dayIntDiff2)
                        changeInt = changeInt - 2;
                    else if (dayIntDiff1 > dayIntDiff2)
                        changeInt = changeInt + 2;

                    Log.i("distanceRank", "ChangeInt after week : " + changeInt);


                }
                if (isTimeOn) {
                    Log.i("distanceRank", "Time setting on");
                    Log.i("distanceRank", photo.get(j - 1).getPath() + " time: " +
                            photo.get(j - 1).getDateTaken() + ", " + photo.get(j).getPath() +
                            " time: " + photo.get(j).getDateTaken());


                    if (Long.parseLong(photo.get(j - 1).getDateTaken()) >
                            Long.parseLong(photo.get(j).getDateTaken())) {

                        changeInt = changeInt - 2;
                    } else if (Long.parseLong(photo.get(j - 1).getDateTaken()) <
                            Long.parseLong(photo.get(j).getDateTaken())) {

                        changeInt = changeInt + 2;
                    }

                    Log.i("distanceRank", "ChangeInt after time : " + changeInt);

                }
                Log.i("distanceRank", "ChangeInt : " + changeInt);
                if (changeInt > 0) {
                    Log.i("distanceRank", "Swapping " + photo.get(j - 1).getPath() + "[" + (j - 1) +
                            "] and " + photo.get(j).getPath() + "[" + (j) + "]");
                    temp = photo.get(j - 1);
                    photo.set((j - 1), photo.get(j));
                    photo.set(j, temp);
                }
            }
        }

        for (Photo p : this.photo) {
            for (Photo x : Global.displayCycle) {
                if (p.getPath().equals(x.getPath())) {
                    Log.i("RebuildCycle", p.getPath() + " == " + x.getPath());
                    Log.i("RebuildCycle", "replacing Photo in this.photo with x...");
                    p = x;
                }
            }
        }
        Log.i("RankCheck", "________________________");


        Global.displayCycle = this.photo; //update the global variable for display cycle

        for (Photo y : Global.displayCycle) {
            Log.i("RankCheck", y.getPath());
        }
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

    private String tempPath(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);

        if (path.contains("DejaCopy")) {
            Log.i(TAG, path + " contains 'DejaCopy'");
            path = "/storage/emulated/0/" + filename;
            Log.i(TAG, "New path: " + path);
        } else {
            Log.i(TAG, "New path not needed");
        }

        return path;
    }
}