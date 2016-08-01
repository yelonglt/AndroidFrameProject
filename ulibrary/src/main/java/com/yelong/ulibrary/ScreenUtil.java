package com.yelong.ulibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * 手机屏幕工具类
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class ScreenUtil {

    /**
     * 获取手机屏幕的宽度
     *
     * @param context 上下文
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕的高度
     *
     * @param context 上下文
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取状态栏高度＋标题栏(ActionBar)高度
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取手机屏幕的信息，包括widthPixels,heightPixels,densityDpi,density
     *
     * @param context 上下文
     * @return
     */
    public static String getScreenInfo(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        StringBuilder builder = new StringBuilder();
        builder.append("widthPixels:" + metrics.widthPixels).append(",");
        builder.append("heightPixels:" + metrics.heightPixels).append(",");
        builder.append("densityDpi:" + metrics.densityDpi).append(",");
        builder.append("density:" + metrics.density);

        return builder.toString();
    }
}
