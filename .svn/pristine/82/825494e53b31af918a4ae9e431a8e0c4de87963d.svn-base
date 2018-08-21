package com.lightcone.ad.update;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lightcone.ad.AdManager;
import com.lightcone.ad.cache.BannerCacheManager;
import com.lightcone.ad.cache.PopupCacheManager;
import com.lightcone.ad.helper.AdFileHelper;
import com.lightcone.ad.helper.Callback;
import com.lightcone.ad.helper.DownloadHelper;
import com.lightcone.ad.helper.HttpConnectionUtil;
import com.lightcone.ad.model.AdModel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UpdateManager {
	
	//轮询更新广告配置，间隔时间
	private static final long TIME_LOOP_UPDATE_VERSION = 12 * 60 * 60 * 1000;

	private static UpdateManager instance = new UpdateManager();
	
	public static UpdateManager getInstance() {
		return instance;
	}
	
	private Executor threadPool = Executors.newSingleThreadExecutor();//广告下载线程

	private UpdateManager() {
		//每隔一定时间，更新一次广告配置文件
		Thread updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try{
						Thread.sleep(TIME_LOOP_UPDATE_VERSION);
						updatePopupAdVersion();
						updateBannerAdVersion();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateThread.start();
	}
	
	/**
	 * 检查是否有popup配置文件更新,若有，则下载保存
	 */
	public void updatePopupAdVersion() {
		downloadVersion(AdManager.getInstance().getAdConfig().getVersionUrl(), new Callback<String>() {
			@Override
			public void onCallback(String response) {
				if (response != null) {
					PopupCacheManager.getInstance().updateCache(response);
				}
			}
		});
	}
	
	/**
	 * 检查是否有banner配置文件更新,若有，则下载保存
	 */
	public void updateBannerAdVersion() {
		downloadVersion(AdManager.getInstance().getAdConfig().getBannerFileUrl(), new Callback<String>() {
			@Override
			public void onCallback(String response) {
				if (response != null) {
					BannerCacheManager.getInstance().updateCache(response);
				}
			}
		});
	}
	
	/**
	 * 检查所有图片是否下载完成，若有没下载完的，则最多下载2张
	 */
	public void checkAndDownloadImage(List<AdModel> adModelList) { 
		checkAndDownloadImage(adModelList, 2);
	}
	
	/**
	 * 检查所有图片是否下载完成，若有没下载完的，则最多下载num张
	 */
	public void checkAndDownloadImage(List<AdModel> adModelList, int num) {
		int temp = 0;
		for (int i = 0; i < adModelList.size(); i++) {
			AdModel adModel = adModelList.get(i);
			if (adModel.getDrawable() == null) {
				prepareImage(adModel);
				temp++;
			}
			if (temp >= num) {
				break;
			}
		}
	}
	
	/**
	 * 下载图片,根据adModel下载
	 * @param adModel
	 */
	public void prepareImage(final AdModel adModel) {
		if (adModel.getDrawable() != null) {
			return;
		}
		String path = AdFileHelper.getVersionSavePath();
		File imgeFile = new File(path + File.separator + adModel.getFileName());
		if (imgeFile.exists() && adModel.getDrawable() == null) {
			imgeFile.delete();
		}
		downloadImage(adModel.getDownloadUrl(), adModel.getFileName(), new Callback<Integer>() {
			@Override
			public void onCallback(Integer response) {
				if (adModel.getModelType() == AdModel.TYPE_ADMODEL_POPUP && response == 0) {
					PopupCacheManager.getInstance().updateImage();
				}
			}
		});
	}
	
	//=====================================================================================
	//=====================================================================================
	//====================================私有方法============================================
	//=====================================================================================
	//=====================================================================================
	
	/**
	 * 下载配置文件,新启线程下载,然后回调
	 */
	private void downloadVersion(final String url, final Callback<String> callback) {
		if (!isNetworkAvailable()) {
			return;
		}
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				HttpConnectionUtil.syncConnect(url, HttpConnectionUtil.HttpMethod.GET, new HttpConnectionUtil.HttpConnectionCallback() {
					@Override
					public void execute(String response) {
						if (callback != null) {
							callback.onCallback(response);
						}
					}
				});
			}
		});
	}
	
	/**
	 * 新启线程下载图片就好了，不用管下载成功或者失败
	 * @param url
	 * @param fileName
	 */
	private void downloadImage(final String url, final String fileName, final Callback<Integer> callback) {
		if (!isNetworkAvailable()) {
			return;
		}
		final String path = AdFileHelper.getVersionSavePath();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				int res = DownloadHelper.downloadFile(url, path + File.separator + fileName);
				if (callback != null) {
					callback.onCallback(res);
				}
			}
		});
	}
	
	public boolean isNetworkAvailable() {
		try {
			Context context = AdManager.getInstance().getContext();
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			return (info != null && info.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
