package com.example.android;

import android.support.design.widget.TabLayout;
import android.support.test.rule.ActivityTestRule;
import android.widget.Switch;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Justin on 5/12/17.
 */

public class ModeActivityTest {

    @Rule
    public ActivityTestRule<ModeActivity> activity = new ActivityTestRule<>(ModeActivity.class);

    @Test
    public void test1(){

        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_mode);
        Boolean checked = sw.isChecked();
        String txt = sw.getText().toString();
        if(checked){
            assertEquals("Mode On", txt);
        }
        else{
            assertEquals("Mode", txt);
        }
    }
}
