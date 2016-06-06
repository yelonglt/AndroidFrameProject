package com.yelong.ulibrary;

import java.text.DecimalFormat;

/**
 * 格式化工具
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class FormatUtil {

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return format.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return format.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return format.format(gbsize) + "GB";
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
        if (size < 0) {
            size = 0;
        }
        return getDataSize(size * 1024);
    }

}
