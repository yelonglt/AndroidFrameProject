package com.yelong.androidframeproject.db;

import android.database.sqlite.SQLiteDatabase;

import com.yelong.androidframeproject.MainApplication;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据库管理类
 * Created by yelong on 2016/3/1.
 */
public class DBManager {
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    // 打开数据库操作计数器
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static class DBManagerHolder {
        private static DBManager instance = new DBManager();
    }

    private DBManager() {
        mHelper = new DatabaseHelper(MainApplication.getInstance().getApplicationContext());
    }

    public static DBManager getInstance() {
        return DBManagerHolder.instance;
    }

    public synchronized SQLiteDatabase openDB() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDB() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }
}
