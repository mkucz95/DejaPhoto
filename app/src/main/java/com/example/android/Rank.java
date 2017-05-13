package com.example.android;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class Rank {
    private double localLat;
    private double localLng;

    /*we still not get the two varible value*/
    boolean isLocaOn = true;
    boolean isTimeOn = true;

    private ArrayList<Photo> photo = new ArrayList<>();
    //each photo is populated with all the information we need
    private boolean[] settings; //location, time, day, karma


    public Rank(ArrayList<Photo> list, boolean[] settings, String localLat, String localLong) {
        this.photo = list;
        this.settings = settings;
        setMyLocation(localLat, localLong);
        sort(); //sort the array list
    }

    public void sort() {

        Photo temp = new Photo();
        long curMiliSecond = System.currentTimeMillis();
        long hour = Long.parseLong(getHour(curMiliSecond));
        SimpleDateFormat sdf1 = new SimpleDateFormat();
        
        
        java.sql.Date date = new java.sql.Date(curMiliSecond);                         //long to Date type
        String dayOfWeek = getWeekOfDate(date);              // Date type to  day of week
        int dayInt = week(dayOfWeek);                       //the integer for the day of week
        
        int dayIntDiff1 = 0;      //dayPhoto1 compare to dayInt
        int dayIntDiff2 = 0;

        for(int i = 0 ;i<photo.size() -1 ;i++) {
            for (int j = 1; j < photo.size() - 1; j++) {


                double photo1Lat = Double.parseDouble(photo.get(j - 1).getLatLong().toString());
                double photo1Lng = Double.parseDouble(photo.get(j - 1).getLatLong().toString());
                double photo2Lat = Double.parseDouble(photo.get(j).getLatLong().toString());
                double photo2Lng = Double.parseDouble(photo.get(j).getLatLong().toString());
                int dayPhoto1 = week(photo.get(j - 1).getDayOfWeek());       //photo1 's interger for the day of week
                int dayPhoto2 = week(photo.get(j).getDateTaken());


                double distance1 = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));
                double distance2 = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));

                /*find the real different on the day of the week*/
                if (abs(dayPhoto1 - dayInt) > 3)
                    dayIntDiff1 = 7 - abs(dayPhoto1 - dayInt);
                else dayIntDiff1 = abs(dayPhoto1 - dayInt);

                if (abs(dayPhoto2 - dayInt) > 3)
                    dayIntDiff1 = 7 - abs(dayPhoto2 - dayInt);
                else dayIntDiff2 = abs(dayPhoto2 - dayInt);


               // location off  and time on
                if (!isLocaOn && isTimeOn) {

                    /*day of the week equal*/
                    if (dayIntDiff1 == dayIntDiff2) {
                        if (abs(abs(photo.get(j - 1).getHour() - hour) - 2) > abs(photo.get(j).getHour() - hour)) {
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);
                        } else if (!photo.get(j - 1).isKarma() && photo.get(j).isKarma()) {
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);
                        }
                    }
                    /*day of the week differenct*/
                    else if (dayIntDiff1 > dayIntDiff2)
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                }


                //location on and time off
                else if (isLocaOn && !isTimeOn) {
                    //both photo take out of 1000 curcle
                    if (distance1 > 1000 && distance2 > 1000 && distance1 > distance2) {
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                        //photo1 is out of the curcle
                    else if (distance1 > 1000 && distance2 <= 1000) {
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                        //both photos are in the circle
                    else if (distance1 <= 1000 && distance2 <= 1000 && !photo.get(j-1).isKarma() && photo.get(j).isKarma()) {
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }
                }

                //both off
                else if (!isLocaOn && !isTimeOn && !photo.get(j-1).isKarma() && photo.get(j).isKarma()) {
                    temp = photo.get(j - 1);
                    photo.set((j - 1), photo.get(j));
                    photo.set(j, temp);
                }


                    //both on
                    //photo1 and photo2 both are in the circle like the photo location switch off
                else if (distance1 < 1000 && distance2 < 1000) {
                    if (dayIntDiff1 == dayIntDiff2) {
                        if (abs(abs(photo.get(j-1).getHour() - hour) - 2) > abs(photo.get(j).getHour() - hour)) {
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);
                        } else if (!photo.get(j-1).isKarma() && photo.get(j).isKarma()) {
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);
                        }

                    }
                    /*day of the week differenct*/
                    else if (dayIntDiff1 > dayIntDiff2) {
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }
                }

                //photo1 is in the circle and photo 2 is out of the circle
                else if (distance1 < 1000 && distance2 > 1000) {
                    //depend of the day of the week and karma situation
                    if (dayIntDiff1 > dayIntDiff2 && !photo.get(j-1).isKarma() && photo.get(j).isKarma()){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                    else if (dayIntDiff1 == dayIntDiff2 && !photo.get(j-1).isKarma() &&
                            photo.get(j).isKarma() && abs(abs(photo.get(j-1).getHour() - hour) - 2) >
                            abs(photo.get(j).getHour() - hour)){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                }

                //photo 1 is out of the circle ,and photo 2 is in the circle
                else if (distance1 > 1000 && distance2 < 1000) {

                    //photo2 time differece is smaller
                    if (dayIntDiff1 >= dayIntDiff2){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }
                    else if (dayIntDiff1 == dayIntDiff2 && abs(abs(photo.get(j-1).getHour() - hour) - 2)
                            >= abs(photo.get(j).getHour() - hour)){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                        //photo2 time diff is bigger but karmaa true, photoe1 karma false
                    else if (dayIntDiff1 < dayIntDiff2 || (dayIntDiff1 == dayIntDiff2 && abs(abs(photo.get(j-1).getHour() - hour) - 2) < abs(photo.get(j).getHour() - hour))
                            && !photo.get(j-1).isKarma() && photo.get(j).isKarma()){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);

                    }


                }

                //photo 1 and photo 2 are in the circle
                else if (distance1 > 1000 && distance2 > 1000) {

                    //photo2 close
                    if (distance1 > distance2) {

                        //photo2 time distance is smaller or equal
                        if (dayIntDiff1 >= dayIntDiff2 || (dayIntDiff1 == dayIntDiff2 &&
                                abs(abs(photo.get(j-1).getHour() - hour) - 2) >=
                                        abs(photo.get(j).getHour() - hour))){
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);
                        }

                            //photo 2 time distance is bigger  depend on karma
                        else if (dayIntDiff1 < dayIntDiff2 && !photo.get(j-1).isKarma() && photo.get(j).isKarma() ||
                                (dayIntDiff1 == dayIntDiff2 && abs(abs(photo.get(j-1).getHour() - hour) - 2)
                                        < abs(photo.get(j).getHour() - hour)) && !photo.get(j-1).isKarma()
                                        && photo.get(j).isKarma()){
                            temp = photo.get(j - 1);
                            photo.set((j - 1), photo.get(j));
                            photo.set(j, temp);

                        }

                    }
                }

                //photo 2 is far
                else if (distance1 < distance2) {

                    //photo 2 time diff is small and o2.karma, o1,not karma
                    if (dayIntDiff1 > dayIntDiff2 && !photo.get(j-1).isKarma() && photo.get(j).isKarma() ||
                            (dayIntDiff1 == dayIntDiff2 && abs(abs(photo.get(j-1).getHour() - hour) - 2) >
                                    abs(photo.get(j).getHour() - hour)) && !photo.get(j-1).isKarma() &&
                                    photo.get(j).isKarma()){
                        temp = photo.get(j - 1);
                        photo.set((j - 1), photo.get(j));
                        photo.set(j, temp);
                    }

                }

            }



            }
        }

        

