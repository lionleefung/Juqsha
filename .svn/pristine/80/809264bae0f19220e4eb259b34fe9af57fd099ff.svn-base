package com.lightcone.ad.model;

import java.io.File;

import com.lightcone.ad.helper.AdFileHelper;
import com.lightcone.ad.helper.InstallHelper;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AdModel {
	
	public static final int TYPE_ADMODEL_POPUP = 0;
	public static final int TYPE_ADMODEL_BANNER = 1;

	private int id;
	private String fileName;
	private String downloadUrl;
	private String clickUrl;
	private String filePath;
	private int modelType;
	
	public AdModel(int id, String fileName, String downloadUrl, String clickUrl, int modelType) {
		this.id = id;
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
		this.clickUrl = clickUrl;
		this.filePath = AdFileHelper.getVersionSavePath() + File.separator + getFileName();
		this.modelType = modelType;
	}
	
	public static AdModel build(JSONObject json, int modelType) throws JSONException {
		if (modelType == TYPE_ADMODEL_POPUP) {
			return new AdModel(json.getInt("id"), json.getString("fn"), json.getString("du"), json.getString("cu"), modelType);
		} else {
			return new AdModel(json.getInt("id"), json.getString("fn"), json.getString("im"), json.getString("cu"), modelType);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdModel other = (AdModel) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}
	public String getFileName() {
		return fileName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public String getClickUrl() {
		return clickUrl;
	}
	public String getFilePath() {
		return filePath;
	}
	public int getModelType() {
		return modelType;
	}
	
	private Drawable drawable;
	
	/**
	 * 获得位图文件的drawable对象
	 * @return
	 */
	public Drawable getDrawable() {
		if (drawable == null) {
			Bitmap bm = BitmapFactory.decodeFile(getFilePath());
			if (bm == null) {
				return null;
			}
			drawable = new BitmapDrawable(bm);
		}
		return drawable;
	}
	
	public boolean isInstallForAdModel() {
		String cu = this.getClickUrl();
		String[] temp = cu.split("&");
		if (temp == null || temp.length == 0) {
			return false;
		}
		String packageName = temp[0];
		return InstallHelper.isInstallForPackageName(packageName);
	}
	
	
	
}
