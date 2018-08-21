package com.lightcone.ad.popad;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.lightcone.ad.AdManager;
import com.lightcone.ad.admob.AdmobManager;
import com.lightcone.ad.admob.AdmobType;
import com.lightcone.ad.cache.PopupCacheManager;
import com.lightcone.ad.helper.ContextHelper;
import com.lightcone.ad.helper.InstallHelper;
import com.lightcone.ad.model.AdModel;
import com.lightcone.ad.update.PopUpdateListener;

import java.util.List;


public class PopAdHandler {

    private static final String SHARED_PREFERENCES_NAME = "POPUP_AD";//sharedprefrence_name
    private static final String POPUP_AD_INDEX = "popup_ad_index";//播放的队列index
    private static final String ADMOB_POPUP_AD_INDEX = "admob_popup_ad_index";//播放的admob队列index

    private static final int MAX_FAULT_TOLERANCE = 3;

    private static PopAdHandler instance = new PopAdHandler();

    public static PopAdHandler getInstance() {
        return instance;
    }

    private Context context;
    private List<AdModel> adModelList;
    private int faultTolerance;

    public void init() {
        faultTolerance = 0;
        this.context = AdManager.getInstance().getContext();
        adModelList = AdManager.getInstance().getAdModelList();
        PopupCacheManager.getInstance().setUpdateListner(new PopUpdateListener() {
            @Override
            public void onUpdate() {
                adModelList = AdManager.getInstance().getAdModelList();
                setAdIndexPopup(0);
                setAdmobIndexPopup(0);
            }

            @Override
            public void onImageUpdate() {
                adModelList = AdManager.getInstance().getAdModelList();
            }
        });
    }

    /**
     * 弹下一个广告，可能是admob广告也可能是自家广告
     * 不足：如果是admob广告，无法监听clickListener
     * 容错：如果无法弹出当前广告，则弹下一个广告，迭代3次都无法弹出广告则直接不弹广告(return false)
     *
     * @param parent
     * @param clickListener
     * @param closeListener
     * @return
     */
    public boolean popupNextAd(View parent, OnPopAdWindowAdClickListener clickListener, OnPopAdWindowCloseListener closeListener) {
//		if (AdManager.getInstance().hasConsumeAd()) {//已经去广告了
//			return false;
//		}
        // 如果没有安装google play.则直接弹google广告即可
        if (!InstallHelper.isInstallGooglePlay() || isInstallForAllAdModel()) {//如果没有安装google play或者所有的AdModel都被安装了，那么直接弹admob广告
            Activity activityForFkingAppLovin = ContextHelper.getActivityFromContext(parent.getContext());
            return AdmobManager.getInstance().displayInterstitial(closeListener, activityForFkingAppLovin);
        }
        if (faultTolerance >= MAX_FAULT_TOLERANCE) {//容错处理
            faultTolerance = 0;
            return false;
        }
        List<Integer> admobPopupList = PopupCacheManager.getInstance().getAdmobPopupList();
		/**这里需要进行判断admobPopupList是否为空，
         * 否则会在下面的95行报错，但是如此一来如
         * 果不配置本地广告也不会报错，请注意！！坑坑坑。。
         */
        if (admobPopupList.size() == 0) {
            admobPopupList.add(AdmobType.AD_TYPE_ADMOB);
        }
        int admobIndex = getAdmobIndexPopup();
        if (admobIndex >= admobPopupList.size()) {
            admobIndex = 0;
            setAdmobIndexPopup(admobIndex);
        }
        boolean res = false;
        if (admobPopupList.get(admobIndex) == AdmobType.AD_TYPE_ADMOB) {
            Activity activityForFkingAppLovin = ContextHelper.getActivityFromContext(parent.getContext());
            res = AdmobManager.getInstance().displayInterstitial(closeListener, activityForFkingAppLovin);
            if(!res) {
                res = popupNextSelfAd(parent, clickListener, closeListener);
            }
        } else {
            res = popupNextSelfAd(parent, clickListener, closeListener);
        }
        setAdmobIndexPopup(admobIndex + 1);
        if (!res) {//如果弹出失败则弹下一个广告
            faultTolerance++;
            return popupNextAd(parent, clickListener, closeListener);
        }
        return res;
    }

    /**
     * 弹自家广告
     *
     * @param parent
     * @param clickListener
     * @param closeListener
     * @return
     */
    public boolean popupNextSelfAd(View parent, OnPopAdWindowAdClickListener clickListener, OnPopAdWindowCloseListener closeListener) {
//		if (AdManager.getInstance().hasConsumeAd()) {//已经去广告了
//			return false;
//		}
        if (!InstallHelper.isInstallGooglePlay()) {//没有装google play，则不弹广告
            return false;
        }
        for (int i = 0; i < getAdModelRealSize(); i++) {
            AdModel adModel = getNextAdModel();
            if (!adModel.isInstallForAdModel()) {
                PopAdWindow popAdWindow = new PopAdWindow(context, parent, adModel);
                popAdWindow.setOnPopAdWindowAdClickListener(clickListener);
                popAdWindow.setOnPopAdWindowCloseListener(closeListener);
                popAdWindow.show();
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否所有adModel都被安装了
     *
     * @return
     */
    private boolean isInstallForAllAdModel() {
        for (int i = 0; i < getAdModelRealSize(); i++) {
            AdModel adModel = adModelList.get(i);
            if (!adModel.isInstallForAdModel()) {
                return false;
            }
        }
        return true;
    }

    private AdModel getNextAdModel() {
        int index = getAdIndexPopup();
        if (index >= adModelList.size()) {
            index = 0;
            setAdIndexPopup(index);
        }
        AdModel adModel = adModelList.get(index);
        if (adModel.getDrawable() == null) {//如果这个广告没有下载完,则重新 开始播第0条广告
            index = 0;
            adModel = adModelList.get(index);
        }
        if (index % 2 == 0) {//如果当前播放的index为偶数，则检查更新
            AdManager.getInstance().checkAndDownloadImage();
        }
        setAdIndexPopup(index + 1);
        return adModel;
    }

    private int getAdModelRealSize() {
        for (int i = 0; i < adModelList.size(); i++) {
            if (adModelList.get(i).getDrawable() == null) {
                return i;
            }
        }
        return adModelList.size();
    }


    private int getAdIndexPopup() {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(POPUP_AD_INDEX, 0);
    }

    private void setAdIndexPopup(int index) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(POPUP_AD_INDEX, index).commit();
    }

    private int getAdmobIndexPopup() {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(ADMOB_POPUP_AD_INDEX, 0);
    }

    private void setAdmobIndexPopup(int index) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(ADMOB_POPUP_AD_INDEX, index).commit();
    }

}
