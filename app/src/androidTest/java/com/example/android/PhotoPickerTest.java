package com.example.android;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Justin on 6/7/17.
 */

public class PhotoPickerTest extends ActivityTestRule<MainActivity>{

    private Activity mActivity;
    final String testIntent = "Intent { act=android.intent.action.GET_CONTENT typ=image/* (has extras) }";
    final String testIntentExtras = "Bundle[{android.intent.extra.ALLOW_MULTIPLE=true}]";
    final String testIntentAction = "android.intent.action.GET_CONTENT";
    final String testChooserIntentAction = "android.intent.action.CHOOSER";
    final String testChooserIntent = "Intent { act=android.intent.action.CHOOSER (has extras) }";
    final String testChooserIntentExtras = "Bundle[{android.intent.extra.INTENT=Intent " +
            "{ act=android.intent.action.GET_CONTENT typ=image/* (has extras) }, " +
            "android.intent.extra.TITLE=Select Picture}]";

    public PhotoPickerTest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        MainActivity mainActivity = mActivityRule.getActivity();
        this.mActivity = mainActivity;
    }

    @Test
    public void activityExists(){
        assertNotNull(mActivity);
    }

    @Test
    public void photoPickerTest1(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.toString(), testIntent);
    }

    @Test
    public void photoPickerTest2(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.getExtras().toString(), testIntentExtras);
    }

    @Test
    public void photoPickerTest3(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.getAction(), testIntentAction);
    }

    @Test
    public void photoPickerTest4(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.getAction(), testChooserIntentAction);
    }

    @Test
    public void photoPickerTest5(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.toString(), testChooserIntent);
    }

    @Test
    public void photoPickerTest6(){
        PhotoPicker testAdd = new PhotoPicker(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.getExtras().toString(), testChooserIntentExtras);
    }


}
