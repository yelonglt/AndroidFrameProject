package com.yelong.androidframeproject.db.user;

/**
 * 数据库用户表参数
 * Created by yelong on 2016/12/15.
 * mail:354734713@qq.com
 */

public class DBUserParam {
    //表名
    public static final String TABLE_USER = "USER";
    //用户表字段
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_TEL = "USER_TEL";
    public static final String USER_INTRO = "USER_INTRO";
    public static final String USER_LOGO = "USER_LOGO";
    public static final String SESSION_ID = "SESSION_ID";

    public static final String[] USER_COLUMNS = {USER_NAME, USER_ADDRESS, USER_TEL, USER_INTRO, USER_LOGO, SESSION_ID};


    /* 用户表 */
    public static final String SQL_CREATE_USER = "CREATE TABLE " + TABLE_USER + " (" + //
            USER_NAME + " TEXT(100)," + //
            USER_ADDRESS + " TEXT(200)," + //
            USER_TEL + " TEXT(100)," + //
            USER_INTRO + " TEXT(100)," + //
            USER_LOGO + " TEXT(100)," + //
            SESSION_ID + " TEXT(100)" + //
            ")";

}
