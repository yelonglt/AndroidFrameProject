package com.yelong.androidframeproject.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 字体设置帮助类
 * Created by yelong on 2017/2/27.
 * mail:354734713@qq.com
 */
public class TypeFaceHelper {

    private static Typeface typeFace;

    public static void generateTypeface(Context context) {
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/app_font.ttf");
    }

    public static void applyTypeface(TextView textView) {
        textView.setTypeface(typeFace);
    }

    public static Typeface getTypeface() {
        return typeFace;
    }

}
