package com.yelong.androidframeproject.activity;

import android.os.Bundle;

import com.yelong.androidframeproject.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbarTitle("主界面");
    }
}
