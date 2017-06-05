
package MS2;

import android.support.test.rule.ActivityTestRule;

import com.example.android.AddFriendsActivity;
import com.example.android.Global;
import com.example.android.PhotoStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wl36901 on 2017/6/4.
 */

public   class testUpload {
    StorageReference storageReference = PhotoStorage.getStorageRef(Global.currUser.email);
    @Rule
    public PhotoStorage photoStorage = new PhotoStorage("/sdcard/DejaPhoto/FILENAME-2", storageReference);

    @Test
    public void test1(){
        assertTrue(photoStorage.addElement());

    }
}
