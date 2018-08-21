package com.lightcone.ad.helper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by zhouyuxuan on 2016/12/6.
 */
public class ContextHelper {
    public static Activity getActivityFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
