package com.bangbang.utils;

import android.app.Activity;

import java.util.HashSet;

public class ActivityManager {

    /**
     *  定义HashSet集合来装Activity，是可以防止Activity不被重复
     */
    private static HashSet<Activity> hashSet = new HashSet<Activity>();

    private static ActivityManager instance = new ActivityManager();;

    private ActivityManager() {}

    public static ActivityManager getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
