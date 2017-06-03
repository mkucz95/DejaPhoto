package MS2;

import android.support.test.rule.ActivityTestRule;

import com.example.android.AddFriendsActivity;
import com.example.android.Global;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wl36901 on 2017/6/4.
 */

public class testHandleClick {
    @Rule
    public ActivityTestRule<AddFriendsActivity> addfActivity = new ActivityTestRule<AddFriendsActivity>(AddFriendsActivity.class);

    @Test
    public void test1(){
        AddFriendsActivity addFriendsActivity = addfActivity.getActivity();
        int size = Global.currUser.requestList.size();
        //addFriendsActivity.handleClick(false);
//        assertEquals (Global.currUser.requestList.size(), size-1);
        String input = "hello. world";
        // replaceData(input);

        assertEquals("hello, world", addFriendsActivity.replaceData(input));
    }
}
