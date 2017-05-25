package com.example.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejaphoto.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_MULTIPLE_REQUEST = 99;
    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String GET_INITIAL_LOCATION = "com.example.android.GET_INITIAL_LOCATION";

    static final String dejaAlbum = "Deja Photo Album";
    static final String copyDeja = "/Deja Photo Album Copy/";
   
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dejaAlbum);

    String galleryPath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES + "/";

    //File deja = new File(Environment.getExternalStorageDirectory(), copyDeja);
    File deja;

    static final int REQUEST_CODE = 1;

    static final int SELECT_IMAGE = 1;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(Manifest.permission.READ_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button camera = (Button) findViewById(R.id.bt_1);
        Button settings = (Button) findViewById(R.id.bt_3);
        Button addPhoto = (Button) findViewById(R.id.bt_2);
        Button display = (Button) findViewById(R.id.bt_7);
        Button share = (Button) findViewById(R.id.bt_4);
        Button addFriends = (Button) findViewById(R.id.bt_6);


        //deja = new File("/sdcard/DejaAlbum");

        //deja.mkdirs();
        String rootDirectory = Environment.getExternalStorageDirectory().toString();
        deja = new File(rootDirectory + "/DejaCopy");
        deja.mkdir();


        startApp();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisplay();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShare();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSettings();
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRerank();
            }
        });

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addFriend();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
    }

    // MS2 click the camera button to open default camera
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            Toast.makeText(MainActivity.this, "Image saved", Toast.LENGTH_LONG).show();
        }
    }*/

    public void saveImage(Bitmap myMap) {
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myMap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File file = new File(Environment.getExternalStorageDirectory() + deja);
        if(!file.exists()) {
            file.mkdirs();
        }

        try {
            File f = new File(file, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved:: --->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";*/
        /*File outputFile = new File(Environment.getExternalStorageDirectory(), deja);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            myMap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.d("TAG", "File Saved:: --->" + outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void launchActivity() {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    public void changeSettings() {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
    }

    public void addFriend() {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
    }

    public void openDisplay() {
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }

    public void openShare() {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }

    public void addPhoto () {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            /*switch (requestCode) {
                case SELECT_IMAGE:
                    path = getPath(data.getData());
                    try {
                        copyFile(new File(path), deja);
                        Toast.makeText(getApplicationContext(), "Picture picked", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }*/
            path = getPath(data.getData());
            Log.i("pictureSelect", "path: " + path);
            try {
                copyFile(new File(path), deja);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyFile(File src, File dst) throws IOException {
        Log.i("pictureSelect", "source file: " + src.getPath());
        Log.i("pictureSelect", "Dest Folder: " + dst.getPath());
        File file = new File(dst + File.separator + src.getName());
        file.createNewFile();
        Log.i("pictureSelect", "Dest file: " + file.getAbsolutePath());


        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(file);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        startManagingCursor(cursor);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Toast.makeText(getApplicationContext(), "Picture picked", Toast.LENGTH_LONG).show();
        return cursor.getString(column_index);
    }

    public void startRerank(){
        Intent intent = new Intent (this, Rerank.class);
        startService(intent);
    }

    public void startApp(){
        Intent displayCycleIntent = new Intent(this, BuildDisplayCycle.class);


        Global.autoWallpaperChange = new AutoWallpaperChange(getApplicationContext());
        Global.timer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);

        Log.i("BuildCycle", "Calling BuildDisplayCycle...");
        displayCycleIntent.setAction(ACTION_BUILD_CYCLE);
        startService(displayCycleIntent);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void requestPermission(){
        Log.i("permission", "checking permission...");
        //Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Request Permissions
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_MULTIPLE_REQUEST);
        }
        else{
            startApp();
        }
        Log.i("permission", "done with permissions");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("permission", "Requesting Permission");
        if (requestCode == MY_PERMISSIONS_MULTIPLE_REQUEST) {
            Log.i("permission", "checking...");
            if (grantResults.length == 5 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("permission", "Read Permission Now Granted...");
                Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show();
                //Permission Granted, photos now accessible
            }
            else {
                //Permission denied
                Toast.makeText(this, "Read Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Location Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", " Location Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Camera Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Camera Access Denied", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length == 5 && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show();
                Log.i("permission", "Write Permission Now Granted...");
            }
            else {
                //Permission denied
                Toast.makeText(this, "Write Access Denied", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void saveInterval(View v) {
        EditText input = (EditText) findViewById(R.id.user_specify);

        Log.i("saveMsg", "input:" + input);

        if(input == null || input.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a valid time Interval", Toast.LENGTH_SHORT).show();
            return;
        }

        long newInterval = Integer.parseInt(input.getText().toString());


        //set the interval specified by user
        Global.changeInterval = newInterval * 1000;
        Global.timer.cancel();
        Global.timer = new Timer();
        Global.autoWallpaperChange = new AutoWallpaperChange(getApplicationContext());
        Global.timer.schedule(Global.autoWallpaperChange,
                Global.changeInterval, Global.changeInterval);

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

        Log.i("saveMsg", "Location: " + Global.locationSetting);
        Log.i("saveMsg", "Time: " + Global.timeSetting);
        Log.i("saveMsg", "Day: " + Global.daySetting);
        Log.i("saveMsg", "Karma: " + Global.karmaSetting);

        TextView interval = (TextView) findViewById(R.id.prevMin);
        TextView intervalNext = (TextView) findViewById(R.id.change_interval);
        intervalNext.setText(Long.toString(newInterval) + " seconds"); //show settings on settings page
    }
}
