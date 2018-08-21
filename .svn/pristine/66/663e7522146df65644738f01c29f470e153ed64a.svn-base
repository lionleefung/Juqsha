package com.lightcone.ad.helper;

import com.lightcone.ad.AdManager;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class InstallHelper {

	/**
	 * 判断是否安装某个应用
	 * @param packageName
	 * @return
	 */
	public static boolean isInstallForPackageName(String packageName) {
		try {
			AdManager.getInstance().getContext().getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	public static boolean isInstallGooglePlay() {
		return isInstallForPackageName("com.android.vending");
	}
	
	public static void openAppInPlayStore(String packageName) {
		String uri = "market://details?id=" + packageName;
		AdManager.getInstance().getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
	
}
