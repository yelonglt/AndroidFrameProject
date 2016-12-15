package com.yelong.androidframeproject.db.user;

import android.content.ContentValues;
import android.database.Cursor;

import com.yelong.androidframeproject.db.DBDao;
import com.yelong.androidframeproject.model.User;


/**
 * 用户信息业务逻辑
 * Created by yelong on 2016/3/1.
 */
public class UserDao extends DBDao {
    private static String whereClauseByTel = DBUserParam.USER_TEL + "=?";

    public UserDao() {
    }
    
    /**
     * 添加用户或者更新用户信息
     *
     * @param user
     * @return
     */
    public long addUser(User user) {
        open();
        long l = 0;
        User oldUser = getUserByTel(user.tel);
        if (oldUser != null) {
            update(user);
        } else {
            l = insert(user);
        }
        close();
        return l;
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    private long update(User user) {
        open();
        String[] selectionArgs = {user.tel};

        ContentValues values = new ContentValues();
        values.put(DBUserParam.USER_NAME, user.name);
        values.put(DBUserParam.USER_ADDRESS, user.address);
        values.put(DBUserParam.USER_TEL, user.tel);
        values.put(DBUserParam.USER_INTRO, user.intro);
        values.put(DBUserParam.USER_LOGO, user.logo);
        values.put(DBUserParam.SESSION_ID, user.sessionId);

        long num = 0;
        try {
            num = db.update(DBUserParam.TABLE_USER, values, whereClauseByTel, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        close();
        return num;
    }

    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    private long insert(User user) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBUserParam.USER_NAME, user.name);
        values.put(DBUserParam.USER_ADDRESS, user.address);
        values.put(DBUserParam.USER_TEL, user.tel);
        values.put(DBUserParam.USER_INTRO, user.intro);
        values.put(DBUserParam.USER_LOGO, user.logo);
        values.put(DBUserParam.SESSION_ID, user.sessionId);

        long num = 0;
        try {
            num = db.insert(DBUserParam.TABLE_USER, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        close();
        return num;
    }

    /**
     * 获取用户信息
     *
     * @param tel
     * @return
     */
    public User getUserByTel(String tel) {
        open();
        User user = null;
        Cursor cursor = null;
        String[] selectionArgs = {tel};
        try {
            cursor = db.query(DBUserParam.TABLE_USER, DBUserParam.USER_COLUMNS, whereClauseByTel, selectionArgs, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }

            if (cursor.moveToFirst()) {
                user = new User();
                user.name = cursor.getString(cursor.getColumnIndex(DBUserParam.USER_NAME));
                user.address = cursor.getString(cursor.getColumnIndex(DBUserParam.USER_ADDRESS));
                user.tel = cursor.getString(cursor.getColumnIndex(DBUserParam.USER_TEL));
                user.intro = cursor.getString(cursor.getColumnIndex(DBUserParam.USER_INTRO));
                user.logo = cursor.getString(cursor.getColumnIndex(DBUserParam.USER_LOGO));
                user.sessionId = cursor.getString(cursor.getColumnIndex(DBUserParam.SESSION_ID));
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
        close();

        return user;
    }

    /**
     * 删除用户信息
     *
     * @param tel
     * @return
     */
    public long delUserByTel(String tel) {
        open();
        String[] selectionArgs = {tel};

        long num = 0;
        try {
            num = db.delete(DBUserParam.TABLE_USER, whereClauseByTel, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        close();
        return num;
    }
}