//        /*setting sort interface Conections's Comparator*/
//        Comparator<Photo> comparator = new Comparator<Photo>() {
//            @Override
//            public int compare(Photo o1, Photo o2) {
//                long curMiliSecond = System.currentTimeMillis();
//                long hour = Long.parseLong(getHour(curMiliSecond));
//                SimpleDateFormat sdf1 = new SimpleDateFormat();
//
//                java.sql.Date date = new java.sql.Date(curMiliSecond);                         //long to Date type
//                String dayOfWeek = getWeekOfDate(date);              // Date type to  day of week
//                int dayInt = week(dayOfWeek);                       //the integer for the day of week
//
//
//                double photo1Lat = Double.parseDouble(o1.getLatLong().toString());
//                double photo1Lng = Double.parseDouble(o1.getLatLong().toString());
//                double photo2Lat = Double.parseDouble(o2.getLatLong().toString());
//                double photo2Lng = Double.parseDouble(o2.getLatLong().toString());
//                int dayPhoto1 = week(o1.getDayOfWeek());       //photo1 's interger for the day of week
//                int dayPhoto2 = week(o2.getDateTaken());
//                int dayIntDiff1 = 0;      //dayPhoto1 compare to dayInt
//                int dayIntDiff2 = 0;
//
//                double distance1 = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));
//                double distance2 = sqrt(pow((localLat - photo1Lat), 2) + pow((localLng - photo1Lng), 2));
//
//                /*find the real different on the day of the week*/
//                if (abs(dayPhoto1 - dayInt) > 3)
//                    dayIntDiff1 = 7 - abs(dayPhoto1 - dayInt);
//                else dayIntDiff1 = abs(dayPhoto1 - dayInt);
//
//                if (abs(dayPhoto2 - dayInt) > 3)
//                    dayIntDiff1 = 7 - abs(dayPhoto2 - dayInt);
//                else dayIntDiff2 = abs(dayPhoto2 - dayInt);
//
//
//                //location off  and time on
//                if (!isLocaOn && isTimeOn) {
//
//                    /*day of the week equal*/
//                    if (dayIntDiff1 == dayIntDiff2) {
//                        if (abs(abs(o1.getHour() - hour) - 2) > abs(o2.getHour() - hour)) {
//                            return -1;
//                        } else if (!o1.isKarma() && o2.isKarma())
//                            return -1;
//                        else
//                            return 0;
//                    }
//                    /*day of the week differenct*/
//                    else if (dayIntDiff1 > dayIntDiff2)
//                        return -1;
//                }
//
//
//                //location on and time off
//                else if (isLocaOn && !isTimeOn) {
//                    //both photo take out of 1000 curcle
//                    if (distance1 > 1000 && distance2 > 1000 && distance1 > distance2)
//                        return -1;
//
//                        //photo1 is out of the curcle
//                    else if (distance1 > 1000 && distance2 <= 1000) {
//                        return -1;
//                    }
//
//                        //both photos are in the circle
//                    else if (distance1 <= 1000 && distance2 <= 1000 && !o1.isKarma() && o2.isKarma())
//                        return -1;
//                    else
//                        return 0;
//                }
//
//                //both off
//                else if (!isLocaOn && !isTimeOn && !o1.isKarma() && o2.isKarma())
//                    return -1;
//
//
//                    //both on
//                    //photo1 and photo2 both are in the circle like the photo location switch off
//                else if (distance1 < 1000 && distance2 < 1000) {
//                    if (dayIntDiff1 == dayIntDiff2) {
//                        if (abs(abs(o1.getHour() - hour) - 2) > abs(o2.getHour() - hour)) {
//                            return -1;
//                        } else if (!o1.isKarma() && o2.isKarma())
//                            return -1;
//                        else
//                            return 0;
//                    }
//                    /*day of the week differenct*/
//                    else if (dayIntDiff1 > dayIntDiff2)
//                        return -1;
//                }
//
//                //photo1 is in the circle and photo 2 is out of the circle
//                else if (distance1 < 1000 && distance2 > 1000) {
//                    //depend of the day of the week and karma situation
//                    if (dayIntDiff1 > dayIntDiff2 && !o1.isKarma() && o2.isKarma())
//                        return -1;//abs(abs(o1.getHour() - hour) - 2) > abs(o2.getHour() - hour))
//
//                    else if (dayIntDiff1 == dayIntDiff2 && !o1.isKarma() && o2.isKarma() && abs(abs(o1.getHour() - hour) - 2) > abs(o2.getHour() - hour))
//                        return -1;
//                    else
//                        return 0;
//                }
//
//                //photo 1 is out of the circle ,and photo 2 is in the circle
//                else if (distance1 > 1000 && distance2 < 1000) {
//
//                    //photo2 time differece is smaller
//                    if (dayIntDiff1 >= dayIntDiff2)
//                        return -1;
//                    else if (dayIntDiff1 == dayIntDiff2 && abs(abs(o1.getHour() - hour) - 2) >= abs(o2.getHour() - hour))
//                        return -1;
//
//                        //photo2 time diff is bigger but karmaa true, photoe1 karma false
//                    else if (dayIntDiff1 < dayIntDiff2 || (dayIntDiff1 == dayIntDiff2 && abs(abs(o1.getHour() - hour) - 2) < abs(o2.getHour() - hour))
//                            && !o1.isKarma() && o2.isKarma())
//                        return -1;
//                    else return 0;
//
//                }
//
//                //photo 1 and photo 2 are in the circle
//                else if (distance1 > 1000 && distance2 > 1000) {
//
//                    //photo2 close
//                    if (distance1 > distance2) {
//
//                        //photo2 time distance is smaller or equal
//                        if (dayIntDiff1 >= dayIntDiff2 || (dayIntDiff1 == dayIntDiff2 && abs(abs(o1.getHour() - hour) - 2) >= abs(o2.getHour() - hour)))
//                            return -1;
//
//                            //photo 2 time distance is bigger  depend on karma
//                        else if (dayIntDiff1 < dayIntDiff2 && !o1.isKarma() && o2.isKarma() ||
//                                (dayIntDiff1 == dayIntDiff2 && abs(abs(o1.getHour() - hour) - 2) < abs(o2.getHour() - hour)) && !o1.isKarma() && o2.isKarma())
//                            return -1;
//                        else
//                            return 0;
//                    }
//                }
//
//                //photo 2 is far
//                else if (distance1 < distance2) {
//
//                    //photo 2 time diff is small and o2.karma, o1,not karma
//                    if (dayIntDiff1 > dayIntDiff2 && !o1.isKarma() && o2.isKarma() ||
//                            (dayIntDiff1 == dayIntDiff2 && abs(abs(o1.getHour() - hour) - 2) > abs(o2.getHour() - hour)) && !o1.isKarma() && o2.isKarma())
//                        return -1;
//                    else
//                        return 0;
//                }
//                return 0;
//            }
//
//
//        };
//        Collections.sort(photo, comparator);
  //  }

    public String[] getPaths() { //ONLY CALL AFTER FULLY RERANKED!!!
        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < this.photo.size(); i++) {
            if (this.photo.get(i).isReleased()) paths.add(this.photo.get(i).getPath());
        } //add only images without released in their fields

        String[] pathArray = paths.toArray(new String[paths.size()]);

        return pathArray;
    }

    public void setMyLocation(String localLat, String localLong) {
        this.localLat = Double.parseDouble(localLat);
        this.localLng = Double.parseDouble(localLong);
    }


    //long time from 1970 transfer day of week
    public static String getWeekOfDate(java.sql.Date dt) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saterday"};

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getHour(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(l);

    }


    //day of week -> int for compare
    public static int week(String s) {
        if (s.equals("Sunday"))
            return 0;
        else if (s.equals("Monday"))
            return 1;
        else if (s.equals("Tuesday"))
            return 2;
        else if (s.equals("Wednsday"))
            return 3;
        else if (s.equals("Thursday"))
            return 4;
        else if (s.equals("Friday"))
            return 5;
        else
            return 6;

    }
}