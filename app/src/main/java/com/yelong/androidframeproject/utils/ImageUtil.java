package com.yelong.androidframeproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yelong.androidframeproject.MainApplication;
import com.yelong.ulibrary.MD5Util;
import com.yelong.ulibrary.cache.DiskLruCache;
import com.yelong.ulibrary.cache.MemoryCache;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 三级缓存工具类，内存缓存、磁盘缓存、网络缓存
 * Created by eyetech on 16/6/17.
 * mail:354734713@qq.com
 */
public class ImageUtil {

    private static volatile ImageUtil sImageUtil;

    public static final int DISK_MAX_SIZE = 10 * 1024 * 1024;

    private MemoryCache mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private ImageUtil() {
        mMemoryCache = new MemoryCache(8);

        try {
            mDiskLruCache = DiskLruCache.open(FileUtil.getDiskCacheDir(MainApplication.getInstance(),
                    "MyCache"), DeviceInfo.getAppVersionCode(), 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageUtil getInstance() {
        //创建一个临时变量，访问速度更快
        ImageUtil tempInstance = sImageUtil;
        if (tempInstance == null) {
            synchronized (ImageUtil.class) {
                tempInstance = sImageUtil;
                if (tempInstance == null) {
                    sImageUtil = new ImageUtil();
                    tempInstance = sImageUtil;
                }
            }
        }
        return tempInstance;
    }

    /**
     * 获取图片1.首先在内存缓存中取 2.文件缓存中取 3.从服务器获取
     *
     * @param url
     * @return
     */
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = mMemoryCache.getBitmapToMemoryCache(url);
        if (bitmap == null) {
            String key = MD5Util.md5(url);
            try {
                if (mDiskLruCache.get(key) != null) {
                    // 从DiskLruCahce取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null) {
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        // 存入LruCache缓存
                        mMemoryCache.addBitmapToMemoryCache(url, bitmap);
                    }
                    return bitmap;
                } else {
                    bitmap = getBitmapForHttp(url);
                    putBitmapToMemoryAndDiskCache(url, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public Bitmap getBitmapForHttp(String url) {
        //开启网络下载
        return null;
    }

    /**
     * 把从服务器获取的图片存入两级缓存（内存缓存，磁盘缓存）
     *
     * @param url
     * @param bitmap
     */
    public void putBitmapToMemoryAndDiskCache(String url, Bitmap bitmap) {
        // 存入LruCache缓存
        mMemoryCache.addBitmapToMemoryCache(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Util.md5(url);
        try {
            if (mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
