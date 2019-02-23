package com.tiger.tigerstatisticssdk.custom;

import android.text.TextUtils;
import android.util.Base64;

import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.util.OptionUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;
import com.tiger.tigerstatisticssdk.util.SSIDUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gao on 2017/6/7.
 */

public class ErrorEvent {

    public static final String TAG = "ErrorEvent";

    private Unit mUnit;

    public void onError(Throwable error) {

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        error.printStackTrace(printWriter);
        String errorType = error.toString();
        Throwable cause = error.getCause();
        while (cause != null) {
            errorType = cause.toString();
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String errorInfo = writer.toString();

        if(!TextUtils.isEmpty(errorInfo)) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String time = formatter.format(date);

            //errorInfo使用base64加密,避免后端解析出错  Base64.NO_WRAP参数避免不必要的换行符
            errorInfo = Base64.encodeToString(errorInfo.getBytes(),Base64.NO_WRAP);
            TKLog.e("ErrorEvent",errorInfo);

            //errorType 使用正则过滤json以及=>相关字符
            String regEx="[\\[\\]{}'=>\"\":]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(errorType);
            errorType = m.replaceAll(" ").trim();

            mUnit = new Unit(errorInfo,errorType,time,OptionUtil.getOption().uid ,SSIDUtil.getSsid());

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
        String clicks = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_ERROR, null);
        StringBuilder var5 = new StringBuilder();
        if(!TextUtils.isEmpty(clicks)) {
            var5.append(clicks);
            var5.append(";");
        }
        var5.append(String.format("%s,%s,%s,%s,%s",
                new Object[]{mUnit.errorInfo,mUnit.errorType, mUnit.time,mUnit.uid,mUnit.ssId}));

        PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_ERROR);
        PreferenceUtil.getInstance().putString(PreferenceUtil.KEY_ERROR, var5.toString());
    }

    public class Unit{
        public String errorInfo;
        public String errorType;
        public String time;
        public String uid;
        public String ssId;

        public Unit(String errorInfo,String errorType, String time,String uid,String ssId) {
            this.errorInfo = errorInfo;
            this.errorType = errorType;
            this.time = time;
            this.uid = uid;
            this.ssId = ssId;
        }
    }
}
