////////////////////////////////////////////////////////////////////////////////
//
//  AdmobBannarActivity 
//
//  Copyright (C) 2015	huxiaofei
//
//  使用方法：
//  1.如果activity中需要使用banner广告，请将自己的Activity继承AdmobBannerActivity
//  2.如需改变AdView属性，请在onCreate方法中调用
//	3.在需要显示banner位置的地方加上以下布局代码    
//  4.在自己Fragment中的onCreateView()方法中调用setFragmentView(View fragmentView)
//
//	<RelativeLayout
//  	android:id="@+id/ad_layout"
//   	android:layout_width="match_parent"
//      android:layout_height="wrap_content">
//      <include layout="@layout/admob_banner_layout"/>
//  </RelativeLayout>
//
//	PS:比activity多出了第四步，必须实现
//
///////////////////////////////////////////////////////////////////////////////
package com.lightcone.ad.admob.banner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdSize;

public class AdmobBannarFragment extends Fragment{

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
