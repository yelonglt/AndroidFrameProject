package com.dmall.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.dmall.mvp.R;

/**
 * 直接让App所有的Activity来实现
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public abstract class AppActivity extends BaseActivity {

    /**
     * 获取Activity添加的第一个BaseFragment
     */
    protected abstract BaseFragment getFirstFragment();

    /**
     * 处理接收到的Intent参数
     *
     * @param intent 参数
     */
    protected void handleIntent(Intent intent) {

    }

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }
}
