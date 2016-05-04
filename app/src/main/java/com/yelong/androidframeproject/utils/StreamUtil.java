package com.yelong.androidframeproject.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流数据的转换
 * Created by eyetech on 16/4/29.
 */
public class StreamUtil {

    /**
     * 输入流转化成字符串，格式utf-8
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String inputStreamToString(InputStream inputStream)
            throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            return new String(outSteam.toByteArray(), "UTF-8");
        } finally {
            outSteam.close();
        }
    }

    /**
     * 输入流转化成字符串
     *
     * @param inputStream
     * @param charset
     * @return
     * @throws Exception
     */
    public static String inputStreamToString(InputStream inputStream,
                                             String charset) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            return new String(outSteam.toByteArray(), charset);
        } finally {
            outSteam.close();
        }
    }

    /**
     * 输入流转化为字节（二进制）
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            return outSteam.toByteArray();
        } finally {
            outSteam.close();
        }
    }

    /**
     * 字节数组转化成输入流
     *
     * @param in
     * @return
     */
    public static InputStream byteToInputStream(byte[] in) {
        return new ByteArrayInputStream(in);
    }

    /**
     * 字节数组转化成字符串
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String byteToString(byte[] in) throws Exception {
        // return new String(in);
        return inputStreamToString(byteToInputStream(in));
    }

}
