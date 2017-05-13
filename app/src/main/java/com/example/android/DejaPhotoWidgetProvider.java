package com.example.android;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

import org.w3c.dom.Text;

/**
 * Created by Justin on 5/3/17.
 * This class uses android APIs to create a widget that is responsive to presses on buttons
 */

public class DejaPhotoWidgetProvider extends AppWidgetProvider {
    public static String PREVIOUS_PIC = "Previous Picture";
    public static String KARMA_BUTTON = "Karma Added";
    public static String RELEASE_BUTTON = "Picture Released";
    public static String NEXT_PIC = "Next Picture";


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
        String pressed = "button_pressed";
        Boolean buttonPressed = false;
        Intent clickIntent = new Intent(context, WidgetManager.class);
        clickIntent.setAction(Intent.ACTION_SEND);
        clickIntent.setType("text/plain");

        if(intent.getAction().equals(PREVIOUS_PIC)){
            Toast.makeText(context, PREVIOUS_PIC, Toast.LENGTH_SHORT).show();
            clickIntent.putExtra(pressed,"previous");
            buttonPressed = true;
        }

        else if(intent.getAction().equals(KARMA_BUTTON)){
            Toast.makeText(context, KARMA_BUTTON, Toast.LENGTH_SHORT).show();
           // views.setTextViewText(R.id.karma_btn, "Undo Karma Button");
            clickIntent.putExtra(pressed,"karma");
            buttonPressed = true;
        }

        else if(intent.getAction().equals(RELEASE_BUTTON)){
            Toast.makeText(context, RELEASE_BUTTON , Toast.LENGTH_SHORT).show();
            clickIntent.putExtra(pressed, "release");
            buttonPressed = true;
        }

        else if(intent.getAction().equals(NEXT_PIC)){
            Toast.makeText(context, NEXT_PIC, Toast.LENGTH_SHORT).show();
            clickIntent.putExtra(pressed, "next");
            buttonPressed = true;
        }

        if (buttonPressed){
            context.startService(clickIntent); //call widgetmanager
        }

    }

}
