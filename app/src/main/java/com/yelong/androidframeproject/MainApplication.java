package com.yelong.androidframeproject;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yelong.androidframeproject.exception.CrashHandler;
import com.yelong.androidframeproject.net.OkHttpClientConfig;
import com.yelong.androidframeproject.net.OkHttpClientManager;

import org.greenrobot.eventbus.EventBus;

/**
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

        setDoorState(AppConstants.STATE_OPEN);
    }

    public void setDoorState(@AppConstants.DoorState int state) {

    }
}
