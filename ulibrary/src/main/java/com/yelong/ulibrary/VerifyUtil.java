package com.yelong.ulibrary;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * Created by eyetech on 16/4/27.
 */
public class VerifyUtil {

    //验证手机号
    private static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
    //验证邮箱
    private static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //验证url
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    //验证IP地址
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    //验证汉字
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    //验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";


    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        return isRegularMatch(REGEX_MOBILE, mobiles);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return isRegularMatch(REGEX_EMAIL, email);
    }

    /**
     * 验证url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        return isRegularMatch(REGEX_URL, url);
    }

    /**
     * 验证IP地址
     *
     * @param address
     * @return
     */
    public static boolean isIpAddress(String address) {
        return isRegularMatch(REGEX_IP, address);
    }

    /**
     * 验证汉字
     *
     * @param hanzi
     * @return
     */
    public static boolean isChz(String hanzi) {
        return isRegularMatch(REGEX_CHZ, hanzi);
    }

    /**
     * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     *
     * @param username
     * @return
     */
    public static boolean isUsername(String username) {
        return isRegularMatch(REGEX_USERNAME, username);
    }


    /**
     * 正则匹配
     *
     * @param regex
     * @param string
     * @return
     */
    private static boolean isRegularMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

}
