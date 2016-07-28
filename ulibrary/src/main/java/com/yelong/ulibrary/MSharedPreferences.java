package com.yelong.ulibrary;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的封装类
 * Created by yelong on 16/7/27.
 * mail:354734713@qq.com
 */
public abstract class MSharedPreferences {

    public abstract String getSPName();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public MSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(getSPName(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public String getStringValue(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public int getIntValue(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public float getFloatValue(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBooleanValue(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    /**
     * @param key
     * @param value
     */
    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setFloatValue(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * delete all
     */
    public void deleteAll() {
        editor.clear();
        editor.commit();
    }

}
