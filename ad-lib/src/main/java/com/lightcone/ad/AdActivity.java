package com.lightcone.ad;


import java.util.ArrayList;
import java.util.List;

import com.lightcone.ad.model.AdModel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lightcone.common.R;
import com.lightcone.googleanalysis.GaManager;

public class AdActivity extends Activity {

	private ViewGroup tipsGroup;
	private ViewPager viewPager;
	private List<AdModel> adModelList;
	
	private List<ImageView> tipViewList;
	private List<ViewGroup> adImageList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_activity);
		adModelList = new ArrayList<AdModel>();
	}

	@Override
	public void onResume() {
		super.onResume();
		tipsGroup = (ViewGroup) findViewById(R.id.image_tips);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		List<AdModel> tempAdModelList = new ArrayList<>(AdManager.getInstance().getAdModelList());
		adModelList.clear();
		for (AdModel adModel : tempAdModelList) {//去重，数量不大，直接暴力
			boolean isExist = false;
			for (AdModel adModelTemp : adModelList) {
				if (adModelTemp.equals(adModel)) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				adModelList.add(adModel);
			}
		}
		
		initTips();//初始化tips
		initAdImage();//初始化adImage
		
		viewPager.setAdapter(new AdPageAdapter());
		viewPager.setOnPageChangeListener(new AdPageChangeListener());
		findViewById(R.id.close_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		AdManager.getInstance().checkAndDownloadImage();//每次打开activity，检查广告图片下载
		GaManager.sendStatValueByCustomMetric(1, 1);
	}
	
	private void initTips() {
		tipsGroup.removeAllViews();
		tipViewList = new ArrayList<>();
		for (int i = 0; i < getAdModelRealSize(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tipViewList.add(imageView);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			tipsGroup.addView(imageView);
		}
	}
	
	private void initAdImage() {
		adImageList = new ArrayList<>();
		for (int i = 0; i < getAdModelRealSize(); i++) {
			ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.ad_page_view, null);
			ImageView imageView = (ImageView) viewGroup.findViewById(R.id.ad_backgroud);
			imageView.setImageDrawable(adModelList.get(i).getDrawable());
			adImageList.add(viewGroup);
		}
		if (canLoop()) {
			viewPager.setCurrentItem(adImageList.size() * 100);
		} else {
			viewPager.setCurrentItem(0);
		}
	}
	
	private boolean canLoop() {
		return adImageList.size() > 3;
	}
	
	private void openAppInPlayStore(String packageName) {
		String uri = "market://details?id=" + packageName;
		getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
	
	private int getAdModelRealSize() {
		for (int i = 0; i < adModelList.size(); i++) {
			if (adModelList.get(i).getDrawable() == null) {
				return i;
			}
		}
		return adModelList.size();
	}


	class AdPageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			int length = canLoop() ? Integer.MAX_VALUE : adImageList.size();
			return length;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(adImageList.get(position % adImageList.size()));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("instantiateItem");
			final int index = position % adImageList.size();
			View view = adImageList.get(index);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("urlclick", "urlclick");
					GaManager.sendStatValueByCustomMetric(2, 1);
					String packageName = adModelList.get(index).getClickUrl();// ads.get(index).url;
					if (packageName != null && !packageName.equals("")) {
						openAppInPlayStore(packageName);
					}
				}
			});
			container.addView(view, 0);
			return view;
		}
	}
	

	class AdPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int page) {
			GaManager.sendStatValueByCustomMetric(1, 1);
			
			setImageBackground(page % adImageList.size());
		}

		private void setImageBackground(int selectItems) {
			for (int i = 0; i < tipViewList.size(); i++) {
				if (i == selectItems) {
					tipViewList.get(i).setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					tipViewList.get(i).setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
	}
}