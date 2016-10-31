package com.yelong.androidframeproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.yelong.ulibrary.cache.ImageCache;
import com.yelong.ulibrary.cache.MemoryCache;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * 图片加载器
 * Created by yelong on 2016/10/31.
 * mail:354734713@qq.com
 */

public class ImageLoader {

    private ImageCache mImageCache = new MemoryCache();
    private ExecutorService mExecutorService = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //注入缓存实现，默认是内存缓存
    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    private void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            submitLoadRequest(url, imageView);
        }
    }

    /**
     * 到线程池中下载图片
     *
     * @param url       图片的url
     * @param imageView 显示图片的控件
     */
    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapForHttp(url);
                if (bitmap == null) return;

                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                    mImageCache.put(url, bitmap);
                }
            }
        });
    }


    private Bitmap getBitmapForHttp(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
