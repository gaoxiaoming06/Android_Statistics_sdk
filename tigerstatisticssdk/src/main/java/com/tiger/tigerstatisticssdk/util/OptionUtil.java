package com.tiger.tigerstatisticssdk.util;


import com.tiger.tigerstatisticssdk.TkAgent;


/**
 * app层传递信息的提供类
 * Created by gao on 2017/6/2.
 */

public class OptionUtil {

    private static TkAgent.Options mOptions;

    public static TkAgent.Options getOption(){
        if(mOptions == null){
            return new TkAgent.Options();
        }
        return mOptions;
    }

    public static void buildOption(TkAgent.Options options){
        mOptions = options;
    }

}
