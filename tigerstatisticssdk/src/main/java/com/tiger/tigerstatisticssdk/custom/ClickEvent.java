package com.tiger.tigerstatisticssdk.custom;

import android.text.TextUtils;

import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.util.OptionUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;
import com.tiger.tigerstatisticssdk.util.SSIDUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by gao on 2017/5/27.
 */

public class ClickEvent {

    public static final String TAG = "ClickEvent";

    private Unit mUnit;

    public void onClick(String eventId) {
        if(!TextUtils.isEmpty(eventId)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = formatter.format(date);

            mUnit = new Unit(eventId,time, SSIDUtil.getSsid(),OptionUtil.getOption().uid);

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
        String clicks = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_CLICK, null);
        StringBuilder var5 = new StringBuilder();
        if(!TextUtils.isEmpty(clicks)) {
            var5.append(clicks);
            var5.append(";");
        }
        var5.append(String.format("%s,%s,%s,%s",
                new Object[]{mUnit.eventId, mUnit.time,mUnit.uid,mUnit.ssId}));

        PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_CLICK);
        PreferenceUtil.getInstance().putString(PreferenceUtil.KEY_CLICK, var5.toString());
    }


    public class Unit{
        public String eventId;
        public String time;
        public String ssId;
        public String uid;

        public Unit(String eventId, String time,String ssId,String uid) {
            this.eventId = eventId;
            this.time = time;
            this.ssId = ssId;
            this.uid = uid;
        }
    }
}
