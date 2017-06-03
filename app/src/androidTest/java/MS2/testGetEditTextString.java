package MS2;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.AddFriendsActivity;
import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wl36901 on 2017/6/3.
 */

public class testGetEditTextString {
    @Rule
    public ActivityTestRule<AddFriendsActivity> addfActivity = new ActivityTestRule<AddFriendsActivity>(AddFriendsActivity.class);

    @Test
    public void test1() {
        AddFriendsActivity addFriendsActivity = addfActivity.getActivity();
        EditText input = (EditText) addfActivity.getActivity().findViewById(R.id.fortest);





        assertEquals(addFriendsActivity.getEditTextString(input),"test");


    }
}
