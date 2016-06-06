package com.yelong.ulibrary;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * 意图工具类
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class IntentUtil {
    public static final String TAG = IntentUtil.class.getSimpleName();

    /**
     * 打开网页
     *
     * @param context
     * @param url
     */
    public static void openURL(Context context, String url) {
        try {
            if (!url.contains("http://")) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.i(TAG, "open url fail...");
            e.printStackTrace();
        }
    }

    /**
     * 延迟跳转意图
     *
     * @param context
     * @param cls
     * @param time
     */
    public static void delayIntent(Context context, Class<?> cls, int time) {
        Intent intent = new Intent(context, cls);
        PendingIntent pIntent = PendingIntent.getActivity(context, 1000,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + time,
                pIntent);
    }

    /**
     * 打开拨号盘
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    public static void openDial(Context context, String phoneNumber) {
        if (!VerifyUtil.isMobileNum(phoneNumber)) return;
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 拨打电话
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    public static void callPhone(Context context, String phoneNumber) {
        if (!VerifyUtil.isMobileNum(phoneNumber)) return;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 安装APK
     *
     * @param context 上下文
     * @param file    安装文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}
