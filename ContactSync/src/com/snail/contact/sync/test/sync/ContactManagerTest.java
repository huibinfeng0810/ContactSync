package com.snail.contact.sync.test.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;
import com.snail.contact.sync.contact.ContactManager;
import com.snail.contact.sync.contact.RawContact;
import com.snail.contact.sync.util.LogUtil;

import java.util.List;

/**
 * Created by fenghb on 2014/4/10.
 */
public class ContactManagerTest extends AndroidTestCase {
    private static final String TAG = "ContactManagerTestTag";

    private static final String GOOGLE_ACCOUNT_TYPE = "com.google";

    @Override
    protected void setUp() throws Exception {
        Context mContext = getContext();
        getDirtyContact(mContext);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //get dirty contact
    public void getDirtyContact(Context context) {

        Account[] accounts = AccountManager.get(context).getAccountsByType(GOOGLE_ACCOUNT_TYPE);

        for (Account account : accounts) {
            Log.d(TAG, account.name);
        }

        List<RawContact> rawContacts = ContactManager.getDirtyContacts(mContext, accounts[0]);

        LogUtil.d(TAG, "size", rawContacts.size());

        for (RawContact rawContact : rawContacts) {
            Log.d(TAG, rawContact.getBestName());
        }
    }

    public void getAllAccounts(Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            Log.d(TAG, account.name);
        }
    }
}
