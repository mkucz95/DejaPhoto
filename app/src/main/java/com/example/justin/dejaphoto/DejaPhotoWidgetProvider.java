package com.example.justin.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Justin on 5/3/17.
 */

public class DejaPhotoWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;

        for(int i=0; i<N; i++){ //
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, SettingsActivity.class); //TODO: this is fucked
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            //get layout for our wdiget, give each button on-click listener
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dejaphoto_appwidget_layout);
            views.setOnClickPendingIntent(R.id.previous_pic, pendingIntent);
            views.setOnClickPendingIntent(R.id.karma_btn, pendingIntent);
            views.setOnClickPendingIntent(R.id.release_btn, pendingIntent);
            views.setOnClickPendingIntent(R.id.next_pic, pendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

}
