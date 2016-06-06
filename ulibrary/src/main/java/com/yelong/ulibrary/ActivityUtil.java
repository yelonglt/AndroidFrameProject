package com.yelong.ulibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * 界面工具类
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class ActivityUtil {

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
}
