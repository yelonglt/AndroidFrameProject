package com.yelong.ulibrary;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 手机系统相关工具类
 * Created by eyetech on 16/6/8.
 * mail:354734713@qq.com
 */
public class PhoneUtil {

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

    /**
     * 获取设备唯一Id,Id由deviceId,shortId,androidId,macAddress组成
     *
     * @param context 上下文
     * @return
     */
    public static String getDeviceUniqueId(Context context) {
        String uniqueId = "";

        String deviceId = getDeviceId(context);

        String shortId = "35" + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10
                + Build.HOST.length() % 10 + Build.ID.length() % 10
                + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
                + Build.TYPE.length() % 10 + Build.USER.length() % 10;

        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String macAddress = getMacAddress(context);

        String longId = deviceId + shortId + androidId + macAddress;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(longId.getBytes(), 0, longId.length());
            byte p_md5Data[] = digest.digest();
            String uId = "";
            for (int i = 0; i < p_md5Data.length; i++) {
                int b = (0xFF & p_md5Data[i]);
                if (b < 0xF) uId += "0";

                uId += Integer.toHexString(b);
            }
            uniqueId = uId.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            uniqueId = deviceId;
        }

        return uniqueId;
    }

    /**
     * 获取手机的mac地址
     *
     * @param context 上下文
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo == null) return "";
        return wifiInfo.getMacAddress();
    }

    /**
     * 获取手机设备Id
     *
     * @param context 上下文
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId == null) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 获取手机系统信息
     *
     * @param context 上下文
     * @return
     */
    public static String getSystemInfo(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("device:" + Build.DEVICE).append(",");
        builder.append("display:" + Build.DISPLAY).append(",");
        builder.append("serial:" + Build.SERIAL).append(",");
        builder.append("sdk:" + Build.VERSION.SDK_INT).append(",");
        builder.append("codename:" + Build.VERSION.CODENAME).append(",");
        builder.append("cpu_abi:" + Build.CPU_ABI).append(",");
        builder.append("cpu_abi2:" + Build.CPU_ABI2).append(",");
        builder.append("manufacturer:" + Build.MANUFACTURER).append(",");
        builder.append("model:" + Build.MODEL).append(",");
        builder.append("release:" + Build.VERSION.RELEASE).append(",");
        builder.append("screeninfo:(" + getScreenInfo(context)).append(")");

        return builder.toString();
    }


}
