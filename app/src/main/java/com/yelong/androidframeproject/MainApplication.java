package com.yelong.androidframeproject;

import android.app.Application;

/**
 * Created by eyetech on 16/4/17.
 */
public class MainApplication extends Application {

    public static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
