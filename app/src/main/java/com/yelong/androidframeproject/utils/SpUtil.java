package com.yelong.androidframeproject.utils;

/**
 * SharedPreferences辅助工具类
 * Created by eyetech on 16/4/26.
 */
public class SpUtil {

    public static boolean getFirstOpen() {
        return YLSharedPreferences.getInstance().getBoolean(YLSharedPreferences.FIRST_OPEN, false);
    }

    public static void setFirstOpen(boolean state) {
        YLSharedPreferences.getInstance().putBoolean(YLSharedPreferences.FIRST_OPEN, state);
    }
}
