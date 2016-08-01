package com.dmall.mvp.base;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * 基本的Activity,主要用于Activity添加和删除Fragment
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 获取Activity的布局文件Id
     *
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 获取添加Fragment的容器Id
     *
     * @return
     */
    protected abstract int getFragmentContentId();


    /**
     * 添加fragment
     *
     * @param fragment fragment需要继承BaseFragment
     */
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 移除fragment
     */
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
