package com.tiger.tigerstatisticssdk.util;


import android.text.TextUtils;

import java.util.UUID;

/**
 * SSID的生成与存储
 * Created by gao on 2017/6/2.
 */

public class SSIDUtil {

    private static String ssid;

    public static String getSsid(){
        if(TextUtils.isEmpty(ssid)){
            ssid = UUID.randomUUID().toString();
        }
        return ssid;
    }

    public static void buildSSID(){
        ssid = UUID.randomUUID().toString();
    }

}
