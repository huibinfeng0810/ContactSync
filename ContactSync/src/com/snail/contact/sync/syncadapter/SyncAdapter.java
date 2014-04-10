package com.snail.contact.sync.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import com.snail.contact.sync.contact.CloudContact;
import com.snail.contact.sync.contact.ContactManager;
import com.snail.contact.sync.contact.RawContact;
import com.snail.contact.sync.util.NetworkUtilities;

import java.util.List;

/**
 * Created by fenghb on 4/1/14.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private static final boolean DUG = true;


    private static final String SYNC_MARKER_KEY = "com.snail.sync.marker";


    private final Context mContext;

    private final AccountManager mAccountManager;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
        this.mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<RawContact> dirtyContacts;
        List<CloudContact> cloudContacts;


        long lastSyncMarker = getServerSyncMarker(account);

        //make sure the group exists
        final long groupId = ContactManager.ensureSnailGroupExists(mContext, account);

        //get mark local contact
        dirtyContacts = ContactManager.getDirtyContacts(mContext, account);

        //get cloud contact
        cloudContacts = NetworkUtilities.syncContacts(account, dirtyContacts);

        long newSyncState = ContactManager.updateContacts(mContext, account.name, cloudContacts, groupId, lastSyncMarker);
    }

    private long getServerSyncMarker(Account account) {
        String markerString = mAccountManager.getUserData(account, SYNC_MARKER_KEY);
        if (!TextUtils.isEmpty(markerString)) {
            return Long.parseLong(markerString);
        }
        return 0;
    }

    /**
     * Save off the high-water-mark we receive back from the server.
     *
     * @param account The account we're syncing
     * @param marker  The high-water-mark we want to save.
     */
    private void setServerSyncMarker(Account account, long marker) {
        mAccountManager.setUserData(account, SYNC_MARKER_KEY, Long.toString(marker));
    }


}
