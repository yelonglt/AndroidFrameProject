package com.yelong.androidframeproject.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * 图片工具类
 * Created by eyetech on 16/6/2.
 * mail:354734713@qq.com
 */
public class DrawableUtil {

    /**
     * 获取着色后的图片，通过xml设置（android:tint="#FFFF0000"）
     *
     * @param context   上下文
     * @param resId     着色的图片
     * @param tintColor 给图片着色的color
     * @return
     */
    public static Drawable getTintDrawable(Context context, @DrawableRes int resId, @ColorRes int tintColor) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);

        Drawable.ConstantState state = drawable.getConstantState();
        //wrap方法：使用tint就必须调用该方法对Drawable进行一次包装
        //mutate方法：如果不调用该方法，我们进行操作的就是原drawable，着色之后原drawable也改变的.类似拷贝
        Drawable newDrawable = DrawableCompat.wrap(state == null ? drawable : state.newDrawable().mutate());
        newDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        DrawableCompat.setTint(newDrawable, ContextCompat.getColor(context, tintColor));

        return newDrawable;
    }

    /**
     * 获取两种状态的图片，记得图片控件设置可点击（android:clickable="true"）
     *
     * @param context         上下文
     * @param resId           着色的图片
     * @param tintNormalColor 正常状态下的图片颜色
     * @param tintPressColor  选中状态下的图片颜色
     * @return
     */
    public static Drawable getStateDrawable(Context context, @DrawableRes int resId, @ColorRes int tintNormalColor, @ColorRes int tintPressColor) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);

        int[] colors = new int[]{
                ContextCompat.getColor(context, tintPressColor),
                ContextCompat.getColor(context, tintNormalColor)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};

        StateListDrawable stateListDrawable = getStateListDrawable(drawable, states);
        return getStateDrawable(stateListDrawable, colors, states);
    }

    /**
     * 获取多种状态下的图片
     *
     * @param drawable 图片对象
     * @param colors   着色颜色数组
     * @param states   状态的二维数组
     * @return
     */
    public static Drawable getStateDrawable(Drawable drawable, int[] colors, int[][] states) {
        ColorStateList colorStateList = new ColorStateList(states, colors);

        Drawable.ConstantState state = drawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable, colorStateList);
        return drawable;
    }

    /**
     * 把Drawable对象转化为StateListDrawable对象
     *
     * @param drawable 图片对象
     * @param states   状态二维数组
     * @return
     */
    private static StateListDrawable getStateListDrawable(Drawable drawable, int[][] states) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int[] state : states) {
            stateListDrawable.addState(state, drawable);
        }
        return stateListDrawable;
    }

}
