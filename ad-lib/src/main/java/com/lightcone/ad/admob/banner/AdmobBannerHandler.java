package com.lightcone.ad.admob.banner;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lightcone.ad.AdManager;
import com.lightcone.ad.admob.AdmobManager;
import com.lightcone.ad.admob.AdmobType;
import com.lightcone.ad.admob.FbTestDeviceList;
import com.lightcone.ad.applovin.AppLovinBannerView;
import com.lightcone.ad.cache.BannerCacheManager;
import com.lightcone.ad.helper.Callback;
import com.lightcone.ad.helper.InstallHelper;
import com.lightcone.ad.helper.timer.Timer;
import com.lightcone.ad.model.AdModel;
import com.lightcone.ad.update.UpdateManager;
import com.lightcone.common.R;
import com.lightcone.utils.SharedContext;

/**
 * 应对以上4种都不支持的接口，自己调用生命周期方法
 *
 * @author huxiaofei
 */
public class AdmobBannerHandler {

	private static final long BANNER_RELOAD_TIME = 60 * 1000;
	private AdSize adSize = AdSize.BANNER;

	private RelativeLayout adLayout;//布局
	private AdView adView;//admob banner
	private ImageView selfView;//self banner
	private Timer mTimer;//计时器
	private int typeBanner;//当前是哪种banner
	private AdModel adModel;//当前是哪一个self model
	private com.facebook.ads.AdView fbAdView; //Facebook Banner Ad
	private AppLovinBannerView appLovinView;

	/**
	 * 针对activity的构造方法
	 *
	 * @param activity
	 */
	public AdmobBannerHandler(Activity activity) {
		adLayout = (RelativeLayout) activity.findViewById(R.id.layout_admob_banner_ad);
	}

	/**
	 * 针对view的构造方法
	 *
	 * @param fragmentView
	 */
	public AdmobBannerHandler(View fragmentView) {
		adLayout = (RelativeLayout) fragmentView.findViewById(R.id.layout_admob_banner_ad);
	}

	public void setBannerSize(AdSize adSize) {
		this.adSize = adSize;
	}

	public void setAdViewVisible(int visibility) {
		adView.setVisibility(visibility);
	}

