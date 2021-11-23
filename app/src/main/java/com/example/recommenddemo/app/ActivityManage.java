package com.example.recommenddemo.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
    提供一个专门的集合类对所有的活动进行管理
    方便随时退出程序
*/
public class ActivityManage {

    private static List<Activity> mActivities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        mActivities.add(activity);
    }

    public static void removeActivity(Activity activity ){
        mActivities.remove(activity);
    }

    public static void finishAllActivities() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivities.clear();
    }

    public static Activity getActivityByCurrenlyRun(){
        if(mActivities.size() <= 0){
            return null;
        }
        return mActivities.get(mActivities.size() - 1);
    }



}
