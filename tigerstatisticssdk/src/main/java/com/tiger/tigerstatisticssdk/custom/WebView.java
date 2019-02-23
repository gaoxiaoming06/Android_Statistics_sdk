package com.tiger.tigerstatisticssdk.custom;

import android.text.TextUtils;

import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.util.OptionUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;
import com.tiger.tigerstatisticssdk.util.SSIDUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by gao on 2017/5/27.
 */

public class WebView {

    public static final String TAG = "WebView";

    private final ArrayList<Unit> mList = new ArrayList();

    public void onUrlComplete(String url,String title) {
        if(!TextUtils.isEmpty(url)) {

            if(TextUtils.isEmpty(title)){
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = formatter.format(date);

            mList.add(new Unit(url,title,time, SSIDUtil.getSsid(), OptionUtil.getOption().uid));

            //子线程存储数据
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        storageToSp();
                    } catch (Exception e) {
                        TKLog.e(TAG,e.getMessage());
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    private synchronized void storageToSp() {
        String webs = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_WEB, null);
        StringBuilder var5 = new StringBuilder();
        if(!TextUtils.isEmpty(webs)) {
            var5.append(webs);
            var5.append(";");
        }

        ArrayList var6 = WebView.this.mList;
        synchronized(WebView.this.mList) {
            Iterator var8 = WebView.this.mList.iterator();

            while(true) {
                if(!var8.hasNext()) {
                    WebView.this.mList.clear();
                    break;
                }

                Unit var7 = (Unit)var8.next();
                var5.append(String.format("%s,%s,%s,%s,%s",
                        new Object[]{var7.url,var7.title, var7.time,var7.uid,var7.ssId}));
                var5.append(";");
            }
        }
        var5.deleteCharAt(var5.length() - 1);
        PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_WEB);
        PreferenceUtil.getInstance().putString(PreferenceUtil.KEY_WEB, var5.toString());
    }


    public class Unit{
        public String url;
        public String title;
        public String time;
        public String ssId;
        public String uid;

        public Unit(String url, String title, String time,String ssId,String uid) {
            this.url = url;
            this.title = title;
            this.time = time;
            this.ssId = ssId;
            this.uid = uid;
        }
    }
}
