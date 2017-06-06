package com.example.android;

import org.hamcrest.JavaLangMatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Michael on 6/5/17.
 */

@RunWith(JUnit4.class)
public class TestDatabaseSync {

    Timer timer = new Timer();

    @Test
    public void test1() {
        final CountDownLatch latch = new CountDownLatch(1);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerTask task = new DatabaseSync();
                timer.schedule(task, 0, 1000);
                latch.countDown();
            }
        }, (1000));

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
