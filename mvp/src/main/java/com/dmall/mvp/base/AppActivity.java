package com.dmall.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dmall.mvp.BuildConfig;
import com.dmall.mvp.R;

/**
 * 直接让App所有的Activity来实现
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public abstract class AppActivity extends BaseActivity {

    public static final String TAG = AppActivity.class.getSimpleName();

    /**
     * 获取Activity添加的第一个BaseFragment
     *
     * @return
     */
    protected abstract BaseFragment getFirstFragment();

    /**
     * 处理接收到的Intent参数
     *
     * @param intent
     */
    protected void handleIntent(Intent intent) {
        if (BuildConfig.MLOG) {
            Log.d(TAG, "当前Acitvity正在解析Intent参数");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
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
