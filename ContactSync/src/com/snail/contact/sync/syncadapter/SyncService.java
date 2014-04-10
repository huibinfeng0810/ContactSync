package com.snail.contact.sync.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by fenghb on 4/1/14.
 */
public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();

    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