	public void onResume() {
		if (!AdManager.getInstance().isFinishInit()) {
			Log.e("AdmobBannerHandler", "AdManager没有完成初始化");
			return;
		}
		if (adLayout == null) {
			Log.e("AdmobBannerHandler", "R.id.layout_admob_banner_ad为空!");
			return;
		}
		if (adView != null) {
			adView.resume();
		}
		if (mTimer == null) {
			mTimer = new Timer(new Callback<Integer>() {
				@Override
				public void onCallback(Integer response) {
					onTimerRun(response);
				}
			}, 0, BANNER_RELOAD_TIME);
		}
		mTimer.onResume();
	}

	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		if (mTimer != null) {
			mTimer.onPause();
		}
	}

	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		if (mTimer != null) {
			mTimer.onDestroy();
		}
		if (fbAdView != null) {
			fbAdView.destroy();
		}
	}

	private void onTimerRun(int count) {
		buildBannerView();
		if (UpdateManager.getInstance().isNetworkAvailable()) {
			adLayout.setVisibility(View.VISIBLE);
		} else {
			adLayout.setVisibility(View.INVISIBLE);
		}
	}

	private void buildBannerView() {
			int bannerId = BannerCacheManager.getInstance().getNextBannerId(true);
			adModel = BannerCacheManager.getInstance().getBannerAdModelById(bannerId);
			if (adModel == null) {
				buildAdmobBanner();
			} else {
				buildSelfBanner();
			}
	}

	private void buildAdmobBanner() {
		if (adView == null) {
			adView = new AdView(adLayout.getContext());
			adView.setAdUnitId(AdManager.getInstance().getAdConfig().getAdmobBannerId());
			adView.setAdSize(adSize);
			adView.setAdListener(adListener);
			adView.setLayoutParams(getParams());
			adLayout.addView(adView);
			adView.setVisibility(View.INVISIBLE);
		}
		adView.loadAd(AdmobManager.getInstance().buildBannerAdRequest());
		typeBanner = AdmobType.AD_TYPE_ADMOB;
	}

	private void buildSelfBanner() {
		if (selfView == null) {
			selfView = new ImageView(adLayout.getContext());
			selfView.setLayoutParams(getParams());
			adLayout.addView(selfView);
			selfView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					InstallHelper.openAppInPlayStore(adModel.getClickUrl());
				}
			});
		}
		selfView.setImageDrawable(adModel.getDrawable());
		selfView.setVisibility(View.VISIBLE);
		if (adView != null) {
			adView.setVisibility(View.INVISIBLE);
		}
		if (fbAdView != null) {
			fbAdView.setVisibility(View.INVISIBLE);
		}
		if (appLovinView != null) {
			appLovinView.setVisibility(View.INVISIBLE);
		}
		typeBanner = AdmobType.AD_TYPE_SELF;
	}

	/**
	 * admob adlistener
	 */
	private AdListener adListener = new AdListener() {

		@Override
		public void onAdLoaded() {
			super.onAdLoaded();
			if (typeBanner == AdmobType.AD_TYPE_ADMOB) {
				adView.setVisibility(View.VISIBLE);
				if (selfView != null) {
					selfView.setVisibility(View.INVISIBLE);
				}
			}
			if (fbAdView != null) {
				fbAdView.setVisibility(View.INVISIBLE);
			}
			if (appLovinView != null) {
				appLovinView.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {
			super.onAdFailedToLoad(errorCode);
			adView.setVisibility(View.INVISIBLE);
			buildFbBanner();//(只有在谷歌banner弹不出来时才会弹Facebook Banner)
		}
	};

	private LayoutParams getParams() {
		LayoutParams params = new LayoutParams(dp2px(320), dp2px(50));
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		return params;
	}

    public int dp2px(float dpValue) {
        float scale = SharedContext.context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

	/**
	 * 2016年10月8号新增
	 * Facebook Banner Ad(只有在谷歌banner弹不出来时才会弹Facebook Banner)
	 */

	private void buildFbBanner() {
		if (fbAdView == null) {
			String fbBannerId = AdManager.getInstance().getAdConfig().getFbBannerId();
			fbAdView = new com.facebook.ads.AdView(adLayout.getContext(), fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
			addFbTestDevice();
			fbAdView.setLayoutParams(getParams());
			adLayout.addView(fbAdView);
			fbAdView.setAdListener(fbAdListener);
			fbAdView.setVisibility(View.INVISIBLE);
		}
		fbAdView.loadAd();
		typeBanner = AdmobType.AD_TYPE_ADMOB;
	}

	private com.facebook.ads.AdListener fbAdListener = new com.facebook.ads.AdListener() {

		@Override
		public void onAdLoaded(Ad ad) {
			Log.e("Facebook Banner", "Facebook Banner Ad onAdLoaded!");
			if (typeBanner == AdmobType.AD_TYPE_ADMOB) {
				fbAdView.setVisibility(View.VISIBLE);
				if (selfView != null) {
					selfView.setVisibility(View.INVISIBLE);
				}
			}
			if (adView != null) {
				adView.setVisibility(View.INVISIBLE);
			}
			if (appLovinView != null) {
				appLovinView.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onError(Ad ad, AdError error) {
			Log.e("Facebook Banner", "Facebook Banner Ad onError:" + error.getErrorMessage());
			fbAdView.setVisibility(View.INVISIBLE);
			buildAppLovinBanner();
		}

		@Override
		public void onAdClicked(Ad ad) {

		}

		@Override
		public void onLoggingImpression(Ad ad) {

		}
	};

	private void addFbTestDevice() {
		for (String deviceId : FbTestDeviceList.list) {
			AdSettings.addTestDevice(deviceId);
		}
	}

	private void buildAppLovinBanner() {
		if (appLovinView == null) {
			appLovinView = new AppLovinBannerView(adLayout.getContext());
			appLovinView.setLayoutParams(getParams());
			appLovinView.setAdListener(appLovinListener);
			adLayout.addView(appLovinView);
			appLovinView.setVisibility(View.INVISIBLE);
		}
		appLovinView.loadAd();
		typeBanner = AdmobType.AD_TYPE_ADMOB;
	}

	private AppLovinBannerView.AdListener appLovinListener = new AppLovinBannerView.AdListener() {
		@Override
		public void onAdLoad(AppLovinNativeAd ad) {
			Log.e("GzyAppLovinBanner", "Banner onAdLoaded!");
			if (typeBanner == AdmobType.AD_TYPE_ADMOB) {
				appLovinView.setVisibility(View.VISIBLE);
				if (selfView != null) {
					selfView.setVisibility(View.INVISIBLE);
				}
			}
			if (fbAdView != null) {
				fbAdView.setVisibility(View.INVISIBLE);
			}
			if (adView != null) {
				adView.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {
			Log.e("GzyAppLovinBanner", "Banner Failed To Load, code: " + errorCode);
			//如果所有广告都Load不出来，则再试一次自家广告
			appLovinView.setVisibility(View.INVISIBLE);
			int selfBannerId = BannerCacheManager.getInstance().getNextBannerId(false);
			adModel = BannerCacheManager.getInstance().getBannerAdModelById(selfBannerId);
			if (adModel != null) {
				buildSelfBanner();
			}
		}
	};
}
