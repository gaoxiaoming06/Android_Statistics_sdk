package com.tiger.tigerstatisticssdk.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 比较固定的信息存储
 * Created by gao on 2017/6/2.
 */

public class PreferenceForeverUtil {

    public static final String KEY_TKID = "tkid";

    private static final String TAG = "PreferenceForeverUtil";

    public static String PREFERENCE_NAME = "tiger_analysis_config";


    public static boolean putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }
}
