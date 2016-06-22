package com.yelong.androidframeproject.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登陆器载体
 * 注意Bundle类型数据的写入和读取
 * Created by eyetech on 16/6/20.
 * mail:354734713@qq.com
 */
public class LoginCarrier implements Parcelable {

    /**
     * 目标Activity的Action，activity隐式启动
     */
    public String mTargetAction;
    public Bundle mBundle;

    public LoginCarrier(String targetAction, Bundle bundle) {
        mTargetAction = targetAction;
        mBundle = bundle;
    }

    /**
     * 跳转到目标页面,隐式跳转实现的。
     *
     * @param context
     */
    public void invoke(Context context) {
        Intent intent = new Intent(mTargetAction);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    protected LoginCarrier(Parcel in) {
        //按变量定义的顺序读取
        mTargetAction = in.readString();
        mBundle = in.readParcelable(Bundle.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //按变量定义的顺序写入
        dest.writeString(mTargetAction);
        dest.writeParcelable(mBundle, flags);
    }


    public static final Creator<LoginCarrier> CREATOR = new Creator<LoginCarrier>() {
        @Override
        public LoginCarrier createFromParcel(Parcel in) {
            return new LoginCarrier(in);
        }

        @Override
        public LoginCarrier[] newArray(int size) {
            return new LoginCarrier[size];
        }
    };
}
