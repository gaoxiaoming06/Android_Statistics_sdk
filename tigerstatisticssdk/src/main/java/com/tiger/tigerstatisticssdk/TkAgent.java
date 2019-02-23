package com.tiger.tigerstatisticssdk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;

import com.tiger.tigerstatisticssdk.custom.ClickEvent;
import com.tiger.tigerstatisticssdk.custom.PageView;
import com.tiger.tigerstatisticssdk.custom.WebView;
import com.tiger.tigerstatisticssdk.network.NetStrategy;
import com.tiger.tigerstatisticssdk.util.CommonUtil;
import com.tiger.tigerstatisticssdk.util.CrashHandler;
import com.tiger.tigerstatisticssdk.util.OptionUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceForeverUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;
import com.tiger.tigerstatisticssdk.util.SSIDUtil;
import com.tiger.tigerstatisticssdk.util.TKIDUtil;

/**
 * Created by gao on 2017/5/26.
 */

public class TkAgent {

    public static final String TAG = "TkAgent";
    private static final PageView mPageView = new PageView();
    private static final WebView mWebView = new WebView();
    private static final ClickEvent mClickEvent = new ClickEvent();

    //构造方法私有
    private TkAgent() {
    }

    private static class SingletonHolder {
        private static final TkAgent INSTANCE = new TkAgent();
    }

    //获取单例
    public static TkAgent getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 设置log模式，测试设为true，线上设为false
     *
     * @param paramBoolean
     */
    public void setDebugMode(boolean paramBoolean) {
        TKLog.debugMode = paramBoolean;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public synchronized boolean init(Context context, TkAgent.Options options) {
        if (TextUtils.isEmpty(options.appkey)) {
            String value_appkey = CommonUtil.getAppMetaData(context, Constant.META_TIGER_APPKEY);
            options.appkey = value_appkey;
        }
        if (TextUtils.isEmpty(options.channel)) {
            String value_channel = CommonUtil.getAppMetaData(context, Constant.META_TIGER_CHANNEL);
            options.channel = value_channel;
        }
        if (context == null) {
            TKLog.e(TAG, "init fail, context is null");
            return false;
        } else if (options == null) {
            TKLog.e(TAG, "init fail, options is null");
            return false;
        } else if (TextUtils.isEmpty(options.appkey)) {
            TKLog.e(TAG, "init fail, appkey is null");
            return false;
        } else {
            try {
                //TODO 初始化
                initLocation(context);
                PreferenceUtil.getInstance().init(context);
                TKIDUtil.buildTKID(context);
                SSIDUtil.buildSSID();
                OptionUtil.buildOption(options);
                CrashHandler.getInstance().init(context);
                NetStrategy.checkIsToSend(context, true);
                return true;
            } catch (Exception exception) {
                TKLog.e(TAG, exception.getMessage());
                return false;
            }
        }
    }


    static class CusLocationListener implements LocationListener{
        private Context context;

        public CusLocationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                PreferenceForeverUtil.putString(context,"location",String.format("%f,%f", new Object[]{location.getLatitude(), location.getLongitude()}));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private CusLocationListener locationListener;
    private LocationManager locationManager;
    /**
     * 初始化定位
     * @param context
     */
    private void initLocation(final Context context){
        if (locationManager == null){
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    if (locationListener == null){
                        locationListener = new CusLocationListener(context);
                    }

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, locationListener);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 0, locationListener);
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        PreferenceForeverUtil.putString(context,"location",String.format("%f,%f", new Object[]{location.getLatitude(), location.getLongitude()}));
                    }
                }
            }
        } catch (Exception e) {}
    }

    /**
     * 移除定位监听
     */
    public void removeLocationListener(){
        if (locationManager == null || locationListener == null){
            return;
        }
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    /**
     * 进入页面
     *
     * @param context
     * @param name
     */
    public static void onPageStart(Context context, String name) {
        if (!TextUtils.isEmpty(name)) {
            mPageView.startPage(name);
        } else {
            TKLog.e(TAG, "pageName is null or empty");
        }
    }

    /**
     * 退出页面
     *
     * @param context
     * @param name
     */
    public static void onPageEnd(Context context, String name) {
        if (!TextUtils.isEmpty(name)) {
            mPageView.endPage(name);
        } else {
            TKLog.e(TAG, "pageName is null or empty");
        }
        NetStrategy.checkIsToSend(context, false);
    }

    /**
     * 事件点击
     *
     * @param context
     * @param eventId
     */
    public static void onEvent(Context context, String eventId) {
        mClickEvent.onClick(eventId);
        NetStrategy.checkIsToSend(context, false);
    }

    /**
     * url加载完毕
     *
     * @param context
     * @param url
     */
    public static void onWebComplete(Context context, String url, String title) {
        if (!TextUtils.isEmpty(url)) {
            mWebView.onUrlComplete(url, title);
        } else {
            TKLog.e(TAG, "name is null or empty");
        }
        NetStrategy.checkIsToSend(context, false);
    }

    /**
     * 用户userid赋值
     *
     * @param userId
     */
    public static void onUserSignIn(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            OptionUtil.getOption().setUid(userId);
        }
    }

    /**
     * 用户登出清除userid
     */
    public static void onUserSignOut() {
        OptionUtil.getOption().uid = "";
    }


    public static class Options {

        public String appkey = "";
        public String channel = "";
        public String uid = "";

        public Options() {
        }

        public TkAgent.Options setAppkey(String appkey) {
            this.appkey = appkey;
            return this;
        }

        public TkAgent.Options setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public TkAgent.Options setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public TkAgent.Options setHost(String host) {
            Constant.HOST = host;
            return this;
        }

    }

}
