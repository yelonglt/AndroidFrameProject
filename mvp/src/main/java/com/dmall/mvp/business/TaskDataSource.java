package com.dmall.mvp.business;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public interface TaskDataSource {

    String getStringFromRemote();

    String getStringFromCache();

    String getStringFromLocal();
}
