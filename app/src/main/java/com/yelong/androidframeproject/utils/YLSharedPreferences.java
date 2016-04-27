package com.yelong.androidframeproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yelong.androidframeproject.MainApplication;

/**
 * SharedPreferences工具类
 * Created by eyetech on 16/4/17.
 */
public class YLSharedPreferences {
    public static final String SP_NAME = "yl_helper";

    /**
     * 是否第一次打开APP
     */
    public static final String FIRST_OPEN = "FIRST_OPEN";


    private volatile static YLSharedPreferences instance = null;

    private YLSharedPreferences() {
    }

    public static YLSharedPreferences getInstance() {
        //创建临时变量是因为访问volatile变量开销比较大
        YLSharedPreferences tempInstance = instance;
        if (tempInstance == null) {
            synchronized (YLSharedPreferences.class) {
                tempInstance = instance;
                if (tempInstance == null) {
                    tempInstance = new YLSharedPreferences();
                    instance = tempInstance;
                }
            }
        }
        return tempInstance;
    }

    private SharedPreferences getSp() {
        return MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String key) {
        try {
            SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.remove(key);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
