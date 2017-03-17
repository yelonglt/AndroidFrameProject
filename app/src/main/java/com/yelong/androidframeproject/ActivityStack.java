package com.yelong.androidframeproject;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 所有Activity的管理类
 * Created by yelong on 2017/3/17.
 * mail:354734713@qq.com
 */
public class ActivityStack {

    private List<Activity> mActivityList;
    private WeakReference<Activity> mCurrentActivityWeakRef;
    private int activityCounter;


    private static class ActivityStackHolder {
        private static ActivityStack instance = new ActivityStack();
    }

    private ActivityStack() {
        mActivityList = Collections.synchronizedList(new ArrayList<Activity>());
    }

    public static ActivityStack getInstance() {
        return ActivityStackHolder.instance;
    }

    /**
     * 获取当前激活的activity
     *
     * @return 活动
     */
    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (mCurrentActivityWeakRef != null) {
            currentActivity = mCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    /**
     * 设置当前的处于激活的activity
     *
     * @param activity 活动
     */
    public void setCurrentActivity(Activity activity) {
        mCurrentActivityWeakRef = new WeakReference<>(activity);
    }

    /**
     * 添加容器里的activity
     *
     * @param activity 活动
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivityList.add(activity);
        }
    }

    /**
     * 删除容器里的activity
     *
     * @param activity 活动
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 退出App应用
     */
    public void exitApp() {
        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
        mActivityList.clear();
        System.exit(0);
    }

    /**
     * 活跃的Activity数量加1
     */
    public void activityCounterPlusOne() {
        activityCounter += 1;
    }

    /**
     * 活跃的Activity数量减1
     */
    public void activityCounterSubtractOne() {
        activityCounter -= 1;
    }

    /**
     * 判断App是否在后台
     */
    public boolean isAppBackground() {
        return activityCounter == 0;
    }
}
