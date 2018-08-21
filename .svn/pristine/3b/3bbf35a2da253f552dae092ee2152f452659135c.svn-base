package com.lightcone.googleanalysis;

import com.lightcone.googleanalysis.tracker.GaHandler;
import com.lightcone.googleanalysis.tracker.GaTrackerName;

import android.content.Context;

public class GaManager {

	private static String screenName = "";
	private static boolean isInitAppTracker = false;
	private static boolean isInitGlobalTracker = false;
	
	/**
	 * 修改场景名，默认为pakageName
	 * @param screenName
	 */
	public static void setScreenName(String screenName) {
		GaManager.screenName = screenName;
	}
	
	/**
	 * 
	 * @param context
	 * @param xmlConfigId
	 */
	public static void initAppTracker(Context context, Integer xmlConfigId) {
		init(context);
		GaHandler.getInstance().initAppTracker(xmlConfigId);
		isInitAppTracker = true;
	}
	
	public static void initGlobalTracker(Context context, Integer xmlConfigId) {
		init(context);
		GaHandler.getInstance().initGlobleTracker(xmlConfigId);
		isInitGlobalTracker = true;
	}
	
	private static void init(Context context) {
		GaHandler.getInstance().init(context);
		screenName = context.getPackageName();
	}
	
	

	// ======================================== 默认统计的基础接口 =======================================================
	/**
	 * appTracker
	 * 发送screenName
	 */
	public static void sendScreenView() {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendScreenView(screenName);
		}
	}

	/**
	 * appTracker
	 * @param customDimensionIndex 自定义维度Index
	 * @param customDimensionName 自定义维度Name
	 */
	public static void sendScreenViewByCustomDimension(int customDimensionIndex, String customDimensionName) {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendScreenViewByCustomDimension(screenName,	customDimensionIndex, customDimensionName);
		}
	}

	/**
	 * appTracker
	 * @param customMetricIndex 自定义指标Index
	 * @param value 自定义指标Value
	 */
	public static void sendStatValueByCustomMetric(int customMetricIndex, int value) {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendStatValueByCustomMetric(screenName,	customMetricIndex, value);
		}
	}

	/**
	 * appTracker
	 * @param customDimensionIndex
	 * @param customDimensionName
	 * @param customMetricIndex
	 * @param value
	 */
	public static void sendCustomDimensionAndMetric(int customDimensionIndex, String customDimensionName, int customMetricIndex, int value) {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendCustomDimensionAndMetric(screenName, customDimensionIndex, customDimensionName, customMetricIndex, value);
		}
	}

	/**
	 * appTracker
	 * @param category
	 * @param action
	 * @param label
	 */
	public static void sendEvent(String category, String action, String label) {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendEvent(screenName, category, action, label);
		}
	}
	
	public static final String EVENT_CATEGORY_BUTTON = "按钮";
	public static final String EVENT_ACTION_CLICK_BUTTON = "点击";
	public static void sendEvent(String label) {
		sendEvent(EVENT_CATEGORY_BUTTON, EVENT_ACTION_CLICK_BUTTON, label);
	}

	public static void sendExceptionMsg(String exceptionDesc, boolean isFatal) {
		if (isInitAppTracker) {
			GaHandler.getInstance().sendExceptionMsg(exceptionDesc, isFatal);
		}
	}
		
	// ======================================== 全局统计的基础接口 =======================================================
	
	/**
	 * 发送到所有应用公用的统计ID
	 * 
	 * @param screenName
	 */
	public static void sendGlobalScreenView() {
		if (isInitGlobalTracker) {
			GaHandler.getInstance().sendGlobalScreenView(screenName);
		}
	}

	/**
	 * @see #sendScreenViewByCustomDimension(GaTrackerName, String, int, String)
	 * @param screenName
	 * @param customDimensionIndex
	 * @param customDimensionName
	 */
	public static void sendGlobalScreenViewByCustomDimension(int customDimensionIndex,	String customDimensionName) {
		if (isInitGlobalTracker) {
			GaHandler.getInstance().sendGlobalScreenViewByCustomDimension(screenName, customDimensionIndex, customDimensionName);
		}
	}

	/**
	 * @see #sendStatValueByCustomMetric(GaTrackerName, String, int, int)
	 * @param screenName
	 * @param customMetricIndex
	 * @param value
	 */
	public static void sendGlobalStatValueByCustomMetric(int customMetricIndex, int value) {
		if (isInitGlobalTracker) {
			GaHandler.getInstance().sendGlobalStatValueByCustomMetric(screenName, customMetricIndex, value);
		}
	}

	/**
	 * @see #sendCustomDimensionAndMetric(GaTrackerName, String, int, String,
	 *      int, int)
	 * @param screenName
	 * @param customDimensionIndex
	 * @param customDimensionName
	 * @param customMetricIndex
	 * @param value
	 */
	public static void sendGlobalCustomDimensionAndMetric(int customDimensionIndex, String customDimensionName, int customMetricIndex, int value) {
		if (isInitGlobalTracker) { 
			GaHandler.getInstance().sendGlobalCustomDimensionAndMetric(screenName, customDimensionIndex, customDimensionName, customMetricIndex, value);
		}
	}
	

}
