package com.snail.contact.sync.contact;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import com.snail.contact.sync.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghb on 4/1/14.
 */
public class ContactManager {

    private static final String TAG = "ContactManager";

    private static final boolean DUG = true;

    public static final String SAMPLE_GROUP_NAME = "Sample Group";

    /**
     * update local contact data
     *
     * @param context
     * @param account
     * @param cloudContacts
     */
    public static synchronized void syncContact(Context context, Account account, List<CloudContact> cloudContacts) {

        final ContentResolver resolver = context.getContentResolver();

        final ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        for (CloudContact mCloudContact : cloudContacts) {
            /**
             * get cloud  contact cUuid
             */
            String cUuid = mCloudContact.getcUuid();

            /**
             * query raw contact by cloud contact cUuid
             */
            long rawId = getRawId(context, account, cUuid);

            if (rawId > 0) {
                /**
                 * if query raw contact success, return id > 0,update local contact
                 */
                updateLocalContact(context, account, mCloudContact, rawId, operations);

            } else {
                /**
                 *if query raw contact failed, return id = -1;
                 */

                addLocalContact(context, account, mCloudContact, operations);
            }

            /**
             * if operations size >= 50, invoke batch method
             */
            if (operations.size() >= 50) {
                try {
                    resolver.applyBatch(ContactsContract.AUTHORITY, operations);
                } catch (RemoteException e) {
                    if (DUG) {
                        Log.e(TAG, "RemoteException");
                        e.printStackTrace();
                    }
                } catch (OperationApplicationException e) {
                    if (DUG) {
                        Log.e(TAG, "OperationApplicationException");
                        e.printStackTrace();
                    }
                }
                operations.clear();
            }
        }
    }

    /**
     * update local contact by cloud server data
     */
    private final static void updateLocalContact(Context context, Account account, CloudContact cloudContact, long rawContactId,
                                                 ArrayList<ContentProviderOperation> operations) {
        operations.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=" + rawContactId, null)
                .build());

