package com.yelong.ulibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by eyetech on 16/4/17.
 */
public class ToastUtil {
    /**
     * 短时间显示字符串
     *
     * @param context
     * @param info
     */
    public static void showShort(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示字符串
     *
     * @param context
     * @param info
     */
    public static void showLong(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示整型
     *
     * @param context
     * @param info
     */
    public static void showShort(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示整型
     *
     * @param context
     * @param info
     */
    public static void showLong(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
}
