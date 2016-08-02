package com.yelong.androidframeproject;

import android.support.annotation.IntDef;

/**
 * 程序全局常量类
 * Created by yelong on 16/8/2.
 * mail:354734713@qq.com
 */
public class AppConstants {

    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSE = 1;
    public static final int STATE_BROKEN = 2;

    //使用注解,设置DoorState只能取值设置的常量.取代枚举提高性能(枚举的实质还是创建对象)
    @IntDef({STATE_OPEN, STATE_CLOSE, STATE_BROKEN})
    public @interface DoorState {
    }

}
