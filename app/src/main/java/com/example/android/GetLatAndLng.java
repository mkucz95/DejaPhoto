package com.example.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;

/**
 * Created by wl36901 on 2017/5/7.
 * by input the path of picture to get the latitude and logtitude
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;



/**
 * Created by wl36901 on 2017/5/7.
 */

public class GetLatAndLng {
    public GetLatAndLng() {
        super();
    }
    /**
     path 为照片的路径
     */
    public  double[] getlocation(String path){
        float output1 = 0;
        float output2 = 0;
        @SuppressWarnings("unused")
        String context ;
        Location location;
        try {
            ExifInterface exifInterface=new ExifInterface(path);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String lngRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                try {
                    output1 = convertRationalLatLonToFloat(latValue, latRef);
                    output2= convertRationalLatLonToFloat(lngValue, lngRef);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        context = Context.LOCATION_SERVICE;
        location=new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(output1);
        location.setLongitude(output2);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double[] f={lat,lng};
         return f;
    }

    private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {
        try {
            String [] parts = rationalString.split(",");

            String [] pair;
            pair = parts[0].split("/");
            double degrees = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[1].split("/");
            double minutes = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[2].split("/");
            double seconds = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
            if ((ref.equals("S") || ref.equals("W"))) {
                return (float) -result;
            }
            return (float) result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }
}
