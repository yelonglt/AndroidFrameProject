package com.yelong.ulibrary.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * 内存缓存
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class MemoryCache {
    public static final String TAG = MemoryCache.class.getSimpleName();
    //硬引用
    private LruCache<String, Bitmap> mLruCache;
    //软引用
    private static final int SOFT_CACHE_CAPACITY = 40;
    private final static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_CAPACITY, 0.75f, true) {
        @Override
        public SoftReference<Bitmap> put(String key, SoftReference<Bitmap> value) {
            return super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
            if (size() > SOFT_CACHE_CAPACITY) {
                return true;
            }
            return false;
        }
    };

    /**
     * 构造函数
     *
     * @param split 设置几分之一内存
     */
    public MemoryCache(int split) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / split;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                Log.i(TAG, "LruCache is full,push to SoftCache");
                //硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
                mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
    }

    /**
     * 添加图片到内存缓存
     *
     * @param key
     * @param bitmap
     * @return
     */
    public boolean addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (bitmap != null && getBitmapToMemoryCache(key) == null) {
            mLruCache.put(key, bitmap);
            return true;
        }
        return false;
    }

    /**
     * 根据key获取内存缓存中的Bitmap
     * @param key
     * @return
     */
    public Bitmap getBitmapToMemoryCache(String key) {
        Bitmap bitmap = mLruCache.get(key);
        if (bitmap != null) {
            return bitmap;
        }
        //硬引用缓存区间中读取失败，从软引用缓存区间读取
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapSoftReference = mSoftCache.get(key);
            if (bitmapSoftReference != null) {
                bitmap = bitmapSoftReference.get();
                if (bitmap != null) {
                    return bitmap;
                } else {
                    Log.i(TAG, "soft reference is recycled");
                    mSoftCache.remove(key);
                }
            }
            return null;
        }
    }
}
