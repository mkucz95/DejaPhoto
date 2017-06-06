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
    public void testDejaMode(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_mode);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.dejaVuSetting, true);

            sw.setChecked(false);
            assertEquals(Global.dejaVuSetting, false);
        }
        else{
            assertEquals(Global.dejaVuSetting, false); //all should be off if dejamode off
            assertEquals(Global.locationSetting, false);
            assertEquals(Global.timeSetting, false);
            assertEquals(Global.daySetting, false);
            assertEquals(Global.karmaSetting, false);

            sw.setChecked(true);
            assertEquals(Global.dejaVuSetting, true);
        }
    }

    @Test
    public void testLocation(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_location);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.locationSetting, true);

            sw.setChecked(true);
            assertEquals(Global.locationSetting, false);
        }
        else{
            assertEquals(Global.locationSetting, false);

            sw.setChecked(true);
            assertEquals(Global.locationSetting, true);
        }
    }

    @Test
    public void testKarma(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_karma);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.karmaSetting, true);

            sw.setChecked(false);
            assertEquals(Global.karmaSetting, false);
        }
        else{
            assertEquals(Global.karmaSetting, false);

            sw.setChecked(true);
            assertEquals(Global.karmaSetting, true);
        }
    }

    @Test
    public void testTime(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_time);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.timeSetting, true);

            sw.setChecked(false);
            assertEquals(Global.timeSetting, false);
        }
        else{
            assertEquals(Global.timeSetting, false);

            sw.setChecked(true);
            assertEquals(Global.timeSetting, true);
        }

    }

    @Test
    public void testDay(){
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_dow);
        Boolean checked = sw.isChecked();
        if(checked){
            assertEquals(Global.daySetting, true);

            sw.setChecked(false);
            assertEquals(Global.daySetting, false);
        }
        else{
            assertEquals(Global.daySetting, false);

            sw.setChecked(true);
            assertEquals(Global.daySetting, true);
        }
    }


}
