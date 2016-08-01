package com.dmall.mvp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基本的Fragment,方便Fragment的初始化以及跳转和返回
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    /**
     * 获取Fragment的布局文件Id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局文件组件
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    /**
     * 添加BaseFragment
     *
     * @param fragment
     */
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    /**
     * 移除最上层的Fragment
     */
    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }
}
