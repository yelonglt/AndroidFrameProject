package com.yelong.androidframeproject.net.cookie;

import com.yelong.androidframeproject.MainApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookies管理类
 * Created by yelong on 16/7/22.
 * mail:354734713@qq.com
 */
public class CookiesManager implements CookieJar {
    private final PersistentCookieStore mCookieStore = new PersistentCookieStore(MainApplication.getInstance());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                mCookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return mCookieStore.get(url);
    }
}
