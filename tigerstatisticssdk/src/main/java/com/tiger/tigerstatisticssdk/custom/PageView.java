package com.tiger.tigerstatisticssdk.custom;

import android.text.TextUtils;

import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.util.OptionUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;
import com.tiger.tigerstatisticssdk.util.SSIDUtil;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gao on 2017/5/27.
 */

public class PageView {

    public static final String TAG = "PageView";

    private final Map<String, Long> page = new HashMap();
    private final ArrayList<Unit> c = new ArrayList();

    public void startPage(String name) {
        if (!TextUtils.isEmpty(name)) {
            synchronized (this.page) {
                this.page.put(name, System.currentTimeMillis());
            }
        }

    }

    public void endPage(String name) {
        if (!TextUtils.isEmpty(name)) {
            Long var2;
            synchronized (this.page) {
                var2 = this.page.remove(name);
            }

            if (var2 == null) {
                TKLog.e(TAG, String.format("please call \'onPageStart(%s)\' before onPageEnd", new Object[]{name}));
                return;
            }

            long activeTimeLong = System.currentTimeMillis() - var2;
            String activeTime = new BigDecimal(activeTimeLong).divide(new BigDecimal(1000),3, BigDecimal.ROUND_UP).toPlainString();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(var2);
            String time = formatter.format(date);

            ArrayList var5 = this.c;
            synchronized (this.c) {
                this.c.add(new Unit(name, time, activeTime, OptionUtil.getOption().uid, SSIDUtil.getSsid()));
            }

            //子线程存储数据
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        storageToSp();
                    } catch (Exception e) {
                        TKLog.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    private synchronized void storageToSp() {
        String activities = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_ACTIVITIES, null);
        StringBuilder var5 = new StringBuilder();
        if (!TextUtils.isEmpty(activities)) {
            var5.append(activities);
            var5.append(";");
        }

        ArrayList var6 = PageView.this.c;
        synchronized (PageView.this.c) {
            Iterator var8 = PageView.this.c.iterator();

            while (true) {
                if (!var8.hasNext()) {
                    PageView.this.c.clear();
                    break;
                }

                Unit var7 = (Unit) var8.next();
                var5.append(String.format("%s,%s,%s,%s,%s",
                        new Object[]{var7.className, var7.time, var7.activeTime, var7.uid, var7.ssId}));
                var5.append(";");
            }
        }
        var5.deleteCharAt(var5.length() - 1);
        PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_ACTIVITIES);
        PreferenceUtil.getInstance().putString(PreferenceUtil.KEY_ACTIVITIES, var5.toString());
        int num = PreferenceUtil.getInstance().getInt(PreferenceUtil.KEY_ACTIVITIES_NUMBER, 0);
        PreferenceUtil.getInstance().putInt(PreferenceUtil.KEY_ACTIVITIES_NUMBER, ++num);
    }

    public class Unit {
        public String className;
        public String time;
        public String activeTime;
        public String uid;
        public String ssId;

        public Unit(String className, String time, String activeTime, String uid, String ssId) {
            this.className = className;
            this.time = time;
            this.activeTime = activeTime;
            this.uid = uid;
            this.ssId = ssId;
        }
    }
}
