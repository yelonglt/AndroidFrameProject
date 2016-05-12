package com.yelong.androidframeproject.adapter;

import android.content.Context;
import android.view.View;

import com.yelong.androidframeproject.adapter.rv.CommonAdapter;
import com.yelong.androidframeproject.adapter.rv.ViewHolder;
import com.yelong.androidframeproject.model.User;

import java.util.List;

/**
 * Created by eyetech on 16/5/12.
 * mail:354734713@qq.com
 */
public class SimpleAdapter extends CommonAdapter<User> {


    public SimpleAdapter(Context context, int layoutId, List<User> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, User user) {

        holder.setText(-1, "");
        holder.setOnClickListener(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
