package com.example.android;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


/**
 * Created by Justin on 5/11/17.
 * This class gets longitude and latitude values from the EXIF data in each photo
 * The long/lat values are then converted to a city value or street address with geocode
 */


public class PhotoLocation{

    public String locationName;
    ExifInterface exif;
    Double Longitude, Latitude;

    PhotoLocation(String path, Geocoder gc) {
        Log.i("findLocation", this.toString());
        this.locationName = location(path, gc);
    }

    private String location(String path, Geocoder gc) {
        try {
            exif = new ExifInterface(path);
            String LAT = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String LAT_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String LONG = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String LONG_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            Log.i("PhotoLocation", "Latitude: " + LAT);
            Log.i("PhotoLocation", "Longitude: "+LONG);
            Log.i("PhotoLocation", "Latitude ref: " + LAT_REF);
            Log.i("PhotoLocation", "Longitude ref: "+LONG_REF);

            //Convert EXIF data to real latitude/longitude values
            if((LAT !=null) && (LAT_REF !=null) && (LONG != null) && (LONG_REF !=null)) {
                if(LAT_REF.equals("N")){
                    Latitude = convertToDegree(LAT);            //Northern Hemisphere
                }
                else{
                    Latitude = 0 - convertToDegree(LAT);        //Southern Hemisphere
                }

                if(LONG_REF.equals("E")){
                    Longitude = convertToDegree(LONG);          //Eastern Hemisphere
                }
                else{
                    Longitude = 0 - convertToDegree(LONG);      //Western Hemisphere
                }
            }
            if((LAT == null) || (LONG==null)){
                locationName = "No Location Found";
            }
            Log.i("PhotoLocation", "Latitude: " + Latitude);
            Log.i("PhotoLocation", "Longitude: " + Longitude);
            List<Address> addresses = gc.getFromLocation(Latitude, Longitude, 1);
            if (addresses.size() > 0) {
                locationName = addresses.get(0).getLocality();         //Get city information
                if(locationName == null){                              //If no city info available
                    locationName = addresses.get(0).getAddressLine(0); //Get street address
                }
                if(locationName == null){                              //still no location info found
                    locationName = "No Location Found";
                }
                Log.i("PhotoLocation", "Location: " + locationName);
            } else {                                                   // No EXIF data available
                Log.i("PhotoLocation", "No Location Found");
                locationName = "No Location Found";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return locationName;
    }

    //Converts exif long lat format to real long lat values
    private Double convertToDegree(String value){
        Double result;
        String[] DMS = value.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Double(FloatD + (FloatM/60) + (FloatS/3600));

        return result;
    }
}
