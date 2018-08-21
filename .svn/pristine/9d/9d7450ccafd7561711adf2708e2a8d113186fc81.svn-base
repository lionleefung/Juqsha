package com.lightcone.feedback;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.lightcone.feedback.message.MessageManager;
import com.lightcone.feedback.message.callback.LoadUnreadCallback;


/**
 * Created by chenkehui on 0007 2017/12/7.
 */

public class FeedbackManager {

    private static FeedbackManager instance = new FeedbackManager();

    private FeedbackManager() {

    }

    public static FeedbackManager getInstance() {
        return instance;
    }

    public void init(String versionFileName) {
        MessageManager.getInstance().init(versionFileName);
    }

    public void loadUnreadCount(LoadUnreadCallback callback) {
        MessageManager.getInstance().loadUnreadCount(callback);
    }

    public void showFeedbackActivity(Activity parent) {
        parent.startActivity(new Intent(parent, FeedbackActivity.class));
    }

}
