package com.yelong.androidframeproject.net.interceptor;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 缓存拦截器
 * Created by yelong on 16/7/22.
 * mail:354734713@qq.com
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
        return response.newBuilder().removeHeader("Pragma")
                .header("Cache-Control", String.format(Locale.getDefault(), "max-age=%d", 60 * 60 * 12 * 30)).build();

    }
}
