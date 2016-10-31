package com.yelong.ulibrary.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.yelong.ulibrary.CloseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 磁盘缓存
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class DiskCache implements ImageCache {
    private static final String TAG = DiskCache.class.getSimpleName();

    private File mCacheDir;
    private static final int MAX_CACHE_SIZE = 10 * 1024 * 1024; //10M
    private final LruCache<String, Long> mLruCache = new LruCache<String, Long>(MAX_CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, Long value) {
            return value.intValue();
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Long oldValue, Long newValue) {
            File file = getFile(key);
            if (file != null) file.delete();

        }
    };

    private static BitmapFactory.Options sBitmapOptions;

    static {
        sBitmapOptions = new BitmapFactory.Options();
        sBitmapOptions.inPurgeable = true; //bitmap can be purged to disk
    }

    public DiskCache(Context context) {
        //====/data/data/<application package>/cache
        mCacheDir = context.getCacheDir();
    }

    @Override
    public Bitmap get(String url) {
        try {
            File file = getFile(url);
            if (file != null) {
                return BitmapFactory.decodeStream(new FileInputStream(file), null, sBitmapOptions);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean put(String url, Bitmap bitmap) {
        File file = getFile(url);
        if (file != null) {
            Log.v(TAG, "文件已经存在");
            return true;
        }
        FileOutputStream fos = getOutputStream(url);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "文件缓存失败");
            return false;
        } finally {
            CloseUtil.closeQuietly(fos);
        }

        synchronized (mLruCache) {
            mLruCache.put(url, getFile(url).length());
        }
        return true;
    }

    private File getFile(String fileName) {
        File file = new File(mCacheDir, fileName);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    private FileOutputStream getOutputStream(String url) {
        if (mCacheDir == null) return null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mCacheDir.getAbsolutePath() + File.separator + url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fos;
    }
}
