package com.yelong.androidframeproject.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.yelong.androidframeproject.MainApplication;

/**
 * 手机详细信息
 * Created by eyetech on 16/5/26.
 * mail:354734713@qq.com
 */
public class DeviceInfo {

    public static String deviceName = Build.MODEL;
    public static String osName = "Android";
    public static String osVersion = Build.VERSION.RELEASE;


    /**
     * 获取包管理器
     *
     * @return
     */
    public static PackageManager getPackageManager() {
        return MainApplication.getInstance().getPackageManager();
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getPackageName() {
        return MainApplication.getInstance().getPackageName();
    }

    /**
     * 获取元数据的Bundle对象
     *
     * @return
     */
    public static Bundle getMetaData() {
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取应用版本名
     *
     * @return
     */
    public static String getAppVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取应用版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 获取屏幕的尺寸，
     *
     * @return 字符串格式如：720*1280
     */
    public static String getScreenSize() {
        DisplayMetrics dm = MainApplication.getInstance().getResources().getDisplayMetrics();
        return String.valueOf(dm.widthPixels) + "*" + String.valueOf(dm.heightPixels);
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = MainApplication.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = MainApplication.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
