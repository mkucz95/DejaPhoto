package com.example.android;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.android.MainActivity.REQUEST_CODE;
import static com.example.android.MainActivity.SELECT_IMAGE;

/**
 * Created by Justin on 6/7/17.
 * AddPhoto class
 */

public class AddPhoto {

    /*
     * Instance variables and test variables
     */
    Context context;
    private Activity activity;
    String TAG = "AddPhoto";
    Intent testIntent;
    Intent chooserTestIntent;
    private final String imageType = "image/*";
    private final String selectPicture = "Select Picture";


    /*
     * AddPhoto constructor -- context and activity instance from MainActivity
     */
    public AddPhoto(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    /*
     * This method prompts the chooser to appear. The result is handled in MainActivity
     */
    public void add(){
        Intent intent = new Intent();
        //Set intent type -- images
        intent.setType(imageType);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Allow multiple selections
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        this.testIntent = intent;
        Intent chooser = Intent.createChooser(intent, selectPicture);
        this.chooserTestIntent = chooser;
        activity.startActivityForResult(chooser, SELECT_IMAGE);
    }
}
