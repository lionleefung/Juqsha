package com.lightcone.googleanalysis.tracker;

import android.content.Context;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.lang.Thread.UncaughtExceptionHandler;

/***
 * Ga = Google Analytics 
 */
public class GaHandler {
    private static GaHandler instance = new GaHandler();
    private Context context;
    private GaTrackerManager trackerManager;
    private Tracker globalTracker = null;
    private Tracker defaultTracker = null;//appTracker

    public static GaHandler getInstance() {
        return instance;
    }

    private GaHandler() {
    }

    public void init(Context context) {
        this.context = context;
        if (this.trackerManager == null) {
            this.trackerManager = new GaTrackerManager(context);
        }
    }

    public synchronized void initGlobleTracker(Integer xmlConfigId) {
        if (this.globalTracker == null) {
            this.globalTracker = this.trackerManager.getTracker(GaTrackerName.createGaTrackerName(GaTrackerName.GLOBAL_TRACKER.getName(), xmlConfigId));
            if (this.globalTracker != null) {
                this.globalTracker.enableAdvertisingIdCollection(true);
            }
        }
    }

    public synchronized void initAppTracker(Integer xmlConfigId) {
        if (this.defaultTracker == null) {
            this.defaultTracker = this.trackerManager.getTracker(GaTrackerName.createGaTrackerName(GaTrackerName.APP_TRACKER.getName(), xmlConfigId));
            if (this.defaultTracker != null) {
                this.defaultTracker.enableAdvertisingIdCollection(true);
            }
        }
    }

    /**
     * 初始化默认的异常处理程序
     */
    public void initDefaultExceptionHandler() {
        if (this.defaultTracker == null) {
            throw new RuntimeException("No Local Tracker Is INIT. Please Call Function : GaManager.initLocalTracker(trackerId) to Init");
        }

        UncaughtExceptionHandler myHandler = new ExceptionReporter(
                this.getDefaultTracker(),                                        // Currently used Tracker.
                Thread.getDefaultUncaughtExceptionHandler(),      // Current default uncaught exception handler.
                this.context);                                         // Context of the application.

        // Make myHandler the new default uncaught exception handler.
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }

    private Tracker getDefaultTracker() {
        if (instance.defaultTracker == null) {
            throw new RuntimeException("No Local Tracker Is INIT. Please Call Function : GaManager.initLocalTracker(trackerId) to Init");
        }
        return defaultTracker;
    }

    private Tracker getGlobleTracker() {
        return globalTracker;
    }


// ======================================== 全局统计的基础接口 =======================================================

    /**
     * 发送到所有应用公用的统计ID
     *
     * @param screenName
     */
    public void sendGlobalScreenView(String screenName) {
        sendScreenView(this.getGlobleTracker(), screenName);
    }

    /**
     * @param screenName
     * @param customDimensionIndex
     * @param customDimensionName
     * @see #sendScreenViewByCustomDimension(GaTrackerName, String, int, String)
     */
    public void sendGlobalScreenViewByCustomDimension(String screenName, int customDimensionIndex, String customDimensionName) {
        sendScreenViewByCustomDimension(this.getGlobleTracker(), screenName, customDimensionIndex, customDimensionName);
    }

    /**
     * @param screenName
     * @param customMetricIndex
     * @param value
     * @see #sendStatValueByCustomMetric(GaTrackerName, String, int, int)
     */
    public void sendGlobalStatValueByCustomMetric(String screenName, int customMetricIndex, int value) {
        sendStatValueByCustomMetric(this.getGlobleTracker(), screenName, customMetricIndex, value);
    }

    /**
     * @param screenName
     * @param customDimensionIndex
     * @param customDimensionName
     * @param customMetricIndex
     * @param value
     * @see #sendCustomDimensionAndMetric(GaTrackerName, String, int, String, int, int)
     */
    public void sendGlobalCustomDimensionAndMetric(String screenName, int customDimensionIndex, String customDimensionName, int customMetricIndex, int value) {
        sendCustomDimensionAndMetric(this.getGlobleTracker(), screenName, customDimensionIndex, customDimensionName, customMetricIndex, value);
    }


// ======================================== 默认统计的基础接口 =======================================================

