package com.dmall.mvp.business;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public class TaskManager {

    TaskDataSource mDataSource;

    public TaskManager(TaskDataSource dataSource) {
        mDataSource = dataSource;
    }

    public String getShowContent() {
        return mDataSource.getStringFromRemote()
                + mDataSource.getStringFromLocal()
                + mDataSource.getStringFromCache();
    }
}
