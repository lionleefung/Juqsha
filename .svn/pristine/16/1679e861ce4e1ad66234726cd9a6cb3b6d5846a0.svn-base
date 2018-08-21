package com.lightcone.ad.cache;

import android.content.Context;
import android.util.Log;

import com.lightcone.ad.AdManager;
import com.lightcone.ad.helper.AdFileHelper;
import com.lightcone.ad.helper.MD5Helper;
import com.lightcone.ad.model.AdModel;
import com.lightcone.ad.update.PopUpdateListener;
import com.lightcone.ad.update.UpdateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PopupCacheManager {

    private static PopupCacheManager instance = new PopupCacheManager();
    private AdConfig adconfig;

    public static PopupCacheManager getInstance() {
        return instance;
    }

    private List<AdModel> adModelList = new ArrayList<>();
    private List<AdModel> defaultAdModelList = new ArrayList<>();
    private List<Integer> admobPopupList = new ArrayList<>();

    private String localVersion;
    private Set<PopUpdateListener> listenerSet;

    /**
     * 1.把默认配置广告写进目录
     * 2.初始化adModelList,并去下载第一张图片
     */
    public void init() {
        adconfig = AdManager.getInstance().getAdConfig();
        File versionFile = new File(AdFileHelper.getVersionSavePath() + File.separator + adconfig
                .getVersionFileName());
        initAllFiles(versionFile);
        localVersion = AdFileHelper.readFile(versionFile);
        initCache();
    }

    /**
     * 设置配置文件更新监听
     *
     * @param listener
     */
    public void setUpdateListner(PopUpdateListener listener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }

        if (listener != null) {
            listenerSet.add(listener);
        }
    }

    /**
     * 跟新配置文件，只有当配置文件不一致时才去initCache()
     * 因为异步下载，所以加同步
     */
    public void updateCache(String response) {
        if (!MD5Helper.getMD5(response).equals(MD5Helper.getMD5(localVersion))) {
            AdFileHelper.writeResponse(response, AdManager.getInstance().getAdConfig()
                    .getVersionFileName());
            localVersion = response;
            initCache();
            Log.e("CacheManager", "下载更新配置文件");

            for (PopUpdateListener listener : listenerSet) {
                listener.onUpdate();
            }
        }
    }

    /**
     * 图片下载有更新
     */
    public void updateImage() {
        for (PopUpdateListener listener : listenerSet) {
            listener.onImageUpdate();
        }
    }

    /**
     * 初始化AdModelList缓存
     * adOrderList:播放队列，ModelId队列
     * adModelList:播放队列，Model队列
     * 初始化完成，前往下载第一张图片
     */
    public synchronized void initCache() {
        adModelList = getAdModelListFromVersion(localVersion, true);
        //下载第一张图片
        if (adModelList.size() > 0) {
            UpdateManager.getInstance().prepareImage(adModelList.get(0));
        }
    }

    /**
     * 获得排好序的adModelList
     * 如果adModelList为空，或者第一张图片没有下载下来，则播放默认配置文件
     *
     * @return
     */
    public List<AdModel> getSortedAdModelList() {
        List<AdModel> resutList = adModelList;
        if (resutList.size() == 0 || resutList.get(0).getDrawable() == null) {
            resutList = getDefaultAdModelList();
            //赶紧再去下第一张图片
            if (adModelList.size() > 0) {
                UpdateManager.getInstance().prepareImage(adModelList.get(0));
            }
        }
        return resutList;
    }

    public List<Integer> getAdmobPopupList() {
        return admobPopupList;
    }

    /**
     * 获得默认配置广告队列
     *
     * @return
     */
    private List<AdModel> getDefaultAdModelList() {
        if (defaultAdModelList == null || defaultAdModelList.isEmpty()) {
            String defaultJson = AdFileHelper.readDefaultVersionFile(AdManager.getInstance()
                    .getContext(), adconfig.getVersionFileResId());
            defaultAdModelList = getAdModelListFromVersion(defaultJson, false);
        }
        return defaultAdModelList;
    }

    /**
     * 把默认配置文件和广告写进cache目录
     *
     * @param versionFile
     */
    private void initAllFiles(File versionFile) {
        Context context = AdManager.getInstance().getContext();
        if (!versionFile.exists()) {
            AdFileHelper.createFile(context, adconfig.getVersionFileName(), adconfig
                    .getVersionFileResId());
        }
        List<AdModel> adModelList = getDefaultAdModelList();
        for (AdModel adModel : adModelList) {
            File imageFile = new File(adModel.getFilePath());
            if (!imageFile.exists()) {
                int resId = context.getResources().getIdentifier(adModel.getFileName().split("\\" +
                        ".")[0], "raw", context.getPackageName());
                AdFileHelper.createFile(context, adModel.getFileName(), resId);
            }
        }
    }

    /**
     * 从配置文件中读取adModelList
     *
     * @param versionJson
     * @return
     */
    private List<AdModel> getAdModelListFromVersion(String versionJson, boolean needOList) {
        List<Integer> adOrderList = new ArrayList<>();
        List<AdModel> resultList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(versionJson);
            if (needOList) {
                try {
                    admobPopupList = new ArrayList<>();
                    JSONArray admobPopupOrder = json.getJSONArray("o");
                    for (int i = 0; i < admobPopupOrder.length(); i++) {
                        admobPopupList.add(admobPopupOrder.getInt(i));
                    }
                } catch (JSONException e) {
                    admobPopupList.add(1);
                }
            }
            JSONArray adOrder = json.getJSONArray("so");
            for (int i = 0; i < adOrder.length(); i++) {
                adOrderList.add(adOrder.getInt(i));
            }
            JSONArray adList = json.getJSONArray("ads");
            for (int i = 0; i < adOrderList.size(); i++) {
                AdModel adModel = getAdModelById(adOrderList.get(i), adList);
                if (adModel == null) continue;
                resultList.add(adModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private AdModel getAdModelById(int id, JSONArray adList) {
        for (int i = 0; i < adList.length(); i++) {
            try {
                if (adList.getJSONObject(i).getInt("id") == id) {
                    return AdModel.build(adList.getJSONObject(i), AdModel.TYPE_ADMODEL_POPUP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


}
