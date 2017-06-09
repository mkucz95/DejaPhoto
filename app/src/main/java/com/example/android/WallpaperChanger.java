package com.example.android;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dejaphoto.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * When an intent starts this service, this service reads the action (which contains the new
 * image path) and then sets that image to the wallpaper.
 */
public class WallpaperChanger extends IntentService {

    DisplayMetrics metrics = new DisplayMetrics(); //get screen dimensions
    //getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int height = metrics.heightPixels;
    int width = metrics.widthPixels;

    int screenWidth = getScreenWidth();
    int screenHeight = getScreenHeight();
    public RemoteViews rviews;

    public WallpaperChanger() {
        super("WallpaperChanger");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            synchronized (this) {
                String imagePath = intent.getExtras().getString("image_path"); //takes info passed from intent
                Bitmap bitmap;
                if (imagePath.equals("DEFAULTPICTURE")) {
                    bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_picture);
                    setBackground(bitmap);
                } else {
                    //convert image path into something code can use
                    Bitmap bitmap1 = FileManager.getBitmap(imagePath);
                    bitmap1 = checkOrientation(imagePath, bitmap1);
                    setBackground(bitmap1);
                    updateWidget(imagePath);

                }
            }
            stopService(intent);
        }
    }

    public void updateWidget(String path) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName AppWidget = new ComponentName(this.getPackageName(), DejaPhotoWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);

        rviews = new RemoteViews(AppWidget.getPackageName(), R.layout.dejaphoto_appwidget_layout);
        Geocoder gc = new Geocoder(this.getApplicationContext(), Locale.getDefault());//Locale.getDefault()follow the system's language
        Log.i("PhotoLocation", "Getting location...");

        PhotoLocation locName = new PhotoLocation(path, gc);
        rviews.setTextViewText(R.id.display_location, locName.locationName);
        Log.i("LocName", path + " name: [" + locName.locationName + "]");
        if (!Global.isBlank(locName.locationName)) {
            rviews.setInt(R.id.display_location, "setBackgroundResource", R.drawable.widget_shape_location_white);
        } else {
            rviews.setTextViewText(R.id.display_location, "No Location Found");
            rviews.setInt(R.id.display_location, "setBackgroundResource", R.drawable.widget_shape_location_white);

        }
        rviews.setTextViewText(R.id.karma_num, "Karma: " + Global.displayCycle.get(Global.head).getKarma());
        Log.i("PhotoLocation", "Updating widget...");
        appWidgetManager.updateAppWidget(appWidgetIds, rviews);
    }

    public void setBackground(Bitmap bitmap) { //set wallpaper based on inputted bitmap
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        //call java wallpapermanager api
        Log.i("PhotoLocation", "In setBackground");


        if (bitmap != null) {
            try {
                Log.i("PhotoLocation", "Setting Bitmap...");
                Log.i("PhotoLocation", "Bitmap Size: " + "(" + bitmap.getWidth() + ", " + bitmap.getHeight() + ")");

                if (bitmap.getAllocationByteCount() > 3000000) {
                    Log.i("PhotoLocation", "Scaling Bitmap to (" + screenWidth + ", " + screenHeight + ")");
                    if (screenHeight < bitmap.getHeight()) {
                        if (screenWidth < bitmap.getWidth()) {
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight);
                        } else {
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight);
                        }
                        Log.i("PhotoLocation", "Scaling to width/height");
                    } else {
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                        Log.i("PhotoLocation", "Scaling to width/bitmapHeight");
                    }
                } else {
                    Log.i("PhotoLocation", "Bitmap size < 3mb, not scaling");
                }
                wallpaperManager.setBitmap(bitmap); //set wallpaper with new image
                Log.i("PhotoLocation", "Done setting bitmap");

                //wallpaperManager.suggestDesiredDimensions(width, height); //set dimensions
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Checks to see if we need to change the image orientation
    public Bitmap checkOrientation(String imagePath, Bitmap bitmap) {
        Log.i("checkOrientation", "Checking orientation...");
        try {
            ExifInterface ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.i("checkOrientation", "Rotate 90");
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.i("checkOrientation", "Rotate 180");
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.i("checkOrientation", "Rotate 270");
                    bitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    Log.i("checkOrientation", "Orientation Normal");
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    //Rotates the bitmap accordingly
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Log.i("orientation", "Rotation: " + angle);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
