package com.example.android;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mkucz on 6/5/2017.
 */

public class UserTest {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    User user = new User("test", "test@gmail,com", databaseReference);

    @Test
    public void testExists(){
        assertEquals(user.checkExist(user.email), false);  //check to see if false
    }

    @Test
    public void testAdd(){
       ArrayList<String> test = Friends.getFriends(user.email);
        assertNotNull(test);  //check to see that friends is not null
        if(test != null) {
            assertEquals(test.get(0), "user2@gmail,com");
        }
    }
}
