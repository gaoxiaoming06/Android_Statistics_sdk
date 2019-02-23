package com.tiger.tigerstatisticssdk.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;

import com.tiger.tigerstatisticssdk.TKLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取数据，并合成接口需要的数据类型
 * Created by gao on 2017/5/27.
 */

public class DataProduceUtil {

    public static String produceData(Context context) {

        try {
            //首先尝试申请需要的动态权限
            if (context != null && context instanceof Activity) {
                if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        || (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        || (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
            }
        JSONObject object = new JSONObject();
        putDeviceInfoParam(context, object);
        boolean isListNull = putCustomParam(context, object);  //lists是否为空
        String data = object.toString();
        TKLog.i("DataProduceUtil", data);
        return isListNull ? Base64.encodeToString(data.getBytes(), Base64.DEFAULT) : null;
    } catch(
    Exception e)

    {
        e.printStackTrace();
        return null;
    }

}

    /**
     * 自定义属性  track--pageview
     *
     * @param context
     * @param object
     * @throws JSONException
     */
    private static boolean putCustomParam(Context context, JSONObject object) throws JSONException {
        JSONArray array = new JSONArray();
        /*pageview事件*/
        try {
            String str = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_ACTIVITIES, null);
            if (!TextUtils.isEmpty(str)) {
                String[] split = str.split(";");
                for (int i = 0; i < split.length; i++) {
                    JSONObject jo = new JSONObject();
                    String[] sp = split[i].split(",");
                    jo.put("className", sp[0]);
                    jo.put("time", sp[1]);
                    jo.put("activeTime", sp[2]);
                    jo.put("uid", sp[3]);
                    jo.put("ssId", sp[4]);
                    putBaseParam(context, jo, "track", "pageview");
                    array.put(jo);
                }
            }
        } catch (Exception e) {
            //报错了可能是sp中有错误，将错误数据清除
            PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_ACTIVITIES);
            e.printStackTrace();
        }
        /*click事件*/
        try {
            String str_click = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_CLICK, null);
            if (!TextUtils.isEmpty(str_click)) {
                String[] split = str_click.split(";");
                for (int i = 0; i < split.length; i++) {
                    JSONObject jo = new JSONObject();
                    String[] sp = split[i].split(",");
                    jo.put("eventId", sp[0]);
                    jo.put("time", sp[1]);
                    jo.put("uid", sp[2]);
                    jo.put("ssId", sp[3]);
                    putBaseParam(context, jo, "track", "click");
                    array.put(jo);
                }
            }
        } catch (Exception e) {
            //报错了可能是sp中有错误，将错误数据清除
            PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_CLICK);
            e.printStackTrace();
        }
        /*webviwe事件*/
        try {
            String str_web = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_WEB, null);
            if (!TextUtils.isEmpty(str_web)) {
                String[] split = str_web.split(";");
                for (int i = 0; i < split.length; i++) {
                    JSONObject jo = new JSONObject();
                    String[] sp = split[i].split(",");
                    jo.put("url", sp[0]);
                    jo.put("title", sp[1]);
                    jo.put("time", sp[2]);
                    jo.put("uid", sp[3]);
                    jo.put("ssId", sp[4]);
                    putBaseParam(context, jo, "track", "webview");
                    array.put(jo);
                }
            }
        } catch (Exception e) {
            //报错了可能是sp中有错误，将错误数据清除
            PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_WEB);
            e.printStackTrace();
        }
        /*error事件*/
        try {
            String str_error = PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_ERROR, null);
            if (!TextUtils.isEmpty(str_error)) {
                String[] split = str_error.split(";");
                for (int i = 0; i < split.length; i++) {
                    JSONObject jo = new JSONObject();
                    String[] sp = split[i].split(",");
                    jo.put("errorInfo", sp[0]);
                    jo.put("errorType", sp[1]);
                    jo.put("time", sp[2]);
                    jo.put("uid", sp[3]);
                    jo.put("ssId", sp[4]);
                    putBaseParam(context, jo, "track", "error");
                    array.put(jo);
                }
            }
        } catch (Exception e) {
            //报错了可能是sp中有错误，将错误数据清除
            PreferenceUtil.getInstance().removeByKey(PreferenceUtil.KEY_ERROR);
            e.printStackTrace();
        }
        object.put("list", array);
        //sp中取完数据之后 切换sp存储！
        TKLog.e("tklog", System.currentTimeMillis() + " change");
        PreferenceUtil.getInstance().changeSp();

        return array.length() > 0;
    }

    /**
     * app终端属性
     *
     * @param context
     * @param object
     * @throws JSONException
     */
    private static void putDeviceInfoParam(Context context, JSONObject object) throws JSONException {
        object.put("deviceName", CommonUtil.getDeviceName());
        object.put("deviceBrand", CommonUtil.getManufacturer());
        object.put("deviceModel", CommonUtil.getModel());
        object.put("isSocketProxy", CommonUtil.getProxyHost());
        object.put("canGps", CommonUtil.hasGPSDevice(context));
        object.put("location", CommonUtil.getLocationInfo(context));
        object.put("language", CommonUtil.getLanguage());
        object.put("networkState", CommonUtil.getNetTypeName(context));
        object.put("netCarrier", CommonUtil.getMobileType(context));
        object.put("devicePixel", CommonUtil.getScreenPixel(context));
        object.put("appVersion", CommonUtil.getAppVersionName(context));
        object.put("osVersion", CommonUtil.getOSVersion());
        object.put("imsi", CommonUtil.getIMSI(context));
        object.put("wifi_mac", CommonUtil.getWifiBSSID(context));
        object.put("imei", CommonUtil.getIMEI(context));
        object.put("androidId", CommonUtil.getAndroidID(context));
        object.put("isRoot", CommonUtil.isDeviceRooted());
        object.put("mac", CommonUtil.getMacAddress(context));
        object.put("isSimulator", CommonUtil.isEmulator(context));
        object.put("cpu_abi", CommonUtil.getCpuABI());
        object.put("fingerprint", CommonUtil.getFingerPrint());
        object.put("serial", CommonUtil.getDeviceSerial());
        object.put("isPrisonBreak", "");
        object.put("openUdid", "");
        object.put("adfa", "");
        object.put("adfv", "");
    }

    /**
     * 基础参数
     *
     * @param context
     * @param object
     * @param type
     * @param event
     * @throws JSONException
     */
    private static void putBaseParam(Context context, JSONObject object, String type, String event) throws JSONException {
        object.put("type", type);
        object.put("event", event);
        object.put("version", CommonUtil.getSDKVersionName(context));
        object.put("tkid", TKIDUtil.getTkid(context));
        object.put("channel", OptionUtil.getOption().channel);
    }
}
