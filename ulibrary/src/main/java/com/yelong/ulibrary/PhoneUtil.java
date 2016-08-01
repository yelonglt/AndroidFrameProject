package com.yelong.ulibrary;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 手机系统相关工具类
 * Created by eyetech on 16/6/8.
 * mail:354734713@qq.com
 */
public class PhoneUtil {

    /**
     * 判断设备是否是手机
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
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
        builder.append("screeninfo:(" + ScreenUtil.getScreenInfo(context)).append(")");

        return builder.toString();
    }

    /**
     * 获取手机联系人
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[] { "contact_id" },
                null, null, null);
        // 5.解析cursor
        while (cursor.moveToNext()) {
            // 6.获取查询的数据
            String contact_id = cursor.getString(0);
            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
            // 判断contact_id是否为空
            if (!TextUtils.isEmpty(contact_id)) {//null   ""
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                Cursor c = resolver.query(date_uri, new String[] { "data1",
                                "mimetype" }, "raw_contact_id=?",
                        new String[] { contact_id }, null);
                HashMap<String, String> map = new HashMap<String, String>();
                // 8.解析c
                while (c.moveToNext()) {
                    // 9.获取数据
                    String data1 = c.getString(0);
                    String mimetype = c.getString(1);
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        // 电话
                        map.put("phone", data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        // 姓名
                        map.put("name", data1);
                    }
                }
                // 11.添加到集合中数据
                list.add(map);
                // 12.关闭cursor
                c.close();
            }
        }
        // 12.关闭cursor
        cursor.close();
        return list;
    }
}
