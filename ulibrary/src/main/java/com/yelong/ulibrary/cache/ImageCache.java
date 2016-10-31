package com.yelong.ulibrary.cache;

import android.graphics.Bitmap;

/**
 * 图片缓存接口类
 * Created by yelong on 2016/10/31.
 * mail:354734713@qq.com
 */

public interface ImageCache {

    /**
     * 根据url从缓存中获取bitmap对象
     *
     * @param url 图片url
     * @return bitmap对象
     */
    Bitmap get(String url);

    /**
     * 把bitmap对象按照url存到缓存中
     *
     * @param url    图片url
     * @param bitmap bitmap对象
     * @return 是否成功
     */
    boolean put(String url, Bitmap bitmap);
}
