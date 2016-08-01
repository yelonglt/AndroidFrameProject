package com.yelong.ulibrary;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 加密工具类
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class EncryptUtil {

    /**
     * MD5加密
     */
    public static String encryptMD5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return new BigInteger(md5.digest(data.getBytes())).toString(16);
    }

    /**
     * SHA加密
     */
    public static String encryptSHA(String data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        return new BigInteger(sha.digest(data.getBytes())).toString(32);
    }

    /**
     * 使用md5的算法进行加密,返回32位的字符串
     */
    public static String encryptMD5To32(String data) throws Exception {
        byte[] secretBytes = MessageDigest.getInstance("md5").digest(data.getBytes());

        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}
