package com.example.android;

import android.content.Context;
import android.net.Uri;

import com.example.android.SQLiteHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mkucz on 6/5/2017.
 */

@RunWith(JUnit4.class)
public class SQLiteHelperTest {

    @Test
    public void getUriTest() {
        String path1 = "/storage/emulated/0/DejaPhoto/FILENAME-20.jpg";
        String path2 = "/storage/emulated/0/DejaPhoto/FILENAME-2.jpg";

        Context context = null;
        Uri uri = Uri.fromFile(new File(path2));
        String pathTest = SQLiteHelper.getImagePath(context, uri);

        assertEquals(path1, pathTest);
    }

    @Test
    public void testCase1() {
        assertEquals(SQLiteHelper.matchesCases("false", true, true), false);
    }

    @Test
    public void testCase2() {
        assertEquals(SQLiteHelper.matchesCases("false", false, true), false);
    }

    @Test
    public void testCase3() {
        assertEquals(SQLiteHelper.matchesCases("false", true, false), false);
    }

    @Test
    public void testCase4() {
        assertEquals(SQLiteHelper.matchesCases("false", false, false), false);
    }

    @Test
    public void testCase5() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", true, true), true);
    }

    @Test
    public void testCasePhoto() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", true, false), false);
    }

    @Test
    public void testCasePhoto2() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", false, true), true);
    }

    @Test
    public void testCasePhoto3() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", true, true), true);
    }

    @Test
    public void testCasePhoto4() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", false, false), false);
    }

    @Test
    public void testCase6() {
        assertEquals(SQLiteHelper.matchesCases("DejaCopy", false, true), true);
    }

    @Test
    public void testCase7() {
        assertEquals(SQLiteHelper.matchesCases("DejaCopy", false, false), false);
    }

    @Test
    public void testCase8() {
        assertEquals(SQLiteHelper.matchesCases("DejaCopy", true, true), false);
    }

    @Test
    public void testCase9() {
        assertEquals(SQLiteHelper.matchesCases("DejaPhoto", true, true), false);
    }


}


