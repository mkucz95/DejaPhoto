package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.widget.Switch;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 5/11/2017.
 */

public class SetActivityTest {


    @Rule
    public ActivityTestRule<SetActivity> activity = new ActivityTestRule<>(SetActivity.class);

    @Test
    public void test1(){

        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_location);
        Boolean checked = sw.isChecked();
        String txt = sw.getText().toString();
        if(checked){
            assertEquals("Location On", txt);
        }
        else{
            assertEquals("Location", txt);
        }

    }

    @Test
    public void test2(){
        //TODO Check user time input
    }


}
