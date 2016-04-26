package com.yelong.androidframeproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.utils.SpUtil;

import java.lang.ref.WeakReference;

/**
 * 闪屏页面
 * Created by eyetech on 16/4/26.
 */
public class SplashActivity extends Activity {

    private static class StaticHandler extends Handler {

        private WeakReference<SplashActivity> mActivityWeakReference;

        public StaticHandler(SplashActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                //do something
                if (msg.what == 2000) {
                    if (SpUtil.getFirstOpen()) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    } else {
                        SpUtil.setFirstOpen(true);
                        Intent intent = new Intent(activity, SplashIndexImgsActivity.class);
                        activity.startActivity(intent);
                    }

                    activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    activity.finish();
                }
            }
        }
    }

    private Handler mHandler = new StaticHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        mHandler.sendEmptyMessageDelayed(2000, 2000);
    }


}
