package com.yelong.ulibrary.cache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 两级缓存,内存缓存和磁盘缓存
 * Created by yelong on 2016/10/31.
 * mail:354734713@qq.com
 */

public class MemoryAndDiskCache implements ImageCache {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public MemoryAndDiskCache(Context context) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public boolean put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
        return true;
    }
}
