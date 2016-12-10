package com.yelong.androidframeproject;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Activity生命周期回调方法
 * Created by yelong on 2016/12/10.
 * mail:354734713@qq.com
 */

public abstract class SimpleActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
