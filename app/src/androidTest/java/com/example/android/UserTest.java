package com.example.android;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        user.addElement();
        assertEquals(user.checkExist(user.email), true);  //check to see if false
    }

}
