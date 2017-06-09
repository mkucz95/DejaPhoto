package com.example.android;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Michael on 5/25/17.
 */

public class User implements IDataElement {
    public String username;
    public String email;
    public ArrayList<String> requestList = new ArrayList<>();

    private DatabaseReference reference;
    private final String TAG = "User.java";
    public static boolean exists = false;

    public void User() {
        // Default currUser constructor
    }

    public User(String username, String email, DatabaseReference reference) {
        this.username = username;
        this.email = email.replace(".", ",");  //we cannot store certain characters in firebase
        this.reference = reference;
    }

    @Override
    public boolean checkExist(final String check) {
        Log.d(TAG, "checkExist");
        Log.d(TAG, "checkEmail" + check);
        Log.i(TAG, "whole snap: " + Global.userSnapshot);


        if (Global.userSnapshot.child(check).exists()) {
            Log.i(TAG, "userSnap: " + Global.userSnapshot.child(check));
            return true;
        }

        return false;
    }

    @Override
    public void addElement() {
        //add currUser

        Log.d(TAG, "addElement");

        reference.child("users")
                .child(this.email)
                .child(this.email).setValue(true);
    }

    @Override
    public DatabaseReference getRef() {
        return reference.child("users").child(this.email);
    }

    public static void setDatabaseListener(final DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get map of users in datasnapshot
                Global.userSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle database error
            }
        });
    }

    public DatabaseReference userPhotosRef() {
        DatabaseReference databaseReference = reference.getRoot().child("photos").child(this.email);
        Log.d(TAG, "photos ref: " + databaseReference);

        return databaseReference;
    }
}
