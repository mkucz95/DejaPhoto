package com.example.android;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */
@RunWith(JUnit4.class)
public class ShareActivityTest {
    @Rule
    public ActivityTestRule<ShareActivity> activity = new ActivityTestRule<>(ShareActivity.class);
    ShareActivity shareActivity = activity.getActivity();
    @Test
    public void testShare() {
        Switch sw = (Switch) activity.getActivity().findViewById(R.id.s_mode);
//        shareActivity.displayUpdate(true);
        onView(withId(R.id.s_mode)).perform(click());
        Boolean checked = sw.isChecked();
        if (checked) {
            assertEquals(Global.shareSetting, true);

            sw.setChecked(false);
            assertEquals(Global.shareSetting, false);
        } else {
            assertEquals(Global.shareSetting, false);

            sw.setChecked(true);
            assertEquals(Global.shareSetting, true);
        }
    }

    @Test
    public void testTextDisplay() {
        int interval = Global.syncInterval;
        TextView textView = (TextView) activity.getActivity().findViewById(R.id.syncInterval);
        String intervalString = textView.getText().toString();

        assertEquals(intervalString.contains("" + interval), true);
        //make sure string contains the number
    }

    @Test
    public void testTestButton() {
        Button button = (Button) activity.getActivity().findViewById(R.id.test_10sec);
        button.performClick();

        TextView textView = (TextView) activity.getActivity().findViewById(R.id.syncInterval);
        String intervalString = textView.getText().toString();

        assertEquals(intervalString.contains("" + 10), true);
        assertEquals(Global.syncInterval, 10);
        //make sure string contains the number
    }
}
