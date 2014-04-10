package com.snail.contact.sync.test.sync;

import android.test.AndroidTestCase;
import com.snail.contact.sync.util.NetworkUtilities;

/**
 * Created by fenghb on 4/1/14.
 */
public class SyncTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

//        downloadContact();

        uploadContact();
    }

    @Override
    protected void tearDown() throws Exception {

    }

    public void downloadContact() throws Exception {

        NetworkUtilities.downloadContact();


    }

    public void uploadContact() throws Exception {


    }

}
