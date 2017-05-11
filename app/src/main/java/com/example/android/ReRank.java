package com.example.android;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class ReRank {
    int length = 0;
    double localLat;
    double localLng;
    String weekDay;
    double myTime;
    private ArrayList<Photo> photo = new ArrayList<>();
    //TODO use this as the pictures that are populated with information


    ReRank(DisplayCycle dc) {
        this.dc = dc;
        length = dc.cycleLength;

        /*amanda */
//        MyLocation myLocation = new MyLocation();
//        localLat = myLocation.getLat();
//        localLng = myLocation.getLng();

        Date myDate = new Date(0);
        myTime = myDate.getTime();
        weekDay = "Sunday";
    }

    public void sort() {
        String path1="";
        double distance1 = 0;
        double distance2 = 0;
        double timeDif1 = 0;
        double timeDif2 = 0;
        boolean isKarma1 = false;
        boolean isKarma2 = false;
        double time = 0;
        double photoLat = 0;
        double photoLng = 0;
        double[] latLng = {0, 0};
        ImageNode cur = new ImageNode(null, null, null);
        ImageNode priority = new ImageNode(null, null, null);
        priority = dc.first;
        /*  photo prioritest     */
        GetLatAndLng getLatAndLng = new GetLatAndLng();
        latLng = getLatAndLng.getlocation(path1);
        photoLat = latLng[0];
        photoLng = latLng[1];
        distance1 = sqrt(pow((localLat - photoLat), 2) + pow((localLng - photoLng), 2));

                /*assume we know the photo token time is varible time
                * we do not sure the return typle of the time function*/
        timeDif1 = abs(myTime - time);

                /*assume we know the iskarma value*/

        for (int i = 0; i <= length - 1; i++) {
            while (cur != dc.last) {

//                /*  photo 1     */
//                GetLatAndLng getLatAndLng = new GetLatAndLng();
//                latLng = getLatAndLng.getlocation(String path1);
//                photoLat = latLng[0];
//                photoLng = latLng[1];
//                distance1 = sqrt(pow((localLat - photoLat), 2) + pow((localLng - photoLng), 2));
//
//                /*assume we know the photo token time is varible time
//                * we do not sure the return typle of the time function*/
//                timeDif1 = abs(myTime - time);
//
//                /*assume we know the iskarma value*/

                cur = cur.next;
                  /*  photo 2    */
                //GetLatAndLng getLatAndLng = new GetLatAndLng();
                String path2="";
                latLng = getLatAndLng.getlocation(path2);
                photoLat = latLng[0];
                photoLng = latLng[1];
                distance2 = sqrt(pow((localLat - photoLat), 2) + pow((localLng - photoLng), 2));

                /*assume we know the photo token time is varible time
                * we do not sure the return typle of the time function*/
                timeDif2 = abs(myTime - time);

                /*assume we know the iskarma value*/

                if (distance2 < distance1 && timeDif2 < timeDif1) {
                    priority = cur;
                    distance1 = distance2;
                    timeDif1 = timeDif2;
                    isKarma1 = isKarma2;
                } else if (distance2 < distance1 && timeDif2 > timeDif1) {
                    if (isKarma2 && !isKarma1)
                        priority = cur;
                    distance1 = distance2;
                    timeDif1 = timeDif2;
                    isKarma1 = isKarma2;
                }

            }
            ImageNode temp = new ImageNode(null,null,null);
            temp = priority;
            priority = dc.first;
            dc.first = temp;
    }
}

        public void gatherCycleInfo(){
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media.DATA}; //which columns we will get (all in this case)
            Cursor cr = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

            Log.i("BuildCycle", "uri to access"+uri.toString());
            Log.i("BuildCycle", "name, cr.count "+cr.getColumnName(0)+cr.getCount());

        /*
        * query(uri,             // The content URI of the images
        * projection,            // The columns to return for each row (each diff image is new row)
        * null,                 //selection criteria
        * null,                 //selection criteria
        * null                  // The sort order for the returned rows
        */

            int picNum=-1;

            if(null == cr) {
                Log.i("Rerank", "ERROR null==cr in BuildDisplayCycle");
            }else if( cr.getCount()<1) {
                Log.i("Rerank", "NO IMAGES PRESENT");
            } else { //handle returned data
                Log.i("Rerank", "IMAGES PRESENT");
                cr.moveToFirst();

                int[] columns = {cr.getColumnIndex(MediaStore.MediaColumns.DATA),
                cr.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION),
                cr.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN),
                cr.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE),
                cr.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)};

                picNum = 0;
                while(cr.moveToNext()) { //go through all the images
                /*String released = cr.getString(description);
                if(released == "released") continue; //read release from image description
                */

                Photo photo = new Photo(cr.getString(columns[0]), cr.getString(columns[0]), cr.getString(columns[0]),
                        cr.getString(columns[0]),cr.getString(columns[0]));

                    Log.i("Rerank", "Completion of photo object");
                }
            }

            if (cr != null) {
                cr.close();
            }
        }


}
