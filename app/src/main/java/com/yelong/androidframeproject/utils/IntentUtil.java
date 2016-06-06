package com.yelong.androidframeproject.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
}
