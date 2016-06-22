package com.yelong.androidframeproject.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.login.LoginCarrier;
import com.yelong.androidframeproject.login.LoginInterceptor;

import java.lang.ref.WeakReference;

/**
 * Created by eyetech on 16/6/20.
 * mail:354734713@qq.com
 */
public class LoginActivity extends BaseActivity {

    private static class StaticHandler extends Handler {

        private WeakReference<LoginActivity> mActivityWeakReference;

        public StaticHandler(LoginActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                if (msg.what == 100) {
                    //启动目标页面
                    activity.mCarrier.invoke(activity);
                }
            }
        }
    }

    private Handler mHandler = new StaticHandler(this);

    private LoginCarrier mCarrier;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbarTitle("登录");

        mCarrier = getIntent().getParcelableExtra(LoginInterceptor.mINVOKER);
        mTextView = (TextView) findViewById(R.id.text_text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSuccess();
            }
        });
    }

    private void loginSuccess() {
        mHandler.sendEmptyMessage(100);
    }

}
