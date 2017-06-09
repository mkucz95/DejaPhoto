package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wl36901 on 2017/6/8.
 */

public class TestAddFriendsActivity {
    @Rule
    public ActivityTestRule<AddFriendsActivity> addfActivity = new ActivityTestRule<AddFriendsActivity>(AddFriendsActivity.class);

    @Test
    public void testHandleClick(){
        AddFriendsActivity addFriendsActivity = addfActivity.getActivity();
        int size = Global.currUser.requestList.size();
        //addFriendsActivity.handleClick(false);
//        assertEquals (Global.currUser.requestList.size(), size-1);
        String input = "hello. world";
        // replaceData(input);

        assertEquals("hello, world", addFriendsActivity.replaceData(input));
    }
    @Test
    public void testGetEditTextString() {
        AddFriendsActivity addFriendsActivity = addfActivity.getActivity();
        EditText input = (EditText) addfActivity.getActivity().findViewById(R.id.fortest);

        assertEquals(addFriendsActivity.getEditTextString(input),"test");
    }
}
