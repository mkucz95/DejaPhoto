package com.example.android;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Friends implements IDataElement{

    public String currUserEmail, friendWhoRequested;
    private DatabaseReference reference;
    
    public Friends() {
        // Default currUser constructor
    }

    public Friends(String friendEmail1, String friendEmail2, DatabaseReference reference) {
        this.currUserEmail = friendEmail1.replace(".", ",");
        this.friendWhoRequested = friendEmail2.replace(".", ",");
        this.reference = reference;
    }

    @Override
    public boolean checkExist(String check){
      return reference.child("friends").child(check) != null;
 }

    @Override
    public void addElement(){
 //add friends
         reference.child("users").child(currUserEmail)
                .child("friends")
                .child(friendWhoRequested)
                .setValue(true);

        reference.child("users").child(friendWhoRequested)
                .child("friends")
                .child(currUserEmail)
                .setValue(true);
    }

    @Override
    public DatabaseReference getRef() {
        return reference.child("users").child(this.currUserEmail).child("friends");
    }

        public static DatabaseReference getRef(String userEmail, DatabaseReference reference){
        return reference.child("users").child(userEmail).child("friends");
    }

    //return arraylist of friends of a user
    public static ArrayList<String> getFriends(String user){
        ArrayList<String> friends = new ArrayList<>();

        for(DataSnapshot snapshot: Global.userSnapshot.child(user).child("friends").getChildren())
           friends.add(snapshot.getKey());

        return friends;
    }
 }

