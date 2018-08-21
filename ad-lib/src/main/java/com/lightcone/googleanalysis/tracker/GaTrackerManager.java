package com.lightcone.googleanalysis.tracker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

class GaTrackerManager {
	private Context context;
	private Map<String, Tracker> trackers;
	
	GaTrackerManager(Context context) {
		this.context = context;
		this.trackers = new ConcurrentHashMap<String, Tracker>();
	}
	
	Tracker getTracker(GaTrackerName trackerName) {
		return getTracker(trackerName, false);
	}
	
	Tracker getTracker(GaTrackerName trackerName, boolean forceReplace) {
		Tracker tracker = this.trackers.get(trackerName.getName());
		if(tracker == null || forceReplace) {
			synchronized (trackers) {				
				tracker = this.trackers.get(trackerName.getName());
				if(tracker == null || forceReplace) {				
					GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
					
					tracker = trackerName.getXmlConfigId() == null ? analytics.newTracker(trackerName.getTrackerId()) : analytics.newTracker(trackerName.getXmlConfigId());
					this.trackers.put(trackerName.getName(), tracker);
				}
			}
		}
		return tracker;
	}
}
