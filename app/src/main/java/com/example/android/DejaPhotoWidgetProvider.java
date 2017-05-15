package com.example.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dejaphoto.R;

/**
 * Created by Justin on 5/3/17.
 * This class uses android APIs to create a widget that is responsive to presses on buttons
 */

public class DejaPhotoWidgetProvider extends AppWidgetProvider {
    public static String PREVIOUS_PIC = "Previous Picture";
    public static String KARMA_BUTTON = "Karma Added";
    public static String RELEASE_BUTTON = "Picture Released";
    public static String NEXT_PIC = "Next Picture";
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.RELEASE";

    AlarmManager karmaAlarm;
    AlarmManager releaseAlarm;
    PendingIntent karmaPI;
    PendingIntent releasePI;

    Intent intentKarma;
    Intent intentRelease;




    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;


        for(int i=0; i<N; i++){ //
            int appWidgetId = appWidgetIds[i];

            Intent intentPrev = new Intent(context, DejaPhotoWidgetProvider.class);
            intentPrev.setAction(PREVIOUS_PIC); //define action to take when previous is pressed

            Intent intentKarma = new Intent(context, DejaPhotoWidgetProvider.class);
            intentKarma.setAction(KARMA_BUTTON);//define action to take when karma is pressed

            Intent intentRelease = new Intent(context, DejaPhotoWidgetProvider.class);
            intentRelease.setAction(RELEASE_BUTTON);//define action to take when release is pressed

            Intent intentNext = new Intent(context, DejaPhotoWidgetProvider.class);
            intentNext.setAction(NEXT_PIC);//define action to take when next is pressed


            //create pending intent ready to act
            PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
            PendingIntent pendingIntentKarma = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
            PendingIntent pendingIntentRelease = PendingIntent.getBroadcast(context, 0, intentRelease, 0);
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);

            //get layout for our widget, give each button on-click listener
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dejaphoto_appwidget_layout);
            views.setOnClickPendingIntent(R.id.previous_pic, pendingIntentPrev);
            views.setOnClickPendingIntent(R.id.karma_btn, pendingIntentKarma);
            views.setOnClickPendingIntent(R.id.release_btn, pendingIntentRelease);
            views.setOnClickPendingIntent(R.id.next_pic, pendingIntentNext);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String pressed = "button_pressed";
        Intent clickIntent = new Intent(context, WidgetManager.class);


        //Intent restartTimer = new Intent(context, AutoChanger.class);

        clickIntent.setAction(Intent.ACTION_SEND);
        clickIntent.setType("text/plain");

        boolean changePicture = false; //needed to prevent crash
        boolean actionNeeded = false;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dejaphoto_appwidget_layout);

        if (intent.getAction().equals(PREVIOUS_PIC)) {

            Toast.makeText(context, PREVIOUS_PIC, Toast.LENGTH_SHORT).show();
            clickIntent.putExtra(pressed, "previous");
            changePicture = true;

        } else if (intent.getAction().equals(KARMA_BUTTON)) {
            undoManager(context, "karma");
            actionNeeded = true;

        } else if (intent.getAction().equals(RELEASE_BUTTON)) {
            undoManager(context, "release");
            actionNeeded = true;

        } else if (intent.getAction().equals(NEXT_PIC)) {
            Toast.makeText(context, NEXT_PIC, Toast.LENGTH_SHORT).show();
            clickIntent.putExtra(pressed, "next");
            changePicture = true;
        }

        if (changePicture) context.startService(clickIntent); //call widgetmanager
        //if(changePicture || actionNeeded) context.startService(restartTimer);
    }



    public void undoManager(Context context, String action){
        this.karmaAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.releaseAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        intentKarma = new Intent(context, AlarmReceiver.class);
        intentKarma.setAction(Intent.ACTION_SEND);
        intentKarma.setType("text/plain");

        intentRelease = new Intent(context, AlarmReceiver.class);
        intentRelease.setAction(Intent.ACTION_SEND);
        intentRelease.setType("text/plain");


        if(!getAlarm("karma", context) && action.equals("karma")){ //check to see if the alarmmanager returns a object or null (whether alarm is set)
            //Log.i("undoManager", (PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE)).toString());
            intentKarma.setAction(ACTION_KARMA);

            this.karmaPI = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
            karmaAlarm.setExact(AlarmManager.ELAPSED_REALTIME, 15000, karmaPI); // Millisec * Second * Minute

            setPath("karma", context);

            setAlarm("karma", context, true); //switch alarm on

            Toast.makeText(context, "Click Karma again to undo", Toast.LENGTH_LONG).show();

        }

        else if(!getAlarm("release", context)  && action.equals("release") ){
            intentRelease.setAction(ACTION_RELEASE);
            releasePI = PendingIntent.getBroadcast(context, 0, intentRelease, 0);
            releaseAlarm.setExact(AlarmManager.ELAPSED_REALTIME, 15000, releasePI); // Millisec * Second * Minute

            setPath("release", context);

            setAlarm("release", context, true);

            if(getAlarm("karma", context)){
                if(karmaPI != null) {
                    karmaAlarm.cancel(karmaPI);
                    karmaPI.cancel();
                }
                setAlarm("karma", context, false);
            }

            Toast.makeText(context, "Click Release again to undo", Toast.LENGTH_LONG).show();
        }

        else if (getAlarm("karma", context) && action.equals("karma") ){ //when the user presses button a second time before the alarm timer runs out
            //alarm.cancelAlarm(context);
            Log.i("undoManager", "alarmKarma : " + getAlarm("karma", context));


            if(karmaPI != null) {
                karmaAlarm.cancel(karmaPI); // Millisec * Second * Minute
                karmaPI.cancel();
                intentKarma.setAction("");
            }

            setAlarm("karma", context, false);
            Toast.makeText(context, "Undo Successful", Toast.LENGTH_SHORT).show();
        }

        else if(getAlarm("release", context) && action.equals("release")){ //release alarm on
            Log.i("undoManager", "alarmRelease : " + getAlarm("release", context));

            if(releasePI != null) {
                releaseAlarm.cancel(releasePI); // Millisec * Second * Minute
                releasePI.cancel();
                intentRelease.setAction("");
            }

            setAlarm("release", context, false);
            Toast.makeText(context, "Undo Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getAlarm(String type, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(type, false);
    }

    public void setAlarm(String type, Context context, boolean pref){
        SharedPreferences sharedPref = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(type, pref);
        editor.apply();
    }

    public void setPath(String type, Context context){
        SharedPreferences headPref =  context.getSharedPreferences("head", Context.MODE_PRIVATE);
        int head = headPref.getInt("head", 0);

        SharedPreferences dispCycle =  context.getSharedPreferences("display_cycle", Context.MODE_PRIVATE);
        String currpath = dispCycle.getString(Integer.toString(head), "");

        SharedPreferences cycleAction = context.getSharedPreferences("cycleAction", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = cycleAction.edit();
        edit.putString(type, currpath);

        edit.apply();
    }

}
