package com.example.android;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Michael on 5/25/17.
 */

public class User implements IDataElement{
    public String username;
    public String email;
    public ArrayList<String> friendList = new ArrayList<>();
    private DatabaseReference reference;
    private final String TAG = "User.java";
    public  static boolean exists = false;


    public void User() {
        // Default user constructor
    }

    public User(String username, String email, DatabaseReference reference) {
        this.username = username;
        this.email = email.replace(".", ",");  //we cannot store certain characters in firebase
        this.reference = reference;
    }

    @Override
    public boolean checkExist(final String check) {
        Log.d(TAG, "checkExist");
        /*reference.child("users").child(check).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i(TAG, "data exists");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });*/

        if(reference.child("users").child(check).getKey() == check) {
            exists = true;
        }
        

        return exists;
    }

    @Override
    public boolean addElement() {
        //add user

        Log.d(TAG, "addElement");

        reference.child("users")
                .child(this.email)
                .child(this.email).setValue(true);

        return true;
    }

    @Override
    public DatabaseReference getRef(String[] info) {
        return reference.child("users");
    }

    //check if any user exists
    public static boolean checkAnyUser(String name, DatabaseReference reference) {
        Log.d("User.java", "checkAnyUser");

        //check to see if user exists in the database
        return reference.child("users").equalTo(name) != null;
        //returns true if it finds a users called name
    }
}
