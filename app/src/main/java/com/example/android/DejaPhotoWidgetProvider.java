package com.example.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dejaphoto.R;

/**
 * Created by Justin on 5/3/17.
 * This class uses android APIs to create a widget that is responsive to presses on buttons
 */

public class DejaPhotoWidgetProvider extends AppWidgetProvider {
    //ACTIONS
    private static final String ACTION_KARMA = "com.example.android.KARMA";
    private static final String ACTION_RELEASE = "com.example.android.RELEASE";
    private static final String ACTION_PREVIOUS = "com.example.android.PREVIOUS";
    private static final String ACTION_NEXT = "com.example.android.NEXT";
    public static String PREVIOUS_PIC = "Previous Picture";
    public static String KARMA_BUTTON = "Karma Added";
    public static String RELEASE_BUTTON = "Picture Released";
    public static String NEXT_PIC = "Next Picture";
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

        Intent changeIntent = new Intent(context, ChangeImage.class);

        boolean changePicture = false, buttonPressed = false;
        //needed to prevent crash, auto changer override

        if (intent.getAction().equals(PREVIOUS_PIC)) {
            Toast.makeText(context, PREVIOUS_PIC, Toast.LENGTH_SHORT).show();
            changeIntent.setAction(ACTION_PREVIOUS);
            if(Global.currIndex == 0) Global.currIndex = Global.displayCycle.size() - 1;
            else Global.currIndex = Global.currIndex - 1;
            changePicture = true;
            manageTimer(context); //reset undoTimer


        } else if (intent.getAction().equals(KARMA_BUTTON)) {
            manageTimer(context);
            undoManager(context, "karma");
            /*for(Photo p : Global.displayCycle){
            }*/
        } else if (intent.getAction().equals(RELEASE_BUTTON)) {
            manageTimer(context);
            undoManager(context, "release");

        } else if (intent.getAction().equals(NEXT_PIC)) {
            manageTimer(context);
            Toast.makeText(context, NEXT_PIC, Toast.LENGTH_SHORT).show();
            changeIntent.setAction(ACTION_NEXT);
            if(Global.currIndex == Global.displayCycle.size()) Global.currIndex = 0;
            else Global.currIndex = Global.currIndex + 1;
            changePicture = true;

        }

        if (changePicture) context.startService(changeIntent); //call widgetmanager
    }


    public void undoManager(Context context, String action){
        this.karmaAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.releaseAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        intentKarma = new Intent(context, AlarmReceiver.class);
        intentRelease = new Intent(context, AlarmReceiver.class);


        if(!Global.undoKarmaOn && action.equals("karma")){ //check to see if the alarmmanager returns a object or null (whether alarm is set)
            intentKarma.setAction(ACTION_KARMA);
            Global.karmaPath = getPath();

            this.karmaPI = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
            karmaAlarm.setExact(AlarmManager.ELAPSED_REALTIME, 15000, karmaPI); // Millisec * Second * Minute

            Global.undoKarmaOn = true; //set the alarm

            Toast.makeText(context, "Click Karma again to undo", Toast.LENGTH_LONG).show();
        }

        else if(!Global.undoReleaseOn && action.equals("release") ){
            intentRelease.setAction(ACTION_RELEASE);
            Global.releasePath = getPath();

            releasePI = PendingIntent.getBroadcast(context, 0, intentRelease, 0);
            releaseAlarm.setExact(AlarmManager.ELAPSED_REALTIME, 15000, releasePI); // Millisec * Second * Minute

            Global.undoReleaseOn = true;

            if(Global.undoKarmaOn){
                if(karmaPI != null) {
                    karmaAlarm.cancel(karmaPI);
                    karmaPI.cancel();
                }
                Global.undoKarmaOn = false; //switch karma alarm off
            }

            Toast.makeText(context, "Click Release again to undo", Toast.LENGTH_LONG).show();
        }

        else if (Global.undoKarmaOn && action.equals("karma") ){ //when the currUser presses button a second time before the alarm undoTimer runs out
            Log.i("undoManager", "alarmKarma : " +Global.undoKarmaOn);

            if(karmaPI != null) {
                karmaAlarm.cancel(karmaPI); // Millisec * Second * Minute
                karmaPI.cancel();
                intentKarma.setAction("");
            }

            Global.undoKarmaOn = false; //switch karma alarm off
            Toast.makeText(context, "Undo Successful", Toast.LENGTH_SHORT).show();
        }

        else if(Global.undoReleaseOn && action.equals("release")){ //release alarm on
            Log.i("undoManager", "alarmRelease : " + Global.undoReleaseOn);

            if(releasePI != null) {
                releaseAlarm.cancel(releasePI); // Millisec * Second * Minute
                releasePI.cancel();
                intentRelease.setAction("");
            }

            Global.undoReleaseOn = false; //switch release alarm off
            Toast.makeText(context, "Undo Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(){
        Photo photo = Global.displayCycle.get(Global.head);
        return photo.getPath();
    }

    public void manageTimer(Context context) { //called when button is clicked
        Log.i("updateInterval", "Change Interval: " + Global.changeInterval);
        if(Global.undoTimer != null) {
            Global.autoWallpaperChange.cancel();
            Global.autoWallpaperChange = new AutoWallpaperChangeTask(context);
            Global.undoTimer.schedule(Global.autoWallpaperChange,
                    Global.changeInterval, Global.changeInterval);
        }
    }
}
