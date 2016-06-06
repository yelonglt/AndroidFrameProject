package com.yelong.ulibrary.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件缓存
 * Created by eyetech on 16/6/6.
 * mail:354734713@qq.com
 */
public class FileCache {
    public static final String TAG = FileCache.class.getSimpleName();

    private File mCacheDir;
    private static final int MAX_CACHE_SIZE = 20 * 1024 * 1024; //20M
    private LruCache<String, Long> mFileCache = new LruCache<String, Long>(MAX_CACHE_SIZE) {
        @Override
        protected int sizeOf(String key, Long value) {
            return value.intValue();
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Long oldValue, Long newValue) {
            File file = getFile(key);
            if (file != null)
                file.delete();

        }
    };

    public FileCache(Context context) {
        mCacheDir = context.getCacheDir();
    }

    private File getFile(String fileName) {
        File file = new File(mCacheDir, fileName);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    //缓存bitmap到外部存储
    public boolean putBitmap(String key, Bitmap bitmap) {
        File file = getFile(key);
        if (file != null) {
            Log.v(TAG, "文件已经存在");
            return true;
        }
        FileOutputStream fos = getOutputStream(key);
        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "文件缓存失败");
            return false;
        }

        if (saved) {
            synchronized (mFileCache) {
                mFileCache.put(key, getFile(key).length());
            }
            return true;
        }
        return false;
    }

    //根据key获取OutputStream
    private FileOutputStream getOutputStream(String key) {
        if (mCacheDir == null)
            return null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mCacheDir.getAbsolutePath() + File.separator + key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fos;
    }

    //获取bitmap
    private static BitmapFactory.Options sBitmapOptions;

    static {
        sBitmapOptions = new BitmapFactory.Options();
        sBitmapOptions.inPurgeable = true; //bitmap can be purged to disk
    }

    public Bitmap getBitmap(String key) {
        try {
            File bitmapFile = getFile(key);
            if (bitmapFile != null) {
                return BitmapFactory.decodeStream(new FileInputStream(bitmapFile), null, sBitmapOptions);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
