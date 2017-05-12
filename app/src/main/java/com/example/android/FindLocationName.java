package com.example.android;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.example.dejaphoto.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by wl36901 on 2017/5/8.
 * get the address name by the latitude and logtitude from class GetLatAndLng
 */

public class FindLocationName extends WallpaperChanger{

    /**
     * Created by wl36901 on 2017/5/8.
     */
        //double [] position = (path);
        private String path;  //picture's path, if we collect all the informatino to the sql, we could
        //get the information from the sql table
        public String locationName;//encode the location informaiton to country, city, street

        FindLocationName(String path) {
            this.path = path;
            Log.i("findLocation", this.toString());
            this.locationName = findLocation();
        }

         String findLocation() {
             Log.i("findLocation", this.toString());
            Geocoder gc = new Geocoder(this.getApplicationContext(), Locale.getDefault());//Locale.getDefault()follow the system's language
            List<Address> locationList = null;

             try
             {
                GetLatAndLng getLatAndLng = new GetLatAndLng();
                double[] latlng = getLatAndLng.getlocation(path);//by the method to get the latitude and logitude
                locationList = gc.getFromLocation(latlng[0], latlng[2], 1);
             }
             catch (IOException e)
             {
                e.printStackTrace();
             }

            Address address = locationList.get(0);//得到Address实例
            //Log.i(TAG, "address =" + address);
            String countryName = address.getCountryName();//country name
            Log.i(TAG, "countryName = " + countryName);
            String locality = address.getLocality();//city name
            String addressLine = "";
            Log.i(TAG, "locality = " + locality);
            for (
                    int i = 0; address.getAddressLine(i) != null; i++)

            {
                addressLine = address.getAddressLine(i);//steet name
                Log.i(TAG, "addressLine = " + addressLine);
            }
            locationName = addressLine + " " + countryName + ", " + countryName;
             return locationName;
        }


    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}
