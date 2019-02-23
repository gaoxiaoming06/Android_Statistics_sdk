package com.tiger.tigerstatisticssdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tiger.tigerstatisticssdk.TKLog;

/**
 * 需上传数据存储
 * Created by gao on 2017/5/26.
 */

public class PreferenceUtil {

    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_ACTIVITIES_NUMBER = "activities_number";
    public static final String KEY_CLICK = "clicks";
    public static final String KEY_WEB = "webs";
    public static final String KEY_ERROR = "error";

    private static final String TAG = "PreferenceUtil";

    private static PreferenceUtil instance = new PreferenceUtil();
    private final String SHARED_FILE_NAME_A = "tiger_analysis_a";
    private final String SHARED_FILE_NAME_B = "tiger_analysis_b";

    private Context mContext;
    private SharedPreferences sp_a;
    private SharedPreferences sp_b;

    private PreferenceUtil() {
    }

    public static PreferenceUtil getInstance() {
        return instance;
    }

    public void init(Context context) {
        if(context != null) {
            this.mContext = context;
        }

        sp_a = this.mContext.getSharedPreferences(SHARED_FILE_NAME_A, Context.MODE_PRIVATE);
        sp_b = this.mContext.getSharedPreferences(SHARED_FILE_NAME_B, Context.MODE_PRIVATE);
    }

    /**
     * 改变操作哪个sp
     */
    public void changeSp(){
        Log.e(TAG, "changeSp: ");
        TKLog.e("tklog", System.currentTimeMillis()+" change");
        SharedPreferences.Editor edit = sp_a.edit();
        edit.putBoolean("witch_sp",!sp_a.getBoolean("witch_sp",false));
        edit.commit();
    }

    private SharedPreferences getSp(){
        return sp_a.getBoolean("witch_sp",false) ? sp_a : sp_b;
    }

    private SharedPreferences getPreSp(){
        return sp_a.getBoolean("witch_sp",false) ? sp_b : sp_a;
    }

    public void putString(String key,String value) {
        TKLog.e("tklog", System.currentTimeMillis()+" putString "+value + " "+getSp());
        SharedPreferences.Editor editor = getSp().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key,String defaultValue) {
        TKLog.e("tklog", System.currentTimeMillis()+" getString "+key);
        return this.getSp().getString(key, defaultValue);
    }

    public void putInt(String key,int value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.getSp().getInt(key, defaultValue);
    }

    public void removeByKey(String key) {
        TKLog.e("tklog", System.currentTimeMillis()+" removeByKey "+key);
        SharedPreferences.Editor editor = getSp().edit();
        editor.remove(key);
        editor.commit();
    }

    public void removeAll() {
        TKLog.e("tklog", System.currentTimeMillis()+" removeAll ");
        SharedPreferences.Editor editor = getPreSp().edit();
        editor.remove(KEY_ACTIVITIES);
        editor.remove(KEY_ACTIVITIES_NUMBER);
        editor.remove(KEY_CLICK);
        editor.remove(KEY_WEB);
        editor.remove(KEY_ERROR);
        editor.commit();
    }
}
