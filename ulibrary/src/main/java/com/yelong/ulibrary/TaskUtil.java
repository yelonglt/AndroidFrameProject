package com.yelong.ulibrary;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.List;

/**
 * 后台进程工具类
 *
 * @author 800hr：yelong
 *         <p>
 *         2015-6-12
 */
public class TaskUtil {
    /**
     * 关闭所有的进程
     *
     * @param context
     */
    public static void killAllProcess(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningapps = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : runningapps) {
            String packname = info.processName;
            am.killBackgroundProcesses(packname);
        }

    }

    /**
     * 得到所有的进程数目，进程分为用户进程和系统进程
     *
     * @param context
     * @return 返回得到的进程数目
     */
    public static int getProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningapps = am.getRunningAppProcesses();
        return runningapps.size();

    }

    /**
     * 得到所有的应用进程占的内存大小
     *
     * @param context
     */
    public static String getMemeorySize(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return FormatUtil.getDataSize(outInfo.availMem);
    }
}
