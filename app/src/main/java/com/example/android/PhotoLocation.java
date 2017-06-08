package com.example.android;

import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Justin on 5/11/17.
 * This class gets longitude and latitude values from the EXIF data in each photo
 * The long/lat values are then converted to a city value or street address with geocode
 */
public class PhotoLocation{

    public String locationName;
    String city;
    String address;
    String country;
    String state;
    ExifInterface exif;
    Double Longitude, Latitude;
    String TAG = "PhotoLocation";
    Photo temp;

    PhotoLocation(String path, Geocoder gc, Boolean flag) {
        Log.i("findLocation", this.toString());
        this.locationName = location(path, gc, flag);
    }

    PhotoLocation(String path, Geocoder gc) {
        Log.i("findLocation", this.toString());
        this.locationName = location(path, gc, true);
    }

    private String location(String path, Geocoder gc, Boolean flag) {
        if(Global.displayCycle.get(Global.head).userLocation && flag){
            Log.i("widgetProv", "USERLOCATION SET");
            locationName = Global.displayCycle.get(Global.head).userLocationString;
            return locationName;
        }
        else if(Global.displayCycle.get(Global.head).photoLocation && flag){
            Log.i("widgetProv", "USERLOCATION NOT YET SET");
            locationName = Global.displayCycle.get(Global.head).photoLocationString;
            return locationName;
        }
        else {
            Log.i("widgetProv", "FIRST RUNTHROUGH");

            try {
                Log.i(TAG, "Paths in display cycle: ");
                for (Photo p : Global.displayCycle) {
                    Log.i(TAG, "" + p.getPath());
                }

                for (Photo x : Global.displayCycle) {
                    if (x.getPath().equals(path)) {
                        temp = x;
                        Log.i("EDITLOCATION", "++++++++++++++++++++++++++++++++");
                        Log.i("EDITLOCATION", "Temp path : " + temp.getPath());
                        Log.i("EDITLOCATION", "Photo path : " + x.getPath());
                        Log.i("EDITLOCATION", "Photo bool : " + x.userLocation);
                        Log.i("EDITLOCATION", "Photo loc string : " + x.userLocationString);
                        Log.i("EDITLOCATION", "++++++++++++++++++++++++++++++++");


                    }
                }
                //Exif data is corrupted from copy, use source path instead
                path = tempPath(path);
                exif = new ExifInterface(path);

                Log.i(TAG, "Photo Path: " + path);
                String LAT = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String LAT_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                String LONG = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String LONG_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                Log.i("PhotoLocation", "Latitude: " + LAT);
                Log.i("PhotoLocation", "Longitude: " + LONG);
                Log.i("PhotoLocation", "Latitude ref: " + LAT_REF);
                Log.i("PhotoLocation", "Longitude ref: " + LONG_REF);
                Log.i("PhotoLocation", "IMAGE PATH: " + path);

                //Convert EXIF data to real latitude/longitude values
                if ((LAT != null) && (LAT_REF != null) && (LONG != null) && (LONG_REF != null)) {
                    if (LAT_REF.equals("N")) {
                        Latitude = convertToDegree(LAT);            //Northern Hemisphere
                    } else {
                        Latitude = 0 - convertToDegree(LAT);        //Southern Hemisphere
                    }

                    if (LONG_REF.equals("E")) {
                        Longitude = convertToDegree(LONG);          //Eastern Hemisphere
                    } else {
                        Longitude = 0 - convertToDegree(LONG);      //Western Hemisphere
                    }
                }
                if ((LAT == null) || (LONG == null)) {
                    locationName = "No Location Found";
                }
                Log.i("PhotoLocation", "Latitude: " + Latitude);
                Log.i("PhotoLocation", "Longitude: " + Longitude);
                if (LAT != null && LONG != null) {
                    List<Address> addresses = gc.getFromLocation(Latitude, Longitude, 1);
                    if (addresses.size() > 0) {
                        city = addresses.get(0).getLocality();         //Get city information
                        address = addresses.get(0).getAddressLine(0); //Get street address
                        country = addresses.get(0).getCountryName();
                        state = addresses.get(0).getAdminArea();

                        if (country != null) {
                            if (country.equals("United States")) {
                                country = "USA";
                            }
                        }

                        if (address != null && state != null) {
                            if (address.equals(state)) {
                                address = null;
                            }
                        }

                        if (city != null || address != null || country != null || state != null) {
                            Log.i("photoLocation", address + ", " + city + ", " + state + ", " + country);
                            if (city != null && state != null && country != null) {
                                locationName = city + ", " + state + ", " + country;
                            } else if (city != null && state != null) {
                                locationName = city + ", " + state;
                            } else if (city != null && country != null) {
                                locationName = city + ", " + country;
                            } else if (address != null && city != null && country != null) {
                                locationName = address + ", " + city + ", " + country;
                            } else if (address != null && state != null && country != null) {
                                locationName = address + ", " + state + ", " + country;
                            } else if (city != null && address != null) {
                                locationName = address + ", " + city;
                            } else if (state != null && country != null) {
                                locationName = state + ", " + country;
                            } else if (address != null) {
                                locationName = address;
                            } else {
                                locationName = " ";
                            }
                        } else {
                            locationName = " ";
                        }
                        Log.i("PhotoLocation", "Location: " + locationName);
                    } else {                                                   // No EXIF data available
                        Log.i("PhotoLocation", "No Location Found");
                        locationName = " ";
                    }
                } else {
                    locationName = " ";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            temp.photoLocationString = locationName;
            Log.i("widgetProv", "setting [" + temp.getPath() + "'s] location to: " + locationName);
            temp.photoLocation = true;

            for (Photo x : Global.displayCycle) {
                if (x.getPath().equals(path)) {
                    x = temp;
                    Log.i("EDITLOCATION", "Location String: " + x.userLocationString);
                    Log.i("EDITLOCATION", "Has Location been set?: " + x.userLocation);
                }
            }
            return locationName;
        }
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

    /*
     * This method checks to see if the photo is from the DejaCopy folder. If it is, the exif data
     *  must be retrieved from the source photo.
     */
    private String tempPath(String path){
        String filename = path.substring(path.lastIndexOf("/")+1);

        if(path.contains("DejaCopy")){
            Log.i(TAG, path + " contains 'DejaCopy'");
            path = "/storage/emulated/0/" + filename;
            Log.i(TAG, "New path: " + path);
        }
        else{
            Log.i(TAG, "New path not needed");
        }

        return path;
    }


}
