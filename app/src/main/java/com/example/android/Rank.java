package com.example.android;


import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class Rank{
    private double localLat;
    private double localLng;
    double curTime;
    private ArrayList<Photo> photo = new ArrayList<>();
    //each photo is populated with all the information we need
    private boolean[] settings; //location, time, day, karma


    public Rank(ArrayList<Photo> list, boolean[] settings, String localLat, String localLong) {
        this.photo = list;
        this.settings = settings;
        setMyLocation(localLat, localLong);
        sort(); //sort the array list
    }

    public void sort(){
        /*setting sort interface Comparator*/
        Comparator<Photo> comparator = new Comparator<Photo>(){
            @Override
            public int compare(Photo o1, Photo o2) {
                double photo1Lat = Double.parseDouble(o1.getLatLong().toString());
                double photo1Lng = Double.parseDouble(o1.getLatLong().toString());
                double photo2Lat = Double.parseDouble(o2.getLatLong().toString());
                double photo2Lng = Double.parseDouble(o2.getLatLong().toString());
                double distance1 = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));
                double distance2  = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));
                double timeDif1 = abs(curTime - Double.parseDouble(o1.getDateTaken().toString()));
                double timeDif2 = abs(curTime - Double.parseDouble(o2.getDateTaken().toString()));
                Boolean isKarmal = o1.isKarma();
                Boolean isKarma2 = o2.isKarma();
                if (distance2 < distance1 && timeDif2 < timeDif1) {
                    return -1;
                } else if (distance2 < distance1 && timeDif2 > timeDif1) {
                    if (isKarma2 && !isKarmal)
                        return -1;
                }
                return 1;
            }
        };
        Collections.sort(photo,comparator);
    }

    public String[] getPaths(){ //ONLY CALL AFTER FULLY RERANKED!!!
        ArrayList<String> paths = new ArrayList<>();
        for(int i = 0; i<this.photo.size(); i++){
            if(this.photo.get(i).isReleased()) paths.add(this.photo.get(i).getPath());
        } //add only images without released in their fields

        String[] pathArray = paths.toArray(new String[paths.size()]);

        return pathArray;
    }

   public void setMyLocation(String localLat, String localLong){ //TODO get local information
        this.localLat = Double.parseDouble(localLat);
        this.localLng = Double.parseDouble(localLong);
    }
}
