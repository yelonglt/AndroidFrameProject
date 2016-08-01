package com.yelong.ulibrary.model;

import android.graphics.drawable.Drawable;

/**
 * 封装软件信息的bean类
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class AppInfo {
    //名称
    private String name;
    //图标
    private Drawable icon;
    //包名
    private String packagName;
    //版本号
    private String versionName;
    //是否安装到SD卡
    private boolean isSD;
    //是否是用户程序
    private boolean isUser;

    public AppInfo() {
        super();
    }

    public AppInfo(String name, Drawable icon, String packagName,
                   String versionName, boolean isSD, boolean isUser) {
        super();
        this.name = name;
        this.icon = icon;
        this.packagName = packagName;
        this.versionName = versionName;
        this.isSD = isSD;
        this.isUser = isUser;
    }
}