    public void sendScreenView(String screenName) {
        sendScreenView(this.getDefaultTracker(), screenName);
    }

    public void sendScreenViewByCustomDimension(String screenName, int customDimensionIndex, String customDimensionName) {
        sendScreenViewByCustomDimension(this.getDefaultTracker(), screenName, customDimensionIndex, customDimensionName);
    }

    public void sendStatValueByCustomMetric(String screenName, int customMetricIndex, int value) {
        sendStatValueByCustomMetric(this.getDefaultTracker(), screenName, customMetricIndex, value);
    }

    public void sendCustomDimensionAndMetric(String screenName, int customDimensionIndex, String customDimensionName, int customMetricIndex, int value) {
        sendCustomDimensionAndMetric(this.getDefaultTracker(), screenName, customDimensionIndex, customDimensionName, customMetricIndex, value);
    }

    public void sendEvent(String screenName, String category, String action, String label) {
        sendEvent(this.getDefaultTracker(), screenName, category, action, label);
    }

    public void sendExceptionMsg(String exceptionDesc, boolean isFatal) {
        sendExceptionMsg(instance.getDefaultTracker(), exceptionDesc, isFatal);
    }

// ======================================== 统计的基础接口 =======================================================

    /**
     * 发送屏幕的浏览数据
     *
     * @param screenName 代表当前屏幕的唯一名称
     */
    private static void sendScreenView(Tracker tracker, String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 发送自定义维度的屏幕浏览数据
     * <p>
     * 例如，可以用该方式统计不同难度的关卡的浏览量（试玩）。
     *
     * @param tracker
     * @param screenName           自定义屏幕名，可以唯一的辨识，例如当前屏幕的名称为“Level1”，
     * @param customDimensionIndex 自定义的维度索引，需要在GoogleAnalytics的界面上配置，例如该索引对应的维度名称可以为“游戏难度”
     * @param customDimensionName  该次浏览的屏幕维度细分，例如“难度易”，“难度中”，“难度难”
     */
    private static void sendScreenViewByCustomDimension(Tracker tracker, String screenName, int customDimensionIndex, String customDimensionName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().setCustomDimension(customDimensionIndex, customDimensionName).build());
    }

    /**
     * @param tracker
     * @param screenName
     * @param customMetricIndex 自定义的指标索引，需要在GoogleAnalytics的界面上配置，例如该索引对应的指标名称可以是“完成关卡次数”
     */
    private static void sendStatValueByCustomMetric(Tracker tracker, String screenName, int customMetricIndex, int value) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().setCustomMetric(customMetricIndex, value).build());
    }

    /**
     * @param tracker
     * @param screenName
     * @param customDimensionIndex
     * @param customDimensionName
     * @param customMetricIndex
     * @param value
     * @see #sendScreenViewByCustomDimension(GaTrackerName, String, int, String)
     * @see #sendStatValueByCustomMetric(GaTrackerName, String, int, int)
     */
    private static void sendCustomDimensionAndMetric(Tracker tracker, String screenName, int customDimensionIndex, String customDimensionName, int customMetricIndex, int value) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().setCustomDimension(customDimensionIndex, customDimensionName).setCustomMetric(customMetricIndex, value).build());
    }

    /**
     * @param tracker
     * @param screenName
     * @param category   分类， 如“视频”
     * @param action     操作， 如“播放”
     */
    private static void sendEvent(Tracker tracker, String screenName, String category, String action, String label) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    /**
     * @param tracker
     * @param exceptionDesc 异常简述，可以是：异常所在的类+异常的方法+异常的位置
     * @param isFatal
     */
    private static void sendExceptionMsg(Tracker tracker, String exceptionDesc, boolean isFatal) {
        tracker.send(new HitBuilders.ExceptionBuilder().setDescription(exceptionDesc).setFatal(isFatal).build());
    }

}
