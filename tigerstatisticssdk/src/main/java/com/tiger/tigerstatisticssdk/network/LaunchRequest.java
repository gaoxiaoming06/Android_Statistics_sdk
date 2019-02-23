package com.tiger.tigerstatisticssdk.network;


import android.text.TextUtils;

import com.tiger.tigerstatisticssdk.util.OptionUtil;

/**
 * Created by gao on 2017/5/26.
 */

public class LaunchRequest {

    private static LaunchRequest instance = new LaunchRequest();

    private LaunchRequest() {
    }

    public static LaunchRequest getInstance() {
        return instance;
    }

    /**
     * 开始请求入口
     * @param data
     */
    public void sendRequest(String data){
        if(!TextUtils.isEmpty(data)){
            HttpUrlConnection.getInstance().request(OptionUtil.getOption().appkey,data);
        }
    }


}
