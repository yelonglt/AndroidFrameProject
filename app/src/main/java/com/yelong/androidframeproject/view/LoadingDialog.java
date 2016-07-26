package com.yelong.androidframeproject.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yelong.androidframeproject.R;

/**
 * 加载对话框
 * Created by yelong on 16/7/26.
 * mail:354734713@qq.com
 */
public class LoadingDialog {

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_layout);
        ImageView loadingImage = (ImageView) view.findViewById(R.id.loading_image);
        TextView tipMessage = (TextView) view.findViewById(R.id.tip_message);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(
                context, R.anim.dialog_loading);
        // 使用ImageView显示动画
        loadingImage.startAnimation(animation);
        if (TextUtils.isEmpty(msg)) {
            tipMessage.setVisibility(View.GONE);
        } else {
            tipMessage.setVisibility(View.VISIBLE);
            tipMessage.setText(msg);
        }

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }
}
