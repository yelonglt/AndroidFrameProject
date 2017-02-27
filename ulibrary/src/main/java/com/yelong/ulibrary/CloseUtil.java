package com.yelong.ulibrary;

import java.io.Closeable;

/**
 * 关闭工具类
 * Created by yelong on 2016/10/31.
 * mail:354734713@qq.com
 */

public final class CloseUtil {

    private CloseUtil() {
        throw new UnsupportedOperationException("can't init...");
    }

    /**
     * 关闭Closeable对象
     *
     * @param closeable closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
