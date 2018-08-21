package com.lightcone.ad.cache;


import android.content.Context;

import com.lightcone.cdn.CdnResManager;
import com.lightcone.utils.UrlUtil;

public class AdConfig {

    private static final String AD_FILE_KEY = "gzy/ad.da";
    private static final String BANNER_FILE_KEY = "gzy/banner.json";

    private String admobBannerId;
    private String admobScreenId;
    private String fbBannerId;
    private String fbScreenId;
    private String versionUrl;
    private String versionFileName;
    private int versionFileResId;
    private String bannerFileUrl;
    private String bannerFileName;


    /**
     * 广告配置项
     * private static final String admobBannerId = "ca-app-pub-2461325401788816/5862642883";
     * private static final String admobScreenId = "ca-app-pub-2461325401788816/7339376086";
     * private static final String versionFileName = "ad.da";
     * AdConfig adConfig = new AdConfig(admobBannerId, admobScreenId, versionFileName);
     */
    public AdConfig(Context context, String admobBannerId, String admobScreenId, String fbBannerId, String fbScreenId, String versionFileName) {
        this.admobBannerId = admobBannerId;
        this.admobScreenId = admobScreenId;
        this.fbBannerId = fbBannerId;
        this.fbScreenId = fbScreenId;
        this.versionFileName = versionFileName;
        this.versionFileResId = context.getResources().getIdentifier(versionFileName.split("\\.")[0], "raw", context.getPackageName());
        if (versionFileName.contains(".json")) { //新版的广告配置，使用了CDN
            this.bannerFileName = "banner.json";
            versionUrl = CdnResManager.getInstance().getSelfDispatchBaseUrl()  + "/" + AD_FILE_KEY;
            bannerFileUrl = CdnResManager.getInstance().getSelfDispatchBaseUrl() + "/" + BANNER_FILE_KEY;
            String versionValue = CdnResManager.getInstance().getResLatestVersionParamByRelativeUrl(true, AD_FILE_KEY);
            String bannerValue = CdnResManager.getInstance().getResLatestVersionParamByRelativeUrl(true, BANNER_FILE_KEY);
            if (versionValue != null && !versionFileName.equals("")) {
                versionUrl = String.format("%s?v=%s", CdnResManager.getInstance().getSelfResourceBaseUrl(), versionValue);
            }
            if (bannerValue != null && !bannerValue.equals("")) {
                bannerFileUrl = String.format("%s?v=%s", CdnResManager.getInstance().getSelfResourceBaseUrl(), bannerValue);
            }
        } else if (versionFileName.contains(".da")) {
            this.versionUrl = UrlUtil.getAdConfigDownloadUrl(context, this.versionFileName);
            this.bannerFileUrl = UrlUtil.getAdBannerConfigDownloadUrl(context, this.versionFileName);
            this.bannerFileName = "b_" + this.versionFileName;
        } else {
            throw new IllegalArgumentException("versionFileName后缀有误！！！！");
        }
    }

    public String getAdmobBannerId() {
        return admobBannerId;
    }

    public String getAdmobScreenId() {
        return admobScreenId;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getVersionFileName() {
        return versionFileName;
    }

    public int getVersionFileResId() {
        return versionFileResId;
    }

    public String getBannerFileUrl() {
        return bannerFileUrl;
    }

    public String getBannerFileName() {
        return bannerFileName;
    }

    public String getFbBannerId() {
        return fbBannerId;
    }

    public String getFbScreenId() {
        return fbScreenId;
    }
}
