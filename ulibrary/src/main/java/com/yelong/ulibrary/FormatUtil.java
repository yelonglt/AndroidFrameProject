package com.yelong.ulibrary;

import java.text.DecimalFormat;

/**
 * 格式化工具
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class FormatUtil {

    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = K * K * K;
    private static final long T = K * K * K * K;


    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size 数据大小
     * @return 格式化的字符串
     */
    public static String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < K) {
            return size + "bytes";
        } else if (size < M) {
            return format.format(size / 1024f) + "KB";
        } else if (size < G) {
            return format.format(size / 1024f / 1024f) + "MB";
        } else if (size < T) {
            return format.format(size / 1024f / 1024f / 1024f) + "GB";
        } else {
            return "size: error";
        }
    }

    /**
     * 返回kb的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getKBDataSize(long size) {
        return getDataSize(size * 1024);
    }

}
