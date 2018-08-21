package com.lightcone.ad.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadHelper {

	public static void asyncDownloadFile(String url, String saveFilePath, Runnable runnalbe) {
		
	}
	
	/**
	 * 直接下载文件
	 * @param url
	 * @param saveFilePath
	 * @return 成功返回0
	 */
	public static int downloadFile(String url, String saveFilePath) {
		InputStream inputStream = getInputStreamFromUrl(url);
		if (inputStream == null) {
			return -1;
		}
		File resultFile = writeFile(saveFilePath, inputStream);
		if (resultFile == null) {
			return -1;
		}
		return 0;
	}
	
	private static InputStream getInputStreamFromUrl(String urlStr) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

			if (urlConn.getResponseCode() == 200){
				inputStream = urlConn.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return inputStream;
	}
	
	private static File writeFile(String saveFilePath, InputStream inputStream) {
		File file = new File(saveFilePath);
		OutputStream outStream = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			outStream = new FileOutputStream(file);
			byte[] buffer = new byte[4*1024];
			int count = 0;// 循环写出
			while ((count = inputStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, count);
			}
			outStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
}
