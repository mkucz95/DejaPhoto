package com.example.android;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by mkucz on 5/5/2017.
 */

public class Photo {
    private String imagePath;
    private String latitude;
    private String longitude;
    private String date_taken;
    private String description;
    private boolean karma = false;
    private boolean released = false;

    public Photo(){}

    //constructor gets information from the rerank method
    public Photo(String imagePath, String description, String date_taken, String latitude, String longitude)
    {
        this.imagePath = imagePath;
        this.description = description;
        this.date_taken = date_taken;
        this.latitude = latitude;
        this.longitude = longitude;
        this.karma = decodeDescription(description, true);
        this.released = decodeDescription(description, false);
    }

//SETTERS AND GETTERS
    public String getPath() {
        return imagePath;
    }

    public String[] getLatLong() {
      /*  FindLocationName findLocationName = new FindLocationName(imagePath);
        return findLocationName.findLocation();*/

      String[] latLong = {this.latitude, this.longitude};
      return latLong;
    }

    public String getDateTaken() {
        return date_taken;
    }

    public boolean isKarma() {
        return karma;
    }
    public boolean isReleased() {return released;}

    private boolean decodeDescription(String description, boolean action) {
        if (action) { //trying to decode karma
            if (description.toLowerCase().contains("karma")) return true;
        } else { //decoding release
            if (description.toLowerCase().contains("released")) return true;
        }

        return false; //neither karma nor released
    }

}
