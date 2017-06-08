package com.example.android;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Michael on 6/7/17.
 */

public class PhotoUrl implements IDataElement {
    Uri url;
    DatabaseReference reference;

    public void PhotoUrl(Uri url){
        this.url = url;
    }

    @Override
    public boolean checkExist(String check) {
        return false;
    }

    @Override
    public void addElement() {

        //upload references

    }

    @Override
    public DatabaseReference getRef() {
        return null;
    }

    public static void setDatabaseListener(DatabaseReference reference){
        //make sure we get new snapshot when people upload images
       // Global.friendPhotoUrl = .....
    }
}
