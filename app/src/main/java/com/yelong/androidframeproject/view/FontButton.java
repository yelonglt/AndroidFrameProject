package com.yelong.androidframeproject.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.yelong.androidframeproject.helper.TypeFaceHelper;
import com.yelong.androidframeproject.helper.ViewHelper;

/**
 * 显示指定字体的按钮
 * Created by yelong on 2017/2/27.
 * mail:354734713@qq.com
 */
public class FontButton extends AppCompatButton {

    public FontButton(Context context) {
        super(context);
        init();
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        TypeFaceHelper.applyTypeface(this);
    }

    /**
     * 设置背景颜色
     *
     * @param normalColor  正常颜色
     * @param pressedColor 选中颜色
     */
    public void setBackground(@ColorRes int normalColor, @ColorRes int pressedColor) {
        int nColor = ContextCompat.getColor(getContext(), normalColor);
        int pColor = ContextCompat.getColor(getContext(), pressedColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundDrawable(ViewHelper.getDrawableSelector(nColor, pColor, 12));
        } else {
            setBackgroundDrawable(ViewHelper.getStateListDrawable(nColor, pColor, 12));
        }
    }

    /**
     * 设置文本颜色
     *
     * @param normalColor  正常颜色
     * @param pressedColor 选中颜色
     */
    public void setTextColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        int nColor = ContextCompat.getColor(getContext(), normalColor);
        int pColor = ContextCompat.getColor(getContext(), pressedColor);
        setTextColor(ViewHelper.getTextSelector(nColor, pColor));
    }
}
