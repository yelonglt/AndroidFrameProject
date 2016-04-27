package com.yelong.androidframeproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yelong.androidframeproject.model.User;


/**
 * Created by yelong on 2016/3/1.
 */
public class UserDao {
    private SQLiteDatabase db;
    private static String whereClauseByTel = DBParam.USER_TEL + "=?";

    public UserDao(Context context) {
        DBManager.initialize(new DatabaseHelper(context));
    }

    private void openDB() {
        db = DBManager.getInstance().openDB();
    }

    private void closeDB() {
        DBManager.getInstance().closeDB();
    }

    /**
     * 添加用户或者更新用户信息
     *
     * @param user
     * @return
     */
    public long addUser(User user) {
        openDB();
        long l = 0;
        User oldUser = getUserByTel(user.tel);
        if (oldUser != null) {
            update(user);
        } else {
            l = insert(user);
        }
        closeDB();
        return l;
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    private long update(User user) {
        openDB();
        String[] selectionArgs = {user.tel};

        ContentValues values = new ContentValues();
        values.put(DBParam.USER_NAME, user.name);
        values.put(DBParam.USER_ADDRESS, user.address);
        values.put(DBParam.USER_TEL, user.tel);
        values.put(DBParam.USER_INTRO, user.intro);
        values.put(DBParam.USER_LOGO, user.logo);
        values.put(DBParam.SESSION_ID, user.sessionId);

        long num = 0;
        try {
            num = db.update(DBParam.TABLE_USER, values, whereClauseByTel, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDB();
        return num;
    }

    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    private long insert(User user) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(DBParam.USER_NAME, user.name);
        values.put(DBParam.USER_ADDRESS, user.address);
        values.put(DBParam.USER_TEL, user.tel);
        values.put(DBParam.USER_INTRO, user.intro);
        values.put(DBParam.USER_LOGO, user.logo);
        values.put(DBParam.SESSION_ID, user.sessionId);

        long num = 0;
        try {
            num = db.insert(DBParam.TABLE_USER, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDB();
        return num;
    }

    /**
     * 获取用户信息
     *
     * @param tel
     * @return
     */
    public User getUserByTel(String tel) {
        openDB();
        User user = null;
        Cursor cursor = null;
        String[] selectionArgs = {tel};
        try {
            cursor = db.query(DBParam.TABLE_USER, DBParam.USER_COLUMNS, whereClauseByTel, selectionArgs, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }

            if (cursor.moveToFirst()) {
                user = new User();
                user.name = cursor.getString(cursor.getColumnIndex(DBParam.USER_NAME));
                user.address = cursor.getString(cursor.getColumnIndex(DBParam.USER_ADDRESS));
                user.tel = cursor.getString(cursor.getColumnIndex(DBParam.USER_TEL));
                user.intro = cursor.getString(cursor.getColumnIndex(DBParam.USER_INTRO));
                user.logo = cursor.getString(cursor.getColumnIndex(DBParam.USER_LOGO));
                user.sessionId = cursor.getString(cursor.getColumnIndex(DBParam.SESSION_ID));
            }
        } catch (Exception e) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        closeDB();

        return user;
    }

    /**
     * 删除用户信息
     *
     * @param tel
     * @return
     */
    public long delUserByTel(String tel) {
        openDB();
        String[] selectionArgs = {tel};

        long num = 0;
        try {
            num = db.delete(DBParam.TABLE_USER, whereClauseByTel, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDB();
        return num;
    }
}
