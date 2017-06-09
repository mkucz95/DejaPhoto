package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.Switch;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */

public class DisplayActivityTest {
    @Rule
    public ActivityTestRule<DisplayActivity> activity = new ActivityTestRule<>(DisplayActivity.class);
     //Button button = (Button) activity.getActivity().findViewById(R.id.save_display);
    DisplayActivity displayActivity = activity.getActivity();
    public Global global = new Global();


@Test
public void testUserDisplay() {
//    onView(withId(R.id.s_displayUser));
//    assertEquals(Global.displayUser,false);
    onView(withId(R.id.s_displayUser)).perform(click());
    onView(withId(R.id.save_display)).perform(click());
    assertEquals(Global.displayUser,false);
 //   onView(withId(R.id.s_displayUser)).perform(click());
  //  assertEquals(Global.displayUser,false);

}

    @Test
    public void testFriendDisplay() {
       // if (onView(withId(R.id.s_displayFriend)).check(matches isChecked()))
        onView(withId(R.id.s_displayFriend)).perform(click());
        onView(withId(R.id.save_display)).perform(click());

        assertEquals(Global.displayFriend,false);
    }
//    @Test
//    public void testUserDisplay(){
//
//        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_displayUser);
//        Boolean checked = sw.isChecked();
//        if(checked){
//            assertEquals(Global.displayUser, true);
//            onView(withId(R.id.s_displayFriend)).perform();
//
//     //    sw.setChecked(false);
//            onView(withId(R.id.save_display)).perform(click());
//       //     displayActivity.findViewById(R.id.save_display).performClick();
//            //displayActivity.button.performClick();
//
//    //        assertEquals(Global.displayUser, false);
//        }
//        else{
//            assertEquals(Global.displayUser, false);
//
//            sw.setChecked(true);
//           // displayActivity.findViewById(R.id.save_display).performClick();
//            onView(withId(R.id.save_display)).perform();
//            assertEquals(Global.displayUser, true);
//        }
//    }
//    @Test
//    public void testFriendDisplay(){
//        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_displayFriend);
//        Boolean checked = sw.isChecked();
//        if(checked){
//            assertEquals(Global.displayFriend, true);
//
//            sw.setChecked(false);
//         //   displayActivity.findViewById(R.id.save_display).performClick();
//
//            assertEquals(Global.displayFriend, false);
//        }
//        else{
//            assertEquals(Global.displayFriend, false);
//
//            sw.setChecked(true);
//         //   displayActivity.findViewById(R.id.save_display).performClick();
//
//
//
//            assertEquals(Global.displayFriend, true);
//        }
//    }
}
