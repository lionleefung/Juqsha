package com.lightcone.ad.applovin;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdPrecacheListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.lightcone.ad.helper.ContextHelper;
import com.lightcone.common.R;

import java.util.List;

/**
 * Created by zhouyuxuan on 2016/12/6.
 */
public class AppLovinBannerView extends RelativeLayout {

    private TextView txt;
    private ImageView icon;
    private Button btn;

    public interface AdListener {
        void onAdLoad(AppLovinNativeAd ad);

        void onAdFailedToLoad(int errorCode);
    }

    private AppLovinNativeAd nativeAd;
    private AdListener adListener;

    public AppLovinBannerView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.applovin_banner_layout, this, true);
        this.txt = (TextView) findViewById(R.id.txt_applovin);
        this.icon = (ImageView) findViewById(R.id.icon_applovin);
        this.btn = (Button) findViewById(R.id.btn_applovin);

        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nativeAd != null) {
                    nativeAd.launchClickTarget(findViewById(android.R.id.content).getContext());
                }
            }
        });
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nativeAd != null) {
                    nativeAd.launchClickTarget(findViewById(android.R.id.content).getContext());
                }
            }
        });
    }

    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public void loadAd() {
        final AppLovinSdk sdk = AppLovinSdk.getInstance(getContext());
        sdk.getNativeAdService().loadNativeAds(1, new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(List list) {
                nativeAd = (AppLovinNativeAd) list.get(0);
                precacheAd();
            }

            @Override
            public void onNativeAdsFailedToLoad(final int i) {
                if (adListener != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adListener.onAdFailedToLoad(i);
                        }
                    });
                }
            }
        });
    }

    public void precacheAd() {
        final AppLovinSdk sdk = AppLovinSdk.getInstance(getContext());
        sdk.getNativeAdService().precacheResources(nativeAd, new AppLovinNativeAdPrecacheListener() {

            @Override
            public void onNativeAdImagesPrecached(AppLovinNativeAd appLovinNativeAd) {
            }

            @Override
            public void onNativeAdVideoPreceached(AppLovinNativeAd appLovinNativeAd) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(nativeAd.getTitle());
                        AppLovinSdkUtils.safePopulateImageView(icon, Uri.parse(nativeAd.getIconUrl()),
                                AppLovinSdkUtils.dpToPx(getContext(), 50));
                        btn.setText(nativeAd.getCtaText());
                        if (adListener != null) {
                            adListener.onAdLoad(nativeAd);
                        }
                    }
                });
            }

            @Override
            public void onNativeAdImagePrecachingFailed(AppLovinNativeAd appLovinNativeAd, final int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adListener != null) {
                            adListener.onAdFailedToLoad(i);
                        }
                    }
                });
            }

            @Override
            public void onNativeAdVideoPrecachingFailed(AppLovinNativeAd appLovinNativeAd, final int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adListener != null) {
                            adListener.onAdFailedToLoad(i);
                        }
                    }
                });
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        Activity activity = ContextHelper.getActivityFromContext(getContext());
        if (activity != null) {
            activity.runOnUiThread(runnable);
        }
    }
}

