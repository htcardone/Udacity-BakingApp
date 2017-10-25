package com.htcardone.baking.util;

import com.htcardone.baking.BuildConfig;

/**
 * Created by Henrique Cardone on 15/10/2017.
 */

public class Log {
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        android.util.Log.e(tag, msg, t);
    }
}
