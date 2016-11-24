package com.yelong.ulibrary.view;

import android.content.Context;
import android.view.Gravity;

/**
 * 标签视图工厂
 * Created by yelong on 2016/11/17.
 * mail:354734713@qq.com
 */

public class BadgeFactory {

    public static BadgeView createDot(Context context) {
        return new BadgeView(context).setWidthAndHeight(10, 10).setTextSize(0).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_CIRCLE);
    }

    public static BadgeView createCircle(Context context) {
        return new BadgeView(context).setWidthAndHeight(20, 20).setTextSize(12).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_CIRCLE);
    }

    public static BadgeView createRectangle(Context context) {
        return new BadgeView(context).setWidthAndHeight(25, 20).setTextSize(12).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_RECTANGLE);
    }

    public static BadgeView createOval(Context context) {
        return new BadgeView(context).setWidthAndHeight(25, 20).setTextSize(12).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_OVAL);
    }

    public static BadgeView createSquare(Context context) {
        return new BadgeView(context).setWidthAndHeight(20, 20).setTextSize(12).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_SQUARE);
    }

    public static BadgeView createRoundRect(Context context) {
        return new BadgeView(context).setWidthAndHeight(25, 20).setTextSize(12).setBadgeGravity(Gravity.RIGHT | Gravity.TOP).setShape(BadgeView.SHAPE_ROUND_RECTANGLE);
    }

    public static BadgeView create(Context context) {
        return new BadgeView(context);
    }

}
