package com.yelong.androidframeproject.net.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 进度拦截器
 * Created by yelong on 2018/6/19.
 * mail:354734713@qq.com
 */
public class ProgressInterceptor implements Interceptor {
    private static final String TAG = ProgressInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new ProgressResponseBody(response.body(), new ProgressResponseBody.ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                Log.d(TAG, "onProgress: " + "progress:" + progress + "---->total:" + total + "---->done:" + done);
            }
        })).build();
    }
}
