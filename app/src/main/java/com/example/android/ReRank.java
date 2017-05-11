package com.example.android;


import java.util.Date;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

/**
 * Created by wl36901 on 2017/5/11.
 */

public class ReRank {
    int length = 0;
    double localLat;
    double localLng;
    String weekDay;
    double myTime;
    private DisplayCycle dc;

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
        latLng = getLatAndLng.getlocation(String path1);
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
                latLng = getLatAndLng.getlocation(String path2);
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


}
