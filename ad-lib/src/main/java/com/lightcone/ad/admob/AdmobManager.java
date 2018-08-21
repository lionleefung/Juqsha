package com.lightcone.ad.admob;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.lightcone.ad.AdManager;
import com.lightcone.ad.popad.OnPopAdWindowCloseListener;

public class AdmobManager {

	private static final AdmobManager instance = new AdmobManager();

	public static AdmobManager getInstance() {
		return instance;
	}

	private InterstitialAd mInterstitialAd;
	private boolean isInterStitialAdLoadFaild;
	private OnPopAdWindowCloseListener popCloseListener;

	private AppLovinAd curAppLovinAd;
	private AppLovinSdk appLovinSdk;

	public void init(Context context) {
		AppLovinSdk.initializeSdk(context);

		mInterstitialAd = new InterstitialAd(context);
		mInterstitialAd.setAdUnitId(AdManager.getInstance().getAdConfig().getAdmobScreenId());
		isInterStitialAdLoadFaild = false;
		initEventListener();
		requestNewInterstitial();

		//Facebook 插屏广告
		initFbInterstitialAd(context);

		initAppLovinAd(context);
	}
	
	public boolean displayInterstitial(OnPopAdWindowCloseListener popCloseListener, Activity activityForFkingAppLovin) {
		this.popCloseListener = popCloseListener;
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
			return true;
		} else if (isInterStitialAdLoadFaild) {//如果是加载失败了，那么重新加载一次
			isInterStitialAdLoadFaild = false;
			requestNewInterstitial();
			//谷歌插屏广告加载失败，则弹facebook插屏广告，如果facebook插屏失败则弹AppLovin，否则返回False
			return tryPopOtherAd(activityForFkingAppLovin);
		} else {
			return tryPopOtherAd(activityForFkingAppLovin);
		}
	}

	/**
	 * 当Google的mInterstitialAd.isLoaded() == false，（还未Load成功或Load失败时）
	 * 且{@link com.google.android.gms.ads.AdListener#onAdFailedToLoad(int)}还未被回调时
	 * 优先尝试其它广告
	 */
	private boolean tryPopOtherAd(Activity activityForFkingAppLovin) {
		if(fbInterstitialAd.isAdLoaded()) {
			fbInterstitialAd.show();
			return true;
		} else if ( this.curAppLovinAd != null){
			if (activityForFkingAppLovin == null) {
				Log.e("GzyAppLovin", "Activity for AppLovin is Null");
				return false;
			}
			AppLovinInterstitialAdDialog appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(this.appLovinSdk, activityForFkingAppLovin);
			appLovinInterstitialAdDialog.showAndRender(this.curAppLovinAd);
			appLovinInterstitialAdDialog.setAdDisplayListener(appLovinAdDisplayListener);
			return true;
		}
		return false;
	}

	public AdRequest buildBannerAdRequest() {
		return getAdRequestBuilder().build();
	}
	
	private void initEventListener() {
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				if (popCloseListener != null) {
					popCloseListener.onPopAdWindowClose();
				}
				requestNewInterstitial();
			}
			
			@Override
			public void onAdFailedToLoad(int errorCode) {
				super.onAdFailedToLoad(errorCode);
				isInterStitialAdLoadFaild = true;
			}
		});
	}
	
	private void requestNewInterstitial() {
		mInterstitialAd.loadAd(getAdRequestBuilder().build());
	}
	
	private AdRequest.Builder getAdRequestBuilder() {
		AdRequest.Builder builder = new AdRequest.Builder();
		for (String testDevice : TestDeviceList.list) {
			builder.addTestDevice(testDevice);
		}
		return builder;
	}


	/**
	 * Facebook 插屏广告，只有在谷歌插屏广告弹不出来时才会起作用，增加于2016年10月8号
	 */
	private com.facebook.ads.InterstitialAd fbInterstitialAd;

	private void initFbInterstitialAd(Context context) {
		fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, AdManager.getInstance().getAdConfig().getFbScreenId());
		fbInterstitialAd.setAdListener(fbAdListener);
		addFbTestDevice();
		requestNewFbInterstitialAd();
	}

	private void requestNewFbInterstitialAd() {
		if(fbInterstitialAd != null) {
			fbInterstitialAd.loadAd();
		}
	}

	private InterstitialAdListener fbAdListener = new InterstitialAdListener() {
		@Override
		public void onInterstitialDisplayed(Ad ad) {

		}

		@Override
		public void onInterstitialDismissed(Ad ad) {
			if (popCloseListener != null) {
				popCloseListener.onPopAdWindowClose();
			}
			requestNewFbInterstitialAd();
		}

		@Override
		public void onError(Ad ad, AdError adError) {
			Log.e("Facebook InterstitialAd", "Facebook InterstitialAd Ad onError:" + adError.getErrorMessage());
		}

		@Override
		public void onAdLoaded(Ad ad) {
			Log.e("Facebook InterstitialAd", "Facebook InterstitialAd onAdLoaded!");
		}

		@Override
		public void onAdClicked(Ad ad) {

		}

        @Override
        public void onLoggingImpression(Ad ad) {

        }
    };

	private void addFbTestDevice() {
		for(String deviceId : FbTestDeviceList.list) {
			AdSettings.addTestDevice(deviceId);
		}
	}

	/**
	 * 	code for AppLovin
     */
	private void initAppLovinAd(Context context) {
		this.appLovinSdk = AppLovinSdk.getInstance(context);
		this.requestNewAppLovinInterstitialAd(context);
	}

	private void requestNewAppLovinInterstitialAd(Context context) {
		AppLovinSdk.getInstance(context).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
			@Override
			public void adReceived(AppLovinAd ad) {
				curAppLovinAd = ad;
				Log.e("GzyAppLovin", "Load Next Ad Succeed.");
			}

			@Override
			public void failedToReceiveAd(int errorCode) {
				Log.e("GzyAppLovin", "Load Next Ad Failed.");
			}
		});
	}

	private AppLovinAdDisplayListener appLovinAdDisplayListener = new AppLovinAdDisplayListener() {

		@Override
		public void adDisplayed(AppLovinAd appLovinAd) {
			Log.e("GzyAppLovin", "Interstitial Ad Showed.");
		}

		@Override
		public void adHidden(AppLovinAd appLovinAd) {
			Log.e("GzyAppLovin", "Interstitial Ad closed.");
			if (popCloseListener != null) {
				popCloseListener.onPopAdWindowClose();
			}
		}
	};
}
