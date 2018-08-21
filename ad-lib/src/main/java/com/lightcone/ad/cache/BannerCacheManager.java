package com.lightcone.ad.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lightcone.ad.AdManager;
import com.lightcone.ad.admob.AdmobType;
import com.lightcone.ad.helper.AdFileHelper;
import com.lightcone.ad.helper.InstallHelper;
import com.lightcone.ad.helper.MD5Helper;
import com.lightcone.ad.model.AdModel;
import com.lightcone.ad.update.UpdateManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BannerCacheManager {
	
	private static BannerCacheManager instance = new BannerCacheManager();
	
	public static BannerCacheManager getInstance() {
		return instance;
	}
	
	private String localVersion = "";
	private BannerData bannerData;
	
	public void init() {
		initCache();
	}
	
	/**
	 * 获取下一个将要展示bannerId
	 * @return
	 */
	public int getNextBannerId(boolean isNeedAdmob) {
		if (!InstallHelper.isInstallGooglePlay()) {
			return AdmobType.AD_TYPE_ADMOB;
		}
		if (bannerData == null || bannerData.modelRatioList.size() == 0) {
			return AdmobType.AD_TYPE_ADMOB;
		}
		try {
			float totalRatio = bannerData.getTotalRatio(isNeedAdmob);
			if (totalRatio == 0) {
				return AdmobType.AD_TYPE_ADMOB;
			}
			double random = Math.random() * totalRatio;
//			System.out.println("random:" + random);
			return bannerData.getIdForRandomRatio(random, isNeedAdmob);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AdmobType.AD_TYPE_ADMOB;
	}
	
	/**
	 * 根据bannerId获取AdModel实例,如果bannerId为0，则返回null
	 * @param id
	 * @return
	 */
	public AdModel getBannerAdModelById(int id) {
		try {
			if (id == AdmobType.AD_TYPE_ADMOB) {
				return null;
			}
			return bannerData.bannerModelMap.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateCache(String response) {
		if (!MD5Helper.getMD5(response).equals(MD5Helper.getMD5(localVersion))) {//说明有更新
			enableUpdate(response);
		}
	}
	
	public void enableUpdate(String response) {
		AdFileHelper.writeResponse(response, AdManager.getInstance().getAdConfig().getBannerFileName());
		initCache();
	}
	
	private synchronized void initCache() {
		File versionFile = new File(AdFileHelper.getVersionSavePath() + File.separator + AdManager.getInstance().getAdConfig().getBannerFileName());
		if (versionFile.exists()) {
			localVersion = AdFileHelper.readFile(versionFile);
			this.bannerData = buildJsonFile(localVersion);
		}
	}
	
	private BannerData buildJsonFile(String content) {
		List<ModelRatio> modelRatioList = new ArrayList<>();
		Map<Integer, AdModel> bannerModelMap = new HashMap<>();
		try {
			JSONObject json = new JSONObject(content);
			JSONObject o = json.getJSONObject("o");
			JSONArray bs = json.getJSONArray("bs");
			for (Iterator<String> iterator = o.keys();iterator.hasNext();) {
				String key = iterator.next();
				String ratio = o.getString(key);
				int iKey = Integer.parseInt(key);
				modelRatioList.add(new ModelRatio(iKey, Float.parseFloat(ratio)));
				if (iKey != AdmobType.AD_TYPE_ADMOB) {
					AdModel adModel = getAdModelById(iKey, bs);
					if (adModel != null) {
						bannerModelMap.put(iKey, adModel);
						if (!adModel.isInstallForAdModel() && adModel.getDrawable() == null) {
							UpdateManager.getInstance().prepareImage(adModel);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BannerData(modelRatioList, bannerModelMap);
	}
	
	private AdModel getAdModelById(int id, JSONArray bs) throws JSONException {
		for (int i = 0; i < bs.length(); i++) {
			JSONObject item = bs.getJSONObject(i);
			if (item.getInt("id") == id) {
				return AdModel.build(item, AdModel.TYPE_ADMODEL_BANNER);
			}
		}
		return null;
	}
	
	//========================================以下为两个内部类=================================================
	//===================================================================================================
	//===================================================================================================
	static class ModelRatio {
		int id;
		float ratio;
		
		private ModelRatio(int id, float ratio) {
			this.id = id;
			this.ratio = ratio;
		}
	}
	
	static class BannerData {
		List<ModelRatio> modelRatioList;
		Map<Integer, AdModel> bannerModelMap;
		
		public BannerData(List<ModelRatio> modelRatioList, Map<Integer, AdModel> bannerModelMap) {
			this.modelRatioList = modelRatioList;
			this.bannerModelMap = bannerModelMap;
		}
		
		/**
		 * 获取总权重
		 * @return
		 */
		float getTotalRatio(boolean isContainAdmob) {
			float totalRatio = 0;
			for (ModelRatio modelRatio : modelRatioList) {
				if (!isContainAdmob && modelRatio.id == AdmobType.AD_TYPE_ADMOB) {
					continue;
				}
				if (isAvailable(modelRatio)) {
					totalRatio += modelRatio.ratio;
				}
			}
			return totalRatio;
		}
		
		/**
		 * 根据随机数，获取得到将要展示的id
		 * @param random
		 * @return
		 */
		int getIdForRandomRatio (double random, boolean isContainAdmob) {
			float temp = 0;
			for (ModelRatio modelRatio : modelRatioList) {
				if (!isContainAdmob && modelRatio.id == AdmobType.AD_TYPE_ADMOB) {
					continue;
				}
				if (isAvailable(modelRatio)) {
					temp = temp + modelRatio.ratio;
					if (temp >= random) {
						return modelRatio.id;
					}
				}
			}
			return 0;
		}
		
		private boolean isAvailable(ModelRatio modelRatio) {
			if (modelRatio.id == AdmobType.AD_TYPE_ADMOB) {
				return true;
			}
			AdModel adModel = bannerModelMap.get(modelRatio.id);
			if (adModel == null || adModel.isInstallForAdModel()) {//如果安装了，肯定不可用
				return false;
			}
			if (adModel.getDrawable() == null) {
				UpdateManager.getInstance().prepareImage(adModel);
				return false;
			}
			return true;
		}
	}
	
}
