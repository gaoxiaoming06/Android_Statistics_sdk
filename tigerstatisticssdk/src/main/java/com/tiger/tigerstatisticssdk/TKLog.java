package com.tiger.tigerstatisticssdk;

import android.util.Log;

/**
 * Created by gao on 2017/5/26.
 */

public class TKLog {
    public static boolean debugMode = false;

    public TKLog() {
    }

    public static void d(String tag, String msg) {
        if(msg != null) {
            if(debugMode) {
                Log.d(tag,msg);
            }

        }
    }

    public static void e(String tag, String msg) {
        if(msg != null) {
            if(debugMode) {
                Log.e(tag,msg);
            }

        }
    }

    public static void i(String tag, String msg) {
        if(msg != null) {
            if(debugMode) {
                Log.i(tag,msg);
            }

        }
    }

    public static void v(String tag, String msg) {
        if(msg != null) {
            if(debugMode) {
                Log.v(tag,msg);
            }

        }
    }

    public static void w(String tag, String msg) {
        if(msg != null) {
            if(debugMode) {
                Log.w(tag,msg);
            }

        }
    }
}
