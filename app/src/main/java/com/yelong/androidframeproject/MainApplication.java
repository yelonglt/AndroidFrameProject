package com.yelong.androidframeproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yelong.androidframeproject.exception.CrashHandler;
import com.yelong.androidframeproject.net.OkHttpClientConfig;
import com.yelong.androidframeproject.net.OkHttpClientManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义Application
 * Created by eyetech on 16/4/17.
 */
public class MainApplication extends Application {

    public static MainApplication instance;

    public List<Activity> mActivityList;

    public int activityCounter = 0;

    public static MainApplication getInstance() {
        return instance;
    }

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    //获取App全局唯一的EventBus实例
    private static EventBus mEventBus;

    public static EventBus getEventBus() {
        if (mEventBus == null) {
            mEventBus = EventBus.builder()
                    .throwSubscriberException(BuildConfig.DEBUG)
                    .addIndex(new EventBusIndex()).installDefaultEventBus();
        }
        return mEventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mRefWatcher = LeakCanary.install(this);

        //收集异常信息
        CrashHandler.getInstance().init(this);
        //初始化OkHttp
        OkHttpClientManager.getInstance().init(OkHttpClientConfig
                .getInstance().addLogger("HH").addCookie()
                /*.addCertificates(getAssets().open("srca.cer"))*/
                /*.addCertificates(getResources().openRawResource(R.raw.srca))*/
                .builder());

        init();

        setDoorState(AppConstants.STATE_OPEN);
    }

    /**
     * 初始化相关操作
     */
    public void init() {
        mActivityList = Collections.synchronizedList(new ArrayList<Activity>());
        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                mActivityList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                super.onActivityStarted(activity);
                activityCounter += 1;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                super.onActivityStopped(activity);
                activityCounter -= 1;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                mActivityList.remove(activity);
            }
        });
    }

    /**
     * 判断App是否在后台
     */
    public boolean isAppBackground() {
        return activityCounter == 0;
    }

    /**
     * 退出整个App，避免重新启动
     */
    public void exitApp() {
        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
        mActivityList.clear();
        System.exit(0);
    }

    public void setDoorState(@AppConstants.DoorState int state) {

    }
}
