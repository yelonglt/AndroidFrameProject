package com.dmall.mvp.business;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public class TaskDataSourceImpl implements TaskDataSource {
    @Override
    public String getStringFromRemote() {
        return "Hello Remote\n";
    }

    @Override
    public String getStringFromCache() {
        return "Hello Cache\n";
    }

    @Override
    public String getStringFromLocal() {
        return "Hello Local\n";
    }
}
