package com.yelong.ulibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司提示工具类,防止重复创建Toast和用户一直点击吐司导致很长时间没法消失
 * Created by eyetech on 16/4/17.
 */
public class ToastUtil {

    private static Toast mToast;

    /**
     * 短时间显示字符串
     *
     * @param context
     * @param info
     */
    public static void showShort(Context context, String info) {
        if (mToast == null) {
            mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            if (mToast.getDuration() == Toast.LENGTH_LONG) {
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.setText(info);
        }
        mToast.show();
    }

    /**
     * 长时间显示字符串
     *
     * @param context
     * @param info
     */
    public static void showLong(Context context, String info) {
        if (mToast == null) {
            mToast = Toast.makeText(context, info, Toast.LENGTH_LONG);
        } else {
            if (mToast.getDuration() == Toast.LENGTH_SHORT) {
                mToast.setDuration(Toast.LENGTH_LONG);
            }
            mToast.setText(info);
        }
        mToast.show();
    }
}
