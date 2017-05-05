package com.example.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.justin.dejaphoto.R;

/**
 * Created by Justin on 5/3/17.
 */

public class DejaPhotoWidgetProvider extends AppWidgetProvider {
    public static String PREVIOUS_PIC = "Pressed Back Button";
    public static String KARMA_BUTTON = "Pressed Karma Button";
    public static String RELEASE_BUTTON = "Pressed Release Button";
    public static String NEXT_PIC = "Pressed Next Button";



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
    public void onReceive(Context context, Intent intent){

        super.onReceive(context, intent);
      //  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dejaphoto_appwidget_layout);
        Intent clickIntent = new Intent(context, WidgetManager.class);


        if(intent.getAction().equals(PREVIOUS_PIC)){
            Toast.makeText(context, "Previous Picture", Toast.LENGTH_SHORT).show();
            clickIntent.setAction("previous");
        }

        else if(intent.getAction().equals(KARMA_BUTTON)){
            Toast.makeText(context, "Karma Not Yet Implemented", Toast.LENGTH_SHORT).show();
           // views.setTextViewText(R.id.karma_btn, "Undo Karma Button");
            clickIntent.setAction("karma");
        }

        else if(intent.getAction().equals(RELEASE_BUTTON)){
            Toast.makeText(context, "Picture Release Not Yet Implemented", Toast.LENGTH_SHORT).show();
            clickIntent.setAction("release");
        }

        else if(intent.getAction().equals(NEXT_PIC)){
            Toast.makeText(context, "Next Picture", Toast.LENGTH_SHORT).show();
            clickIntent.setAction("next");
        }
        context.startService(clickIntent); //call widgetmanager
    }



}
