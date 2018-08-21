package com.lightcone.ad.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


import android.content.Context;
import android.util.Log;

import com.lightcone.ad.AdManager;

public class AdFileHelper {
	
	/**
	 * 直接读取默认广告配置
	 * @param context
	 * @param fileName
	 * @param resId
	 * @return
	 */
	public static String readDefaultVersionFile(Context context, int resId) {
		InputStream ins = context.getResources().openRawResource(resId);
		return readFile(ins);
	}
	
	public static String readFile(File versionFile) {
		try {
			return readFile(new FileInputStream(versionFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void writeResponse(String response, String fileName) {
		Log.e("downloadSuccess", "SCCCCCCCCCCCCC");
		File versionFile = new File(AdFileHelper.getVersionSavePath() + File.separator + fileName);
		FileWriter writer;
		try {
			writer = new FileWriter(versionFile);
			writer.write(response);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(Context context, String fileName, int resId) {
		String fileDir = createDir();
		String filePath = fileDir + File.separator + fileName;
		FileOutputStream fos = null;
		InputStream ins = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			ins = context.getResources().openRawResource(resId);
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = ins.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private static String savePath = "";
	public static String getVersionSavePath() {
		if (savePath != null && !savePath.equals("")) {
			return savePath;
		}
		String saveDir = AdManager.getInstance().getContext().getCacheDir().getPath();
		savePath = saveDir + File.separator + "." + AdManager.getInstance().getContext().getPackageName();
		return savePath;
	}
	
	/**
	 * 读inputstream
	 * @param inputStream
	 * @return
	 */
	private static String readFile(InputStream inputStream) {
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNextLine()) {
			sb.append(scanner.nextLine());
		}
		scanner.close();
		return sb.toString();
	}
	
	/**
	 * 创建父目录
	 * @return
	 */
	private static String createDir() {
		String fileDir = getVersionSavePath();
		File dir = new File(fileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return fileDir; 
	}
}
