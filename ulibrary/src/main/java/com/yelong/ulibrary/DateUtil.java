package com.yelong.ulibrary;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 日期工具类,使用ThreadLocal解决多线程并发问题
 * Created by yelong on 2016/12/26.
 * mail:354734713@qq.com
 */

public class DateUtil {

    public static final String TAG = DateUtil.class.getSimpleName();
    private static Map<String, ThreadLocal<SimpleDateFormat>> formatMap = new HashMap<>();

    public static final String YMD = "yyyyMMdd";
    public static final String YMD_ = "yyyy-MM-dd";
    public static final String HMS = "HHmmss";

    /**
     * 根据map中的key得到对应线程的sdf实例
     *
     * @param pattern 格式
     * @return 实例
     */
    private static SimpleDateFormat getFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdfThreadLocal = formatMap.get(pattern);
        if (sdfThreadLocal == null) {
            synchronized (DateUtil.class) {
                sdfThreadLocal = formatMap.get(pattern);
                if (sdfThreadLocal == null) {
                    Log.i(TAG, "put new SimpleDateFormat of pattern " + pattern + " to map");
                    sdfThreadLocal = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            Log.i(TAG, "thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern, Locale.getDefault());
                        }
                    };
                }
                formatMap.put(pattern, sdfThreadLocal);
            }
        }
        return sdfThreadLocal.get();
    }

    /**
     * 按照指定的pattern解析日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return 解析后的Date实例
     */
    public static Date parseDate(String date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        try {
            return getFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "解析的格式不支持: " + pattern);
        }
        return null;
    }

    /**
     * 按照指定pattern格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return 解析后格式
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            return getFormat(pattern).format(date);
        }
    }

}
