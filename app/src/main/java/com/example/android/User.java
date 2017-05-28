package com.example.android;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 5/25/17.
 */

public class User implements IDataElement{
    public String username;
    public String email;
    public ArrayList<String> friendList = new ArrayList<>();
    private DatabaseReference reference;

    public void User() {
        // Default user constructor
    }

    public User(String username, String email, DatabaseReference reference) {
        this.username = username;
        this.email = email;
        this.reference = reference;
    }

    @Override
    public boolean checkExist(String check) {
        return reference.child("users").child(check) != null;
    }

    @Override
    public boolean addElement() {
        //add user

        reference.child("users")
                .child(this.email)
                .setValue(this);

        return true;
    }

    @Override
    public DatabaseReference getRef(String[] info) {
        return reference.child("users");
    }

    //check if any user exists
    public static boolean checkAnyUser(String name, DatabaseReference reference) {
        //check to see if user exists in the database
        return reference.child("users").equalTo(name) != null;
        //returns true if it finds a users called name
    }
}
