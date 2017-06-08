package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.test.InstrumentationRegistry;
import android.test.ServiceTestCase;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Created by mkucz on 5/11/2017.
 */

public class RerankTest extends ServiceTestCase<Rerank>{
    String TAG = "RerankTest";
    String expectedIntent = "Intent { cmp=com.example.android/.Rerank }";


    public RerankTest() {
        super(Rerank.class);
    }

    @Test
    public void testOnHandleIntent1() throws Exception{
        Intent intent = new Intent(getSystemContext(), Rerank.class);
        getSystemContext().startService(intent);
        assertNotNull(intent);
    }

    @Test
    public void testOnHandleIntent2() throws Exception{
        Intent intent = new Intent(getSystemContext(), Rerank.class);
        getSystemContext().startService(intent);
        assertEquals(expectedIntent, intent.toString());
    }

    @Test
    public void testOnHandleIntent3() throws Exception{
        Intent intent = new Intent(getSystemContext(), Rerank.class);
        getSystemContext().startService(intent);
        assertNull(intent.getAction());
        assertNull(intent.getExtras());
    }


}
