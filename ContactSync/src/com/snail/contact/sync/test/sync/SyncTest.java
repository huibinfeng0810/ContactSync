package com.snail.contact.sync.test.sync;

import android.test.AndroidTestCase;
import com.snail.contact.sync.util.NetworkUtilities;

/**
 * Created by fenghb on 2014/4/10.
 */
public class SyncTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        NetworkUtilities.authenticate("22", "1");
    }

    @Override
    protected void tearDown() throws Exception {
    }


}
