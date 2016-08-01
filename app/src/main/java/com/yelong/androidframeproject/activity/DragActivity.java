package com.yelong.androidframeproject.activity;

import android.os.Bundle;

import com.yelong.androidframeproject.R;

/**
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class DragActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        setToolbarTitle("测试拖拽");
    }
}
