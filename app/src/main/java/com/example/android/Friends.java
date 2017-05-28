package com.example.android;

import com.google.firebase.database.DatabaseReference;

public class Friends implements IDataElement{

    public String email1, email2;
    private DatabaseReference reference;
    
    public Friends() {
        // Default user constructor
    }

    public Friends(String friendEmail1, String friendEmail2, DatabaseReference reference) {
        this.email1 = friendEmail1;
        this.email2 = friendEmail2;
        this.reference = reference;
    }

    @Override
    public boolean checkExist(String check){
      return reference.child("friends").child(check) != null;
 }

    @Override
    public boolean addElement(){
 //add friends
         reference.child("users").child(email1)
                .child("friends")
                .child(email2)
                .setValue(true);

        reference.child("users").child(email2)
                .child("friends")
                .child(email1)
                .setValue(true);
 
    return true;
 }

    @Override
    public DatabaseReference getRef(String[] info){
     return reference.child("users").child(info[0]).child("friends");
 }


}
