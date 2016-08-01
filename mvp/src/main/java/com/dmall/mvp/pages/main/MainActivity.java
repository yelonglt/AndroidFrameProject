package com.dmall.mvp.pages.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dmall.mvp.base.AppActivity;
import com.dmall.mvp.base.BaseFragment;

public class MainActivity extends AppActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        MainFragment mainFragment = new MainFragment();
        new MainPresenter(mainFragment);
        return mainFragment;
    }
}
