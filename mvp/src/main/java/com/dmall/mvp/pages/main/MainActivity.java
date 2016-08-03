package com.dmall.mvp.pages.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dmall.mvp.base.AppActivity;
import com.dmall.mvp.base.BaseFragment;

public class MainActivity extends AppActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("Main");
        //不重新设置导致设置失效
        setSupportActionBar(mToolbar);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        MainFragment mainFragment = new MainFragment();
        new MainPresenter(mainFragment);
        return mainFragment;
    }
}
