package com.tiger.tigerstatisticssdk.network;

import android.content.Context;

import com.tiger.tigerstatisticssdk.util.DataProduceUtil;
import com.tiger.tigerstatisticssdk.util.PreferenceUtil;

/**
 * 网络发送策略类
 * Created by gao on 2017/5/26.
 */

public class NetStrategy {

    public static int SEND_MODE = 0;
    public static int ACT_NUM = 10; //缓存数据最大条数
    public static int SEND_TIME = 10*60*1000; //发送数据最大时间间隔

    private static long lastRequestTime;

    /**
     * 检查是否发送数据
     * @param isAppLaunch 如果是刚启动，必发送
     */
    public static void checkIsToSend(Context context, boolean isAppLaunch){
        if(isAppLaunch){
            LaunchRequest.getInstance().sendRequest(DataProduceUtil.produceData(context));
            lastRequestTime = System.currentTimeMillis();
        }else {
            switch (SEND_MODE){
                case 0:
                    sendByNormal(context);
                    break;
                case 1:
                    sendByOther1(context);
                    break;
                case 2:
                    sendByOther2(context);
                    break;
            }
        }
    }

    /**
     * 正常数据发送方式
     * @param context
     */
    private static void sendByNormal(Context context) {
        int aNum = PreferenceUtil.getInstance().getInt(PreferenceUtil.KEY_ACTIVITIES_NUMBER, 0);
        if(aNum >= ACT_NUM){
            LaunchRequest.getInstance().sendRequest(DataProduceUtil.produceData(context));
            lastRequestTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - lastRequestTime >= SEND_TIME){
            LaunchRequest.getInstance().sendRequest(DataProduceUtil.produceData(context));
            lastRequestTime = System.currentTimeMillis();
        }
    }

    /**
     * 其他数据发送方式
     * @param context
     */
    private static void sendByOther1(Context context) {

    }

    /**
     * 其他数据发送方式
     * @param context
     */
    private static void sendByOther2(Context context) {

    }

}
