package com.example.android;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 5/25/17.
 */

public class User{
    public String username;
    public String email;
    public static final String request = "Friend Request From";

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

    // send message to a friend request
    public static void sendNotification(String email1, String email2, DatabaseReference reference) {
        // add a new field of where notification comes from
        // add a new field of request coming from
        reference.child("users").child(email1).child("requests").child(email2).setValue(true);

    }

    //call when a friend request is accepted
    public static void addFriend(String currUserEmail, String userAcceptEmail, DatabaseReference reference){

        //add both users to each other's friends lists
        reference.child("users").child(currUserEmail)
                .child("friends")
                .child(userAcceptEmail)
                .setValue(true);

        reference.child("users").child(userAcceptEmail)
                .child("friends")
                .child(currUserEmail)
                .setValue(true);
    }
/* NEW IMPLEMENTATION

 public static boolean checkExist(String check, DatabaseReference reference){
      return reference.child("users").child(check) != null;
 }
 
 public static boolean addElement(String newUser, String currUserEmail, DatabaseReference reference){
 //add user
 
         User user = new User(name, email);
         
         reference.child("users")
                .child(newUser)
                .setValue(user);
 
 return true;
 }
     
 public static DatabaseReference getRef(String[] info, DatabaseReference reference){
     return reference.child("users");
 }

*/
}
