package com.example.android;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;

import static com.example.android.Friends.getFriends;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */

public class FriendsActivityTest implements TestRule {
    @Rule
    //public ActivityTestRule<AddFriendsActivity> activity = new ActivityTestRule<>(AddFriendsActivity.class);
//    public FirebaseDatabase database = FirebaseDatabase.getInstance();
//    public DatabaseReference reference = database.getReferenceFromUrl("https://dejaphoto-33.firebaseio.com/");
//  public  Friends friends = new Friends(Global.currUser.email, Global.currUser.requestList.get(0), reference);
public Friends friends  = new Friends();
    @Test
    public void testCheckExist (){
        //TODO
//   assertEquals(friends.checkExist(Global.currUser.email),false);
      //  Friends friends2 = new Friends(str, str2,reference);
      //  assertEquals("ddd.dfd",currUserEmail);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return null;
    }

    @Test
    public void testArrayList() {
        ArrayList<String> l = new ArrayList<>();
        l = getFriends("lewan@ucsd.edu");

    }
}
