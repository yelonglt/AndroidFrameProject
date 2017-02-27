package com.yelong.androidframeproject.helper;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * 视图帮助类
 * Created by yelong on 2017/2/27.
 * mail:354734713@qq.com
 */
public class ViewHelper {

    /**
     * 给图片着色
     *
     * @param drawable 图片
     * @param color    颜色
     * @return 着色之后的图片
     */
    public static Drawable tintDrawable(@NonNull Drawable drawable, @ColorInt int color) {
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawableSelector(int normalColor, int pressedColor, int cornerRadius) {
        return new RippleDrawable(ColorStateList.valueOf(pressedColor),
                getRippleMask(normalColor, cornerRadius),
                getRippleMask(normalColor, cornerRadius));
    }

    private static Drawable getRippleMask(int color, int cornerRadius) {
        //数组是八个弧度值指定四个角的弧度
        float[] outerRadius = new float[8];
        Arrays.fill(outerRadius, cornerRadius);
        RoundRectShape roundRectShape = new RoundRectShape(outerRadius, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    /**
     * 获取选中和正常颜色的图片
     *
     * @param normalColor  正常颜色
     * @param pressedColor 选中、聚焦、按下的颜色
     * @return 图片状态集合
     */
    public static StateListDrawable getStateListDrawable(int normalColor, int pressedColor, int cornerRadius) {
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(normalColor);
        normalDrawable.setCornerRadius(cornerRadius);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(pressedColor);
        pressedDrawable.setCornerRadius(cornerRadius);

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_enabled}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_activated}, pressedDrawable);
        states.addState(new int[]{android.R.attr.state_selected}, pressedDrawable);
        states.addState(new int[]{}, normalDrawable);
        return states;
    }

    /**
     * 获取文本选中和正常的颜色
     *
     * @param normalColor  正常颜色
     * @param pressedColor 选中、聚焦、按下的颜色
     * @return 颜色状态集合
     */
    public static ColorStateList getTextSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }

}
