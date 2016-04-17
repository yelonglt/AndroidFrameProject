package com.yelong.androidframeproject.utils;

import android.util.Log;

/**
 * Created by eyetech on 16/4/17.
 */
public class LogUtil {
    /**
     * 是否显示log信息
     */
    public static final boolean showLog = true;

    public static void v(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable thr) {
        if (showLog) {
            Log.v(tag, buildMessage(msg), thr);
        }
    }

    public static void d(String tag, String msg) {
        if (showLog) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable thr) {
        if (showLog) {
            Log.d(tag, buildMessage(msg), thr);
        }
    }

    public static void i(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable thr) {
        if (showLog) {
            Log.i(tag, buildMessage(msg), thr);
        }
    }

    public static void w(String tag, String msg) {
        if (showLog) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable thr) {
        if (showLog) {
            Log.w(tag, buildMessage(msg), thr);
        }
    }

    public static void e(String tag, String msg) {
        if (showLog) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable thr) {
        if (showLog) {
            Log.e(tag, buildMessage(msg), thr);
        }
    }

    /**
     * Building Message
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace()
                .getStackTrace()[2];

        return new StringBuilder().append(caller.getClassName()).append(".")
                .append(caller.getMethodName()).append("(): ").append(msg)
                .toString();
    }
}
