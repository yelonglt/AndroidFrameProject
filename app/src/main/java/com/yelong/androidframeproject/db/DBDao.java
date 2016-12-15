package com.yelong.androidframeproject.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库默认操作类
 * Created by yelong on 2016/12/15.
 * mail:354734713@qq.com
 */

public abstract class DBDao {

    protected SQLiteDatabase db;

    /**
     * 打开数据库连接
     */
    protected void open() {
        db = DBManager.getInstance().openDB();
    }

    /**
     * 关闭数据库连接
     */
    protected void close() {
        DBManager.getInstance().closeDB();
    }
}
