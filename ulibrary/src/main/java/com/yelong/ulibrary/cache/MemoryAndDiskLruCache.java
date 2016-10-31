package com.yelong.ulibrary.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yelong.ulibrary.EncryptUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 两级缓存,内存缓存和DiskLruCache
 * Created by yelong on 2016/10/31.
 * mail:354734713@qq.com
 */

public class MemoryAndDiskLruCache implements ImageCache {

    private MemoryCache mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private static final int DISK_MAX_SIZE = 10 * 1024 * 1024;

    public MemoryAndDiskLruCache(File directory, int appVersion) {
        mMemoryCache = new MemoryCache();
        try {
            mDiskLruCache = DiskLruCache.open(directory, appVersion, 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            try {
                String key = EncryptUtil.encryptMD5To32(url);
                if (mDiskLruCache.get(key) != null) {
                    // 从DiskLruCahce取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null) {
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        // 存入LruCache缓存
                        mMemoryCache.put(url, bitmap);
                    }
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public boolean put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        try {
            String key = EncryptUtil.encryptMD5To32(url);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
