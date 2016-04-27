package com.yelong.androidframeproject.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yelong on 2016/3/1.
 */
public class DBManager {
    private static DBManager instance;
    private static DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    // 打开数据库操作计数器
    private AtomicInteger mOpenCounter = new AtomicInteger();

    public static synchronized void initialize(DatabaseHelper helper) {
        if (instance == null) {
            instance = new DBManager();
            mHelper = helper;
        }
    }

    private DBManager() {

    }

    public static DBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DBManager.class.getName()
                    + " is not initialize");
        }
        return instance;
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
