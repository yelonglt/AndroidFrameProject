package com.yelong.androidframeproject.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.yelong.androidframeproject.R;

/**
 * 网络加载进度条
 * Created by eyetech on 16/5/17.
 * mail:354734713@qq.com
 */
public class NetProgressDialog extends ProgressDialog {
    public NetProgressDialog(Context context) {
        super(context);
    }

    public NetProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_net_progress);
    }
}
