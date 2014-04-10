package com.snail.contact.sync.util;

import android.util.Log;

/**
 * Created by fenghb on 2014/4/10.
 */
public class LogUtil {
    public static void d(String tag, String name, String value) {
        Log.d(tag, "------ " + name + " : " + String.valueOf(value) + " ------");
    }

    public static void d(String tag, String name, int value) {
        Log.d(tag, "------ " + name + " : " + String.valueOf(value) + " ------");
    }


}