        if (cloudContact.getiOperateStatus().equals("0")) {
            /**
             * if getiOperateStatus return 0, delete the local contact
             */
            Uri uri = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();

            operations.add(ContentProviderOperation.newDelete(uri)
                    .withSelection(ContactsContract.RawContacts._ID + "=" + rawContactId, null)
                    .build());
        } else {
            /**
             * add local contact
             */
            insertData(context, cloudContact, account, rawContactId, operations);
        }
    }

    /**
     * query raw contact by cUuid
     *
     * @param context
     * @param account
     * @param uuid
     * @return
     */
    public static long getRawId(Context context, Account account, String uuid) {
        String[] projection = new String[]{
                ContactsContract.RawContacts._ID,
        };
        String selection = ContactsContract.RawContacts.ACCOUNT_TYPE + "=?" + " AND "
                + ContactsContract.RawContacts.ACCOUNT_NAME + "=?" + " AND "
                + ContactsContract.RawContacts.SYNC2 + "=?";

        final ContentResolver resolver = context.getContentResolver();
        final Cursor c = resolver.query(ContactsContract.RawContacts.CONTENT_URI,
                projection, selection,
                new String[]{account.type, account.name, uuid},
                null);

        try {
            c.moveToFirst();

            return c.getCount() == 0 ? -1 : c.getLong(0);
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

    public static List<RawContact> getDirtyContacts(Context context, Account account) {

        List<RawContact> dirtyContacts = new ArrayList<RawContact>();

        final ContentResolver resolver = context.getContentResolver();

        final Cursor c = resolver.query(DirtyQuery.CONTENT_URI,

                DirtyQuery.PROJECTION,

                DirtyQuery.SELECTION,

                new String[]{account.name},

                null);

        try {
            while (c.moveToNext()) {

                final long rawContactId = c.getLong(DirtyQuery.COLUMN_RAW_CONTACT_ID);

                final boolean isDirty = "1".equals(c.getString(DirtyQuery.COLUMN_DIRTY));

                final boolean isDeleted = "1".equals(c.getString(DirtyQuery.COLUMN_DELETED));

                final long version = c.getLong(DirtyQuery.COLUMN_VERSION);

                final String cUuid = c.getString(DirtyQuery.COLUMN_UUID);

                if (DUG) {

                    Log.i(TAG, "Dirty Contact: " + Long.toString(rawContactId));

                    Log.i(TAG, "Contact Version: " + Long.toString(version));

                }

                if (isDeleted) {

                    if (DUG) {

                        Log.i(TAG, "Contact is marked for deletion");

                    }

                    RawContact rawContact = RawContact.createDeletedContact(cUuid);

                    dirtyContacts.add(rawContact);

                } else if (isDirty) {

                    RawContact rawContact = getRawContact(context, rawContactId);

                    if (DUG) {

                        Log.i(TAG, "Contact Name: " + rawContact.getBestName());

                    }

                    dirtyContacts.add(rawContact);

                }
            }

        } finally {
            if (c != null) {
                c.close();
            }
        }
        return dirtyContacts;


    }

    private static RawContact getRawContact(Context context, long rawContactId) {

//        RawContact rawContact = RawContact.create(uuid, sContactFirstName, sContactLastName, dLastModifyTime,
//                phones, groups, sContactData, status, sContactFullName, sComment, sNickname, dBirthday, addresses, websites);
        String uuid = null;

        String sContactFirstName = null;

        String sContactLastName = null;

        String dLastModifyTime = null;

        List<RawContact.Phone> phones = null;

        List<RawContact.Email> emails = null;

        List<RawContact.Group> groups = null;

        String sContactData = null;

        String status = null;

        String sContactFullName = null;

        String sComment = null;

        String sNickname = null;

        String dBirthday = null;

        List<RawContact.Address> addresses = null;

        List<RawContact.Website> websites = null;


        /**
         * Phone
         */
        String cellPhone = null;

        String homePhone = null;

        String workPhone = null;
        /**
         * -------------------------
         */

        String email = null;

        long serverId = -1;

        final ContentResolver resolver = context.getContentResolver();

        final Cursor c = resolver.query(DataQuery.CONTENT_URI, DataQuery.PROJECTION, DataQuery.SELECTION,
                new String[]{String.valueOf(rawContactId)}, null);
        try {

            while (c.moveToNext()) {

                final long id = c.getLong(DataQuery.COLUMN_ID);

                final String mimeType = c.getString(DataQuery.COLUMN_MIMETYPE);

                final long tempServerId = c.getLong(DataQuery.COLUMN_SERVER_ID);

                if (tempServerId > 0) {

                    serverId = tempServerId;
                }

                final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, id);

                if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {

                    sContactLastName = c.getString(DataQuery.COLUMN_FAMILY_NAME);

                    sContactFirstName = c.getString(DataQuery.COLUMN_GIVEN_NAME);

                    sContactFullName = c.getString(DataQuery.COLUMN_FULL_NAME);

                } else if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                    final int type = c.getInt(DataQuery.COLUMN_PHONE_TYPE);

                    if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {

                        cellPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);

                    } else if (type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {

                        homePhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);

                    } else if (type == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {

                        workPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
                    }
                } else if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                    email = c.getString(DataQuery.COLUMN_EMAIL_ADDRESS);
                }
            }
        } finally {

            c.close();
        }

        RawContact rawContact = RawContact.create(uuid, sContactFirstName, sContactLastName, dLastModifyTime,
                phones, groups, emails, sContactData, status, sContactFullName, sComment, sNickname, dBirthday, addresses, websites);

        return rawContact;
    }

    public static long insertEmptyRawContact(Context context, Account account, String uuid) {

        ContentValues values = new ContentValues();

        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, account.name);

        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type);

        values.put(ContactsContract.RawContacts.SYNC2, uuid);

        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);

        return ContentUris.parseId(rawContactUri);
    }


    /**
     * insertData
     *
     * @param context
     * @param cloudContact
     * @param account
     * @param rawContactId
     * @param operations
     */
    public static void insertData(Context context, CloudContact cloudContact, Account account, long rawContactId,
                                  ArrayList<ContentProviderOperation> operations) {
        // DisplayName
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, cloudContact.getsContactFullName())
                .build());

        // Nickname
        String nickname = cloudContact.getsNickname();
        if (nickname != null && !"".equals(nickname)) {
            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, nickname)
                    .build());
        }

        // birthday
        String birthday = cloudContact.getBirthday();
        if (birthday != null && !"".equals(birthday)) {
            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, birthday)
                    .withValue(ContactsContract.Data.DATA2, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                    .build());
        }

        //Comment
        String comment = cloudContact.getsComment();
        if (comment != null && !"".equals(comment)) {
            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, comment)
                    .build());
        }

        // Phone Number
        List<CloudContact.Phone> phoneList = cloudContact.getPhones();

        if (phoneList != null) {
            for (CloudContact.Phone phone : phoneList) {
                operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phone.getsPhoneTypeId())
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getnPhoneNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, phone.getsPhoneType())
                        .build());

            }

        }

        /**
         * Email
         */
        List<CloudContact.Email> emails = cloudContact.getEmails();

        if (null != emails) {
            for (CloudContact.Email email : emails) {
                operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getsEmailAddress())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, email.getsEmailTypeId())
                        .withValue(ContactsContract.CommonDataKinds.Email.LABEL, email.getsEmailType())
                        .build());


            }

        }

        /**
         * Address
         */
        List<CloudContact.Address> addresses = cloudContact.getAddresses();
        if (null != addresses) {
            for (CloudContact.Address address : addresses) {
                operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA, address.getsAddress())
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, address.getsAddressTypeId())
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, address.getsAddressType())
                        .build());

            }
        }

        /**
         * Website
         */
        List<CloudContact.Website> websites = cloudContact.getWebsites();

        for (CloudContact.Website website : websites) {
            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Website.DATA, website.getsSite())
                    .build());
        }

        /**
         * group
         */
        List<CloudContact.Group> groups = cloudContact.getGroups();
        for (CloudContact.Group group : groups) {

            /**
             * get group id by group name
             */
            long groupId = getGroupsId(context, account, group.getsGroupName());

            operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.GroupMembership.DATA1, groupId)
                    .build());

        }
    }

    /**
     * get Groups id by group name
     */
    public static long getGroupsId(Context context, Account account, String groupName) {
        /**
         * 检测是否存在该类别 group
         */
        Cursor c = context.getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                new String[]{ContactsContract.Groups._ID}, ContactsContract.Groups.TITLE + "=?",
                new String[]{groupName}, null);
        if (c.getCount() == 0) {
            /**
             *  没有则创建 group
             */
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Groups.ACCOUNT_NAME, account.name);
            values.put(ContactsContract.Groups.ACCOUNT_TYPE, account.type);
            values.put(ContactsContract.Groups.TITLE, groupName);

            Uri groupsUri = context.getContentResolver().insert(ContactsContract.Groups.CONTENT_URI,
                    values);
            return ContentUris.parseId(groupsUri);
        } else {
            c.moveToFirst();
            return c.getLong(c.getColumnIndex(ContactsContract.Groups._ID));
        }
    }

    /**
     * add local contact by cloud server data
     */
    public static final void addLocalContact(Context context, Account account, CloudContact cloudContact,
                                             ArrayList<ContentProviderOperation> operations) {
        if (cloudContact.getiOperateStatus().equals(0)) {
            /**
             *  表示是删除的数据
             */
            return;
        }

        /**
         *  raw_contact表插入一行，用来占位
         */
        long rawContactId = insertEmptyRawContact(context, account, cloudContact.getcUuid());
        /**
         *  data表插入真实数据
         */
        insertData(context, cloudContact, account, rawContactId, operations);

    }

    final private static class DirtyQuery {

        public final static String[] PROJECTION = new String[]{

                ContactsContract.RawContacts._ID,
                /**
                 * SYNC2 save cUuid
                 */
                ContactsContract.RawContacts.SOURCE_ID,
                ContactsContract.RawContacts.DIRTY,
                ContactsContract.RawContacts.DELETED,
                ContactsContract.RawContacts.VERSION,

        };
        public final static int COLUMN_RAW_CONTACT_ID = 0;
        public final static int COLUMN_UUID = 1;
        public final static int COLUMN_DIRTY = 2;
        public final static int COLUMN_DELETED = 3;
        public final static int COLUMN_VERSION = 4;

        public static final Uri CONTENT_URI = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                .build();

        public static final String SELECTION =
                ContactsContract.RawContacts.DIRTY + "=1 AND "
                        + ContactsContract.RawContacts.ACCOUNT_TYPE + "='" + Constants.ACCOUNT_TYPE + "' AND "
                        + ContactsContract.RawContacts.ACCOUNT_NAME + "=?";

        private DirtyQuery() {
        }

    }

    /**
     * query
     */
    final private static class DataQuery {
        /**
         * content uri
         */
        public static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
        public static final String[] PROJECTION =
                new String[]{ContactsContract.Data._ID, ContactsContract.RawContacts.SOURCE_ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2, ContactsContract.Data.DATA3, ContactsContract.Data.DATA15, ContactsContract.Data.SYNC1};
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_SERVER_ID = 1;
        public static final int COLUMN_MIMETYPE = 2;
        public static final int COLUMN_DATA1 = 3;
        public static final int COLUMN_DATA2 = 4;
        public static final int COLUMN_DATA3 = 5;
        public static final int COLUMN_DATA15 = 6;
        public static final int COLUMN_SYNC1 = 7;
        public static final int COLUMN_PHONE_NUMBER = COLUMN_DATA1;
        public static final int COLUMN_PHONE_TYPE = COLUMN_DATA2;
        public static final int COLUMN_EMAIL_ADDRESS = COLUMN_DATA1;
        public static final int COLUMN_EMAIL_TYPE = COLUMN_DATA2;
        public static final int COLUMN_FULL_NAME = COLUMN_DATA1;
        public static final int COLUMN_GIVEN_NAME = COLUMN_DATA2;
        public static final int COLUMN_FAMILY_NAME = COLUMN_DATA3;
        public static final int COLUMN_AVATAR_IMAGE = COLUMN_DATA15;
        public static final int COLUMN_SYNC_DIRTY = COLUMN_SYNC1;
        public static final String SELECTION = ContactsContract.Data.RAW_CONTACT_ID + "=?";

        private DataQuery() {
        }
    }

    //update local contact
    public static synchronized long updateContacts(Context context, String account, List<CloudContact> rawContacts,
                                                   long groupId, long lastSyncMarker) {

        return 0;
    }


    public static long ensureSampleGroupExists(Context context, Account account) {
        final ContentResolver resolver = context.getContentResolver();

        long groupId = 0;

        final String[] projection = new String[]{ContactsContract.Groups._ID};
        final String selection = ContactsContract.Groups.ACCOUNT_NAME + "=? AND " + ContactsContract.Groups.ACCOUNT_TYPE +
                "=? AND " + ContactsContract.Groups.TITLE + "=?";
        final String[] selectionArgs = new String[]{account.name, account.type, SAMPLE_GROUP_NAME};
        final String sortOder = null;

        final Cursor cursor = resolver.query(ContactsContract.Groups.CONTENT_URI, projection, selection, selectionArgs,
                sortOder);




    }

}
