package com.tiger.tigerstatisticssdk.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.tiger.tigerstatisticssdk.BuildConfig;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by gao on 2017/5/26.
 */

public class CommonUtil {
    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * get app version name
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        if (context == null) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    return pi.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * get app version name
     *
     * @param context
     * @return
     */
    public static String getSDKVersionName(Context context) {
        if (context == null) {
            return null;
        }
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionName;
    }

    /**
     * 终端标识
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {

            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机名称
     *
     * @return 手机型号
     */

    public static String getDeviceName() {
        String user = Build.USER;
        if (TextUtils.isEmpty(user)) {
            user = "";
        }
        String display = Build.DISPLAY;
        if (TextUtils.isEmpty(display)) {
            display = "";
        }
        return user + "," + display;
    }

    /**
     * 获取设备厂商
     * <p>如Xiaomi</p>
     *
     * @return 设备厂商
     */

    public static String getManufacturer() {
        return TextUtils.isEmpty(Build.MANUFACTURER) ? "" : Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * <p>如MI2SC</p>
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取代理设置信息
     *
     * @return
     */
    public static int getProxyHost() {
        String proHost = android.net.Proxy.getDefaultHost();
        if (TextUtils.isEmpty(proHost))
            proHost = System.getProperty("http.proxyHost");
        return TextUtils.isEmpty(proHost) ? 0 : 1;
    }

    /**
     * 检测是否有gps模块
     *
     * @return
     */
    public static int hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return 0;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return 0;
        return providers.contains(LocationManager.GPS_PROVIDER) ? 1 : 0;
    }

    /**
     * 获取系统语言
     *
     * @return
     */
    public static String getLanguage() {
//        Locale locale = App.getInstance().getApplicationContext().getResources().getConfiguration().locale;
//        String language = locale.getLanguage();
//        return language;
        return TextUtils.isEmpty(Locale.getDefault().toString()) ? "" : Locale.getDefault().toString();
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getNetTypeName(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo.isConnected()) {
            return "WIFI";
        } else {
            ConnectivityManager conManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return "未知";
            }
            int subtype = networkInfo.getSubtype();
            switch (subtype) {
                case 1:
                case 2:
                case 4:
                case 11:
                case 7:
                    return "2G";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                    return "3G";
                default:
                    return "4G";
            }
        }
    }

    /**
     * 获取网络运营商
     *
     * @return
     */
    public static String getMobileType(Context context) {
        String type = "";
        TelephonyManager iPhoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iNumeric = iPhoneManager.getSimOperator();
        if (iNumeric.length() > 0) {
            if (iNumeric.equals("46000") || iNumeric.equals("46002")) {
                type = "中国移动";
            } else if (iNumeric.equals("46001")) {
                type = "中国联通";
            } else if (iNumeric.equals("46003")) {
                type = "中国电信";
            }
        }
        return type;
    }

    /**
     * 获取分辨率
     *
     * @return
     */
    public static String getScreenPixel(Context context) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            return dm.heightPixels + "," + dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取操作系统版本及API
     *
     * @return
     */
    public static String getOSVersion() {
        String release = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(release)) {
            release = "";
        }
        String sdk = Build.VERSION.SDK;
        if (TextUtils.isEmpty(sdk)) {
            sdk = "";
        }
        return release + "," + sdk;
    }

    /**
     * 获取IMSI
     *
     * @return
     */
    public static String getIMSI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TextUtils.isEmpty(telephonyManager.getSubscriberId()) ? "" : telephonyManager.getSubscriberId();//获取手机IMSI号
        }
        return "";
    }


    /**
     * 获取wifi mac地址
     *
     * @return
     */
    public static String getWifiBSSID(Context context) {
        try {

            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getBSSID();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return TextUtils.isEmpty(tm.getDeviceId()) ? "" : tm.getDeviceId();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取设备AndroidID
     *
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        String and_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(and_id) ? "" : and_id;
    }

    /**
     * 判断设备是否root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public static String isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return "1";
            }
        }
        return "0";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC地址
     */
    public static String getMacAddress(Context context) {
        String macAddress = getMacAddressByWifiInfo(context);
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return MAC地址
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo(Context context) {
        try {

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC地址
     */
    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址
     *
     * @return MAC地址
     */
    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    if (result.successMsg != null) {
                        return result.successMsg;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
     */
    public static int isEmulator(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return EmulatorUtils.CheckEmulatorBuild() ||
                    EmulatorUtils.CheckPipes() ||
                    EmulatorUtils.CheckDeviceIDS(context) ||
                    EmulatorUtils.CheckEmulatorFiles() ||
                    EmulatorUtils.CheckImsiIDS(context) ||
                    EmulatorUtils.CheckOperatorNameAndroid(context) ||
                    EmulatorUtils.CheckQEmuDriverFile() ||
                    EmulatorUtils.CheckPhoneNumber(context) ? 1 : 0;
        }
        return 1;
    }

    /**
     * 获取CPU_abi
     *
     * @return
     */
    public static String getCpuABI() {
        String abi = Build.CPU_ABI;
        String abi2 = Build.CPU_ABI2;
        if (TextUtils.isEmpty(abi))
            abi = "";
        if (TextUtils.isEmpty(abi2))
            abi2 = "";
        return "abi:" + abi + ",abi2:" + abi2;
    }

    /**
     * 获取设备指纹
     *
     * @return
     */
    public static String getFingerPrint() {
        return TextUtils.isEmpty(Build.FINGERPRINT) ? "" : Build.FINGERPRINT;
    }

    /**
     * 设备序列号
     *
     * @return
     */
    public static String getDeviceSerial() {
        return TextUtils.isEmpty(Build.SERIAL) ? "" : Build.SERIAL;
    }

    /**
     * 获取经纬度
     */
    public static String getLocationInfo(Context context) {
        return PreferenceForeverUtil.getString(context,"location","");
    }
    /**
     * 获取经纬度
     */
    public static String getLocation(Context context) {
        double latitude = 0.0;
        double longitude = 0.0;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        return String.format("%f,%f", new Object[]{latitude, longitude});
                    }
                } else {
                    LocationListener locationListener = new LocationListener() {
                        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        // Provider被enable时触发此函数，比如GPS被打开
                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        // Provider被disable时触发此函数，比如GPS被关闭
                        @Override
                        public void onProviderDisabled(String provider) {
                        }

                        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                        @Override
                        public void onLocationChanged(Location location) {
                            if (location != null) {
                            }
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude(); //经度
                        longitude = location.getLongitude(); //纬度
                        return String.format("%f,%f", new Object[]{latitude, longitude});
                    }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

}
