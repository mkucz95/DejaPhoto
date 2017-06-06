package com.example.android;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */

public class FriendsTest {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    User user = new User("tester1", "test1@gmail,com", databaseReference);
    DatabaseReference friendsRef = databaseReference.child(user.email);

    Friends friends = new Friends("test1@gmail,com", "test2@gmail,com", friendsRef);
    @Test
    public void testExists(){
        user.addElement();
        assertEquals(friends.checkExist("test2@gmail,com"), false);  //check to see if false
    }

    @Test
    public void testAdd(){
        friends.addElement();
        assertEquals(friends.checkExist("test2@gmail,com"), true);  //check to see if true
    }

    @Test
    public void testGetFriends(){
        friends.addElement();
        assertEquals(friends.checkExist("test2@gmail,com"), true);  //check to see if true
    }
}
