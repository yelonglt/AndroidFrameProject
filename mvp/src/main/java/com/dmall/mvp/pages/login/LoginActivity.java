package com.dmall.mvp.pages.login;

import android.content.Intent;
import android.os.Bundle;

import com.dmall.mvp.base.AppActivity;
import com.dmall.mvp.base.BaseFragment;

/**
 * 登录页面,承载Fragment的载体
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class LoginActivity extends AppActivity {

    private String username;

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            username = bundle.getString("username");
        }
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return LoginFragment.newInstance(username);
    }
}
