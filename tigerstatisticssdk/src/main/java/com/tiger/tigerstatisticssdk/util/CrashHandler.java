package com.tiger.tigerstatisticssdk.util;

import android.content.Context;
import com.tiger.tigerstatisticssdk.TKLog;
import com.tiger.tigerstatisticssdk.custom.ErrorEvent;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static final ErrorEvent mErrorEvent = new ErrorEvent();

    private static CrashHandler INSTANCE = new CrashHandler();
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        if (!(Looper.getMainLooper().getThread() == thread)) {
//            //子线程引发的异常并不会导致程序异常退出
//            return;
//        }

        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        } else {
            mErrorEvent.onError(ex);
        }
        TKLog.e(TAG,ex.getMessage());
        return false;
    }

}
