package com.dmall.mvp.pages.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("登录");
        //不重新设置导致设置失效
        setSupportActionBar(mToolbar);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        LoginFragment loginFragment = LoginFragment.newInstance(username);
        new LoginPresenter(loginFragment);
        return loginFragment;
    }
}
