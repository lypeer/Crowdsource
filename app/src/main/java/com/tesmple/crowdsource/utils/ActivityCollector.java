package com.tesmple.crowdsource.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lypeer on 10/13/2015.
 */
public class ActivityCollector {

    /**
     * 包括所有的activity的list
     */
    public static List<Activity> sActivities = new ArrayList<>();

    /**
     * 向集合中天添加activity的方法
     * @param activity 要添加的activity
     */
    public static void addActivity(Activity activity){
        sActivities.add(activity);
    }

    /**
     * 移除集合中的activity的方法
     * @param activity 要移除的activity
     */
    public static void removeActivity(Activity activity){
        sActivities.remove(activity);
    }

    /**
     * 清除activity栈里面所有的activity的方法
     */
    public static void finishAll() {
        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 清除activity栈里面所有的activity的方法
     */
    public static void finishAllExceptNow(Activity targetActivity) {
        for (Activity activity : sActivities) {
            if (activity != targetActivity && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
