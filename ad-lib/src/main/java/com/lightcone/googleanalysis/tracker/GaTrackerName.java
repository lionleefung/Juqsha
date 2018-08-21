package com.lightcone.googleanalysis.tracker;


import com.lightcone.common.R;

/** 不同类型的Tracker，提供不同需要的统计需求使用，大部分情况下，只需要使用APP_TRACKER */
public class GaTrackerName {
	/** 只统计本App的相关信息 */
	public static final GaTrackerName APP_TRACKER = new GaTrackerName("APP_TRACKER", R.xml.app_tracker);
	/** 统计同一公司的所有APP的信息，例如某个广告在所有App中被点击了多少次之类的 */
	public static final GaTrackerName GLOBAL_TRACKER = new GaTrackerName("GLOBAL_TRACKER", R.xml.global_tracker);
	/** 消费统计，只供消费统计使用 */
	public static final GaTrackerName ECOMMERCE_TRACKER = new GaTrackerName("ECOMMERCE_TRACKER", R.xml.ecommerce_tracker);
	
	/** @param xmlConfigId xmlConfigId = R.xml.XXX，也即一份XML配置文件 */
	public static GaTrackerName createGaTrackerName(String identityName, Integer xmlConfigId) {
		return new GaTrackerName(identityName, xmlConfigId);
	}
	
	/** @param trackerId 从Google Analytics上拿到的追踪ID */
	public static GaTrackerName createGaTrackerName(String identityName, String trackerId) {
		return new GaTrackerName(identityName, trackerId);
	}
	
	private String name;
	private Integer xmlConfigId;
	private String trackerId;
	
	private GaTrackerName(String name) {
		this.name = name;
		this.xmlConfigId = null;
		this.trackerId = null;
	}
	
	private GaTrackerName(String name, Integer xmlConfigId) {
		this.name = name;
		this.xmlConfigId = xmlConfigId;
		this.trackerId = null;
	}
	
	private GaTrackerName(String name, String trackerId) {
		this.name = name;
		this.xmlConfigId = null;
		this.trackerId = trackerId;
	}
	
	String getName() {
		return name;
	}
	
	/** @return can be null, means default */
	Integer getXmlConfigId() {
		return xmlConfigId;
	}
	
	String getTrackerId() {
		return trackerId;
	}
}
