package com.example.android;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by mkucz on 5/5/2017.
 */

public class Photo {
    private String imagePath;
    private String location;
    private Time time;
    private Date day;
    private boolean karma = false;

    public Photo(){}

    //constructor
    public Photo(String imagePath) {
        this.imagePath = imagePath;
        this.location = null;
        this.time = null;
        this.day = null;
       //TODO get variable information from imagePath
        //TODO  create separate function
    }
public void populatePhotoInfo(Photo photo){
    //TODO get variable information from imagePath
}
//SETTERS AND GETTERS
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public boolean isKarma() {
        return karma;
    }

    public void setKarma(boolean karma) {
        this.karma = karma;
    }
}
