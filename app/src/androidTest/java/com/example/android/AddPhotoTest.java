package com.example.android;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Justin on 6/7/17.
 */

public class AddPhotoTest extends ActivityTestRule<MainActivity>{

    private Activity mActivity;
    final String testIntent = "Intent { act=android.intent.action.GET_CONTENT typ=image/* (has extras) }";
    final String testIntentExtras = "Bundle[{android.intent.extra.ALLOW_MULTIPLE=true}]";
    final String testIntentAction = "android.intent.action.GET_CONTENT";
    final String testChooserIntentAction = "android.intent.action.CHOOSER";
    final String testChooserIntent = "Intent { act=android.intent.action.CHOOSER (has extras) }";
    final String testChooserIntentExtras = "Bundle[{android.intent.extra.INTENT=Intent " +
            "{ act=android.intent.action.GET_CONTENT typ=image/* (has extras) }, " +
            "android.intent.extra.TITLE=Select Picture}]";

    public AddPhotoTest() {
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
    public void addPhotoTest1(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.toString(), testIntent);
    }

    @Test
    public void addPhotoTest2(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.getExtras().toString(), testIntentExtras);
    }

    @Test
    public void addPhotoTest3(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.testIntent.getAction(), testIntentAction);
    }

    @Test
    public void addPhotoTest4(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.getAction(), testChooserIntentAction);
    }

    @Test
    public void addPhotoTest5(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.toString(), testChooserIntent);
    }

    @Test
    public void addPhotoTest6(){
        AddPhoto testAdd = new AddPhoto(this.mActivity.getApplicationContext(), this.mActivity);
        testAdd.add();
        assertEquals(testAdd.chooserTestIntent.getExtras().toString(), testChooserIntentExtras);
    }


}
