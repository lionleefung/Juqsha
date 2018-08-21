package com.lightcone.ad.admob.banner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.ads.AdSize;

public class AdmobBannarFragmentV4 extends Fragment{

	private AdmobBannerHandler admobBannerHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void setFragmentView(View fragmentView) {
		admobBannerHandler = new AdmobBannerHandler(fragmentView);
	}
	
	protected void setBannerSize(AdSize adSize) {
		admobBannerHandler.setBannerSize(adSize);
	}
	
	protected void setAdViewVisible(int visibility) {
		admobBannerHandler.setAdViewVisible(visibility);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		admobBannerHandler.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		admobBannerHandler.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		admobBannerHandler.onDestroy();
	}
}
