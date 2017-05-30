package com.example.android;

import com.google.firebase.database.DatabaseReference;

public class Request implements IDataElement{
    public String requestFrom;
    public String requestTo;
    private DatabaseReference reference;
    
    public Request() {
        // Default user constructor
    }

    public Request(String fromEmail, String toEmail, DatabaseReference reference) {
        this.requestFrom = fromEmail.replace(".", ",");
        this.requestTo = toEmail.replace(".", ",");
        this.reference = reference;
    }

    @Override
    public boolean checkExist(String check){
      return reference.child("requests").child(check) != null;
 }

 @Override
 public boolean addElement(){
//add request
       reference.child("users").child(requestTo)
                .child("requests")
                .child(requestFrom)
                .setValue(true);
 return true;
 }
    @Override
    public DatabaseReference getRef(String[] info){
     return reference.child("users").child(info[0]).child("requests");
 } //returns reference to the requests of a user

    public static void clearRequest(String from, String to, DatabaseReference reference){
        reference.child("users").child(to).child("requests").removeValue();
    }
 }
