package com.lightcone.ad.admob.banner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.ads.AdSize;

public class AdmobBannarFragmentActivity extends FragmentActivity{

	private AdmobBannerHandler admobBannerHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void setBannerSize(AdSize adSize) {
		admobBannerHandler.setBannerSize(adSize);
	}
	
	protected void setAdViewVisible(int visibility) {
		admobBannerHandler.setAdViewVisible(visibility);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (admobBannerHandler == null) {
			admobBannerHandler = new AdmobBannerHandler(this);
		}
		admobBannerHandler.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		admobBannerHandler.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		admobBannerHandler.onDestroy();
	}
}
