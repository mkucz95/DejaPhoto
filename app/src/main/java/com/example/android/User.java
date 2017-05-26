package com.example.android;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 5/25/17.
 */

public class User{
    public String username;
    public String email;

    public User() {
        // Default user constructor
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public static boolean checkUser(String name, DatabaseReference reference){
        //check to see if user exists in the database

        return reference.child("users").equalTo(name) != null;
        //returns true if it finds a users called name
    }

    public static void createNewUser(String name, String email, DatabaseReference reference){
        //create a new user and save to database
        //use email as a 'user id'

        User user = new User(name, email);

        reference.child("users").child(email).setValue(user); //add new user node
    }

    //call when a friend request is accepted
    public static void addFriend(String email1, String email2, DatabaseReference reference){
        //add both users to each other's friends lists
        reference.child("users").child(email1).child("friends").child(email2).setValue(true);
        reference.child("users").child(email2).child("friends").child(email1).setValue(true);
    }
}
