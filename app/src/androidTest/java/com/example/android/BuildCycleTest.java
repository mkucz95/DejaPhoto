package com.example.android;

import org.junit.Rule;
import org.junit.Test;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ServiceTestCase;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeoutException;

/**
 * Created by mkucz on 5/11/2017.
 * unit testing for buildDisplayCycle
 */

@RunWith(JUnit4.class)
public class BuildCycleTest{

    private static final String ACTION_BUILD_CYCLE = "com.example.android.BUILD_CYCLE";
    private static final String ACTION_RERANK_BUILD = "com.example.android.RERANK_BUILD";

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Test
    public void test1() throws TimeoutException {
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), BuildDisplayCycle.class);
        serviceIntent.setAction(ACTION_BUILD_CYCLE);
        IBinder binder = mServiceRule.bindService(serviceIntent);
        //BuildDisplayCycle service = (BuildDisplayCycle);
    }
}
