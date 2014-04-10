package com.snail.contact.sync.util;

import android.accounts.Account;
import android.util.Log;
import com.snail.contact.sync.contact.CloudContact;
import com.snail.contact.sync.contact.RawContact;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghb on 4/1/14.
 */
public class NetworkUtilities {

    private static final String TAG = "NetworkUtilities";

    private static final boolean DUG = true;

    private static final String CODE = "code";

    private static final int AUTHENTICATION_FAILED = 1008;

    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;

    public static final String BASE_URL = "http://10.206.0.59";

    public static final String DOWNLOAD_CONTACTS_URI = BASE_URL + "/mobile/platform/cloud/storage/user/contact/list";

    public static final String UPLOAD_CONTACTS_URI = BASE_URL + "/mobile/platform/cloud/storage/user/contact";

    public static final String ARGS = "args";


    public static HttpClient getHttpClient() {

        HttpClient httpClient = new DefaultHttpClient();

        final HttpParams params = httpClient.getParams();

        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);

        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);

        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);

        return httpClient;

    }

    public static List<CloudContact> syncContacts(Account account, List<RawContact> dirtyContacts) {


        return null;
    }


    public static List<RawContact> downloadContact() throws Exception {

        final ArrayList<RawContact> serverDirtyList = new ArrayList<RawContact>();

        HttpParams params = new BasicHttpParams();

        params.setParameter(ARGS, ARGS);

        final HttpGet get = new HttpGet(DOWNLOAD_CONTACTS_URI);

        get.setParams(params);

        final HttpResponse resp = getHttpClient().execute(get);

        final String response = EntityUtils.toString(resp.getEntity());

        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            JSONObject mJSONObject = new JSONObject(response);

            if (mJSONObject.getInt(CODE) == AUTHENTICATION_FAILED) {

                Log.e(TAG, "Authentication Failed");

                throw new AuthenticationException();
            }

//            final JSONArray serverContacts = new JSONArray(response);

            if (DUG) {
                Log.d(TAG, response);
            }

//            for (int i = 0; i < serverContacts.length(); i++) {

            RawContact rawContact = null;

            if (rawContact != null) {

                serverDirtyList.add(rawContact);
            }
//            }

        } else {

            Log.e(TAG, "Server error in sending dirty contacts: " + resp.getStatusLine());

            throw new IOException();

        }

        return serverDirtyList;

    }

    public static void uploadContact(RawContact rawContact) throws Exception {

        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair(RawContact.Constant.cUuid, rawContact.getcUuid()));

        params.add(new BasicNameValuePair(RawContact.Constant.sContactFirstName, rawContact.getsContactFirstName()));

        params.add(new BasicNameValuePair(RawContact.Constant.sContactLastName, rawContact.getsContactLastName()));

        params.add(new BasicNameValuePair(RawContact.Constant.dLastModifyTime, rawContact.getdLastModifyTime()));

//        params.add(new BasicNameValuePair(RawContact.Constant.phone, rawContact.getPhone()));

//        params.add(new BasicNameValuePair(RawContact.Constant.group, rawContact.getGroup()));

//        params.add(new BasicNameValuePair(RawContact.Constant.email, rawContact.getEmail()));

        params.add(new BasicNameValuePair(RawContact.Constant.sContactData, rawContact.getsContactData()));

        params.add(new BasicNameValuePair(RawContact.Constant.status, rawContact.getStatus()));

        params.add(new BasicNameValuePair(RawContact.Constant.sContactFullName, rawContact.getsContactFullName()));

        params.add(new BasicNameValuePair(RawContact.Constant.sComment, rawContact.getsComment()));

        params.add(new BasicNameValuePair(RawContact.Constant.sNickname, rawContact.getsNickname()));

        params.add(new BasicNameValuePair(RawContact.Constant.dBirthday, rawContact.getdBirthday()));

//        params.add(new BasicNameValuePair(RawContact.Constant.address, rawContact.getAddress()));

//        params.add(new BasicNameValuePair(RawContact.Constant.website, rawContact.getWebsite()));

        final HttpPost post = new HttpPost(UPLOAD_CONTACTS_URI);

        final HttpResponse resp = getHttpClient().execute(post);

        final String response = EntityUtils.toString(resp.getEntity());

        if (DUG) {

            Log.d(TAG, response);

        }

        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            JSONObject mJSONObject = new JSONObject(response);

            if (mJSONObject.getInt(CODE) == AUTHENTICATION_FAILED) {

                Log.e(TAG, "Authentication Failed");

                throw new AuthenticationException();
            }

        } else {

            Log.e(TAG, "Server error in sending dirty contacts: " + resp.getStatusLine());

            throw new IOException();
        }

    }

}
