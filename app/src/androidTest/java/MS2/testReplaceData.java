package MS2;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import com.example.android.AddFriendsActivity;
import com.example.dejaphoto.R;

import org.junit.Rule;
import org.junit.Test;

import static com.example.android.AddFriendsActivity.replaceData;
import static junit.framework.Assert.assertEquals;

/**
 * Created by wl36901 on 2017/6/3.
 */

public class TestUpdateUI {

    @Rule
    public ActivityTestRule<AddFriendsActivity> addfActivity = new ActivityTestRule<AddFriendsActivity>(AddFriendsActivity.class);

    @Test
    public void test1() {
      //AddFriendsActivity adf = AddFriendsActivity.
        String input = "hello. world";
       // replaceData(input);

        assertEquals("hello, world", replaceData(input));

    }


}
