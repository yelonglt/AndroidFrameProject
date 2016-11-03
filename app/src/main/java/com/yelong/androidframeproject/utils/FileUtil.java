package com.yelong.androidframeproject.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件操作工具类
 * Created by eyetech on 16/6/19.
 * mail:354734713@qq.com
 */
public class FileUtil {

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context    上下文
     * @param uniqueName 缓存目录名
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
