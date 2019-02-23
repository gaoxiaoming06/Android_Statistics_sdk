package com.tiger.tigerstatisticssdk.network;


import com.tiger.tigerstatisticssdk.Constant;
import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by gao on 2017/5/26.
 */
public class HttpUrlConnection {


    //构造方法私有
    private HttpUrlConnection() {
    }

    //获取单例
    public static HttpUrlConnection getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void request(final String token, final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Constant.HOST + Constant.ADDRESS);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(15 * 1000);
                    String body = "token=" + URLEncoder.encode(token) + "&data=" + URLEncoder.encode(data);
                    conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(body.getBytes());
                    TKLog.i("HttpUrlConnection",body);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        PreferenceUtil.getInstance().removeAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpUrlConnection INSTANCE = new HttpUrlConnection();
    }
}