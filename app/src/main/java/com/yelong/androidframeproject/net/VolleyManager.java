package com.yelong.androidframeproject.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley管理类
 * Created by eyetech on 16/5/17.
 * mail:354734713@qq.com
 */
public class VolleyManager {
    private static VolleyManager instance;
    private RequestQueue mQueue;
    private Context mContext;

    private VolleyManager(Context context) {
        this.mContext = context;
        mQueue = getRequestQueue();
    }

    public static VolleyManager getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleyManager.class) {
                instance = new VolleyManager(context);
            }
        }
        return instance;
    }

    /**
     * 获取Volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mQueue;
    }

    /**
     * 添加请求到请求队列
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(mContext.getClass().getSimpleName());
        getRequestQueue().add(req);
    }

    /**
     * 取消所有的任务
     */
    public void cancelTask() {
        getRequestQueue().cancelAll(mContext.getClass().getSimpleName());
    }
}