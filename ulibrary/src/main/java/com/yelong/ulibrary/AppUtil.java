package com.yelong.ulibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.List;

/**
 * App工具类,包括判断程序的前后台,获取当前进程的包名
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class AppUtil {

    /**
     * 创建桌面快捷方式
     *
     * @param context 上下文
     * @param resId   桌面图标Id
     * @param strId   应用名文本Id
     */
    public static void addShortCut(Context context, @DrawableRes int resId, @StringRes int strId) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(strId));
        shortcut.putExtra("duplicate", false); // 不允许重复创建
        Intent target = new Intent(Intent.ACTION_MAIN).setClassName(context,
                context.getClass().getName());
        target.addCategory(Intent.CATEGORY_LAUNCHER); // 防止重复启动
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, resId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcut);
    }

    /**
     * 获取当前进程的包名
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        //获取当前的pid
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }

        return null;
    }

    /**
     * 判断程序在前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
