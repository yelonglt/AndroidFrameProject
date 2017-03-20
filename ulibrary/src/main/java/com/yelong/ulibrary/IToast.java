package com.yelong.ulibrary;

import android.view.View;

/**
 * Created by yelong on 2017/3/20.
 * mail:354734713@qq.com
 */
public interface IToast {
    IToast setGravity(int gravity, int xOffset, int yOffset);
    IToast setDuration(long durationMillis);
    IToast setView(View view);
    IToast setMargin(float horizontalMargin, float verticalMargin);
    IToast setText(String text);
    void show();
    void cancel();
}
