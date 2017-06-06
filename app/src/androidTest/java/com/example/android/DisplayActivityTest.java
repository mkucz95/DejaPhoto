package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.Switch;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */

public class DisplayActivityTest {
    @Rule
    public ActivityTestRule<DisplayActivity> activity = new ActivityTestRule<>(DisplayActivity.class);
    Button button = (Button) activity.getActivity().findViewById(R.id.save_display);

    @Test
    public void testUserDisplay(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_displayUser);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.displayUser, true);

            sw.setChecked(false);
            button.performClick();

            assertEquals(Global.displayUser, false);
        }
        else{
            assertEquals(Global.displayUser, false);

            sw.setChecked(true);
            button.performClick();

            assertEquals(Global.displayUser, true);
        }
    }
    @Test
    public void testFriendDisplay(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_displayFriend);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.displayFriend, true);

            sw.setChecked(false);
            button.performClick();

            assertEquals(Global.displayFriend, false);
        }
        else{
            assertEquals(Global.displayFriend, false);

            sw.setChecked(true);
            button.performClick();

            assertEquals(Global.displayFriend, true);
        }
    }
}
