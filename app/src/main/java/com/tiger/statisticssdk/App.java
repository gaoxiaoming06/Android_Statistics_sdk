package com.tiger.statisticssdk;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tiger.tigerstatisticssdk.TkAgent;

/**
 * Created by gao on 2017/5/27.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        TkAgent.getInstance().setDebugMode(true);
        TkAgent.getInstance().init(this,new TkAgent.Options().setChannel("tiger"));
    }
}
