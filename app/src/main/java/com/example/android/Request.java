package com.example.android;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class Request implements IDataElement{
    public String requestFrom;
    public String requestTo;
    private DatabaseReference reference;
    
    public Request() {
        // Default currUser constructor
    }

    public Request(String fromEmail, String toEmail, DatabaseReference reference) {
        this.requestFrom = fromEmail.replace(".", ",");
        this.requestTo = toEmail.replace(".", ",");
        this.reference = reference;
    }

    @Override
    public boolean checkExist(String check){
        return true;
 }

 @Override
 public void addElement(){
//add request
       reference.child("users").child(requestTo)
                .child("requests")
                .child(requestFrom)
                .setValue(true);
 }

 @Override
 public DatabaseReference getRef(){
     return reference.child("users").child(requestTo)
             .child("requests")
             .child(requestFrom);
 }

    public static void clearRequest(String to, String from, DatabaseReference reference){
        reference.child("users").child(to).child("requests").child(from).removeValue();
    }

    public static void setRequestListener(DatabaseReference localRef){
        localRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //TODO handle request

                String requestEmail =  dataSnapshot.getKey();
                Log.d("RequestListener", "DataSnapshot"+ dataSnapshot);

                // add email address into array list that stores requests
                Global.currUser.requestList.add(requestEmail);
                AddFriendsActivity.nextRequest = Global.currUser.requestList.get(0).replace(",", ".");

                if(Global.currUser.requestList.size() > 0) {
                    AddFriendsActivity.requestView(true); //switch view on
                }

                Log.d("RequestListener", "childAdded currUser friendslist: "+ Global.currUser.requestList.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(Global.currUser.requestList.size() == 0){
                    AddFriendsActivity.requestView(false); //switch view on
                    AddFriendsActivity.nextRequest = "";


                }
                else  AddFriendsActivity.nextRequest = Global.currUser.requestList.get(0).replace(",", ".");

                Log.d("RequestListener", "childRemoved--- currUser friendslist: "+Global.currUser.requestList.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

 }
