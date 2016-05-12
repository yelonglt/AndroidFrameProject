package com.yelong.androidframeproject.adapter.rv;

import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView的item点击事件
 * Created by eyetech on 16/5/10.
 * mail:354734713@qq.com
 */
public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
