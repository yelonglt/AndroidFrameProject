package com.yelong.androidframeproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yelong.androidframeproject.exception.CrashHandler;
import com.yelong.androidframeproject.helper.TypeFaceHelper;
import com.yelong.androidframeproject.net.OkHttpClientConfig;
import com.yelong.androidframeproject.net.OkHttpClientManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 自定义Application
 * Created by eyetech on 16/4/17.
 */
public class MainApplication extends Application {

    public static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    private static class EventBusHolder {
        //获取App全局唯一的EventBus实例
        private static EventBus mEventBus = EventBus.builder()
                .throwSubscriberException(BuildConfig.DEBUG)
                .addIndex(new EventBusIndex()).installDefaultEventBus();
    }

    public static EventBus getEventBus() {
        return EventBusHolder.mEventBus;
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
                .getInstance().addLogger(null).addCookie()
                .addNetworkProgress()
                /*.addCertificates(getAssets().open("srca.cer"))*/
                /*.addCertificates(getResources().openRawResource(R.raw.srca))*/
                .builder());

        init();

        setDoorState(AppConstants.STATE_OPEN);
        TypeFaceHelper.generateTypeface(this);
    }

    /**
     * 初始化相关操作
     */
    public void init() {
        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                ActivityStack.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                super.onActivityStarted(activity);
                ActivityStack.getInstance().activityCounterPlusOne();
            }

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                ActivityStack.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                super.onActivityStopped(activity);
                ActivityStack.getInstance().activityCounterSubtractOne();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                ActivityStack.getInstance().removeActivity(activity);
            }
        });
    }

    public void setDoorState(@AppConstants.DoorState int state) {

    }
}
