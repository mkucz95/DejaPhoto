package com.example.android;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//Amanda code, dont touch

public class myReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent clickIntent = new Intent(getApplicationContext(), WidgetManager.class);
//        Intent clickIntent = new Intent(context, WidgetManager.class);
//        clickIntent.setAction(Intent.ACTION_SEND);
//        clickIntent.setType("text/plain");
//        clickIntent.putExtra("button_pressed", "next");
//
//        context.startService(clickIntent);



        Intent i = new Intent(context, DejaPhotoWidgetProvider.class);

        //Bundle(key, value): a map from string values to various Parcelable types
        //Use Bundle to store Data
        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");

        //transfer data from 1 act to another
        i.putExtras(bundleRet);
      //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra("button_pressed", "next");
        context.startService(i);
    }

}
