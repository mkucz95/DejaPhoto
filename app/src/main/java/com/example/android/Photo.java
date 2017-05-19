package com.example.android;

import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mkucz on 5/5/2017.
 * Each photo object has relevant information needed to rank it according to specifications
 */

public class Photo {
    private String imagePath;
    private String latitude;
    private String longitude;
    private String date_taken;
    private long hour;
    private String dayOfWeek;
    private boolean karma = false;
    private boolean released = false;

    public Photo(){

    }

    //constructor gets information from the rerank method
    public Photo(String imagePath, String date_taken, String latitude, String longitude)
    {

        this.imagePath = imagePath;

        this.date_taken = date_taken;

        long temp = Long.parseLong(date_taken.toString());  //string to long   (millSec)
        Date date = new Date(temp);                         //long to Date type
        this.dayOfWeek = getWeekOfDate(date);              // Date type to  day of week

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        this.hour = Long.parseLong(sdf.format(temp));

        this.latitude = latitude;
        this.longitude = longitude;
    }


    //long time from 1970 transfer day of week
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //SETTERS AND GETTERS
    public String getPath() {
        return imagePath;
    }

    public String[] getLatLong() {

      String[] latLong = {this.latitude, this.longitude};
        Log.d("lattitud",latitude+"");
        Log.d("logtitude",longitude+"");
      return latLong;
    }
    public String getDayOfWeek() { return dayOfWeek;}

    public long getHour(){return hour;}

    public String getDateTaken() {
        return date_taken;
    }

    public boolean isKarma() {
        return this.karma;
    }

    public void setKarma(boolean boolKarma){
        this.karma = boolKarma;
    }

    public void setReleased(boolean flag){
        this.released = flag;
    }

}
