package com.lightcone.ad;

import android.content.Context;
import android.view.View;

import com.lightcone.utils.SharedContext;
import com.lightcone.ad.admob.AdmobManager;
import com.lightcone.ad.cache.AdConfig;
import com.lightcone.ad.cache.BannerCacheManager;
import com.lightcone.ad.cache.PopupCacheManager;
import com.lightcone.ad.model.AdModel;
import com.lightcone.ad.popad.OnPopAdWindowAdClickListener;
import com.lightcone.ad.popad.OnPopAdWindowCloseListener;
import com.lightcone.ad.popad.PopAdHandler;
import com.lightcone.ad.update.UpdateManager;

import java.util.List;

public class AdManager {

    private Context context;
    private AdConfig adConfig;
    private boolean isFinishInit = false;

    private static AdManager instance = new AdManager();

    public static AdManager getInstance() {
        return instance;
    }

    private AdManager() {

    }

    /**
     * 1.初始化AdParams和AdConfig
     * 2.初始化缓存CacheManager
     * 3.检查更新
     */
    public void  init(String admobBannerId, String admobScreenId, String
            fbBannerId, String fbScreenId, String versionFileName) {
        this.context = SharedContext.context;
        adConfig = new AdConfig(context, admobBannerId, admobScreenId, fbBannerId, fbScreenId,
                versionFileName);
        try {
            AdmobManager.getInstance().init(context);
            BannerCacheManager.getInstance().init();
            PopupCacheManager.getInstance().init();
            PopAdHandler.getInstance().init();
            checkUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isFinishInit = true;
        }
    }

    /**
     * 是否初始化完成
     *
     * @return
     */
    public boolean isFinishInit() {
        return isFinishInit;
    }

    /**
     * 检查广告队列，并前往下载2张未下完的图片
     */
    public void checkAndDownloadImage() {
        UpdateManager.getInstance().checkAndDownloadImage(getAdModelList());
    }

    /**
     * 获取排好顺序的AdModelList
     * 这里一定保证AdModelList不会全空
     * 若AdModelList图片一张未下载，则返回默认配置广告队列
     *
     * @return
     */
    public List<AdModel> getAdModelList() {
        return PopupCacheManager.getInstance().getSortedAdModelList();
    }

    /**
     * 检查配置文件是否有更新
     */
    public void checkUpdate() {
        UpdateManager.getInstance().updatePopupAdVersion();
        UpdateManager.getInstance().updateBannerAdVersion();
    }

    /**
     * 弹窗广告，弹出下一个广告
     *
     * @param parent
     * @return 如果弹窗成功返回true，如果无广告可以弹出，返回false
     */
    public boolean popupNextAd(View parent) {
        return popupNextAd(parent, null, null);
    }

    /**
     * 弹窗广告，弹出下一个自家广告
     *
     * @param parent
     * @return 如果弹窗成功返回true，如果无广告可以弹出，返回false
     */
    public boolean popupNextSelfAd(View parent) {
        return popupNextSelfAd(parent, null, null);
    }

    /**
     * 弹窗广告，弹出下一个广告
     * PS：如果为admob广告，则无法监听点击事件
     *
     * @param parent
     * @param clickListener 广告点击事件
     * @param closeListener 广告关闭事件
     * @return 如果弹窗成功返回true，如果无广告可以弹出，返回false
     */
    public boolean popupNextAd(View parent, OnPopAdWindowAdClickListener clickListener,
                               OnPopAdWindowCloseListener closeListener) {
        if (!isFinishInit) {
            return false;
        }
        return PopAdHandler.getInstance().popupNextAd(parent, clickListener, closeListener);
    }

    /**
     * 弹窗广告，弹出下一个自家广告
     *
     * @param parent
     * @param clickListener 广告点击事件
     * @param closeListener 广告关闭事件
     * @return 如果弹窗成功返回true，如果无广告可以弹出，返回false
     */
    public boolean popupNextSelfAd(View parent, OnPopAdWindowAdClickListener clickListener,
                                   OnPopAdWindowCloseListener closeListener) {
        if (!isFinishInit) {
            return false;
        }
        return PopAdHandler.getInstance().popupNextSelfAd(parent, clickListener, closeListener);
    }

//	/**
//	 * 付费去广告接口，结果在OnRemoveAdListener中异步返回
//	 * @param billing
//	 * @param onRemoveAdListener
//	 */
//	public void removeAdByConsume(Billing billing, OnRemoveAdListener onRemoveAdListener) {
//		BillingHandler.getInstance().removeAdByConsume(billing, onRemoveAdListener);
//	}
//	
//	/**
//	 * 是否消费过去广告
//	 * @return
//	 */
//	public boolean hasConsumeAd() {
//		return BillingHandler.getInstance().hasConsumeAd();
//	}
//	

    public Context getContext() {
        return this.context;
    }

    public AdConfig getAdConfig() {
        return this.adConfig;
    }

}
