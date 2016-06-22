package com.yelong.androidframeproject.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.yelong.androidframeproject.activity.LoginActivity;

/**
 * 登录判断类
 * Created by eyetech on 16/6/20.
 * mail:354734713@qq.com
 */
public class LoginInterceptor {

    //没有登录，携带登录成功之后需要跳转的页面
    public static final String mINVOKER = "INTERCEPTOR_INVOKER";

    /**
     * 判断处理
     *
     * @param context 当前activity的上下文
     * @param target  目标activity的target
     * @param bundle  目标activity所需要的参数
     * @param intent  目标activity
     */
    public static void interceptor(Context context, String target, Bundle bundle, Intent intent) {
        if (target != null && target.length() > 0) {
            LoginCarrier invoker = new LoginCarrier(target, bundle);
            if (getLogin()) {
                invoker.invoke(context);
            } else {
                if (intent == null) {
                    intent = new Intent(context, LoginActivity.class);
                }
                login(context, invoker, intent);
            }
        } else {
            Toast.makeText(context, "没有activity可以跳转", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录判断
     *
     * @param context 当前activity的上下文
     * @param target  目标activity的target
     * @param bundle  目标activity所需要的参数
     */
    public static void interceptor(Context context, String target, Bundle bundle) {
        interceptor(context, target, bundle, null);
    }

    // 这里获取登录状态，具体获取方法看项目具体的判断方法
    private static boolean getLogin() {
        return false;
    }

    private static void login(Context context, LoginCarrier invoker, Intent intent) {
        intent.putExtra(mINVOKER, invoker);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


}
