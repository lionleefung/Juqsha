package com.lightcone.ad.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class HttpConnectionUtil {

	public static enum HttpMethod {
		GET, POST
	}
	
	private static ExecutorService es = Executors.newFixedThreadPool(3);
			
    private static void execute(Runnable runnable){
    	es.execute(runnable);
    }

	/**
	 * 异步连接
	 * 
	 * @param url
	 *            网址
	 * @param method
	 *            Http方法,POST跟GET
	 * @param callback
	 *            回调方法,返回给页面或其他的数据
	 */
	public static void asyncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		asyncConnect(url, null, method, callback);
	}

	/**
	 * 同步方法
	 * 
	 * @param url
	 *            网址
	 * @param method
	 *            Http方法,POST跟GET
	 * @param callback
	 *            回调方法,返回给页面或其他的数据
	 */
	public static void syncConnect(final String url, final HttpMethod method,
			final HttpConnectionCallback callback) {
		syncConnect(url, null, method, callback);
	}

	/**
	 * 异步带参数方法
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            POST或GET要传递的参数
	 * @param method
	 *            方法,POST或GET
	 * @param callback
	 *            回调方法
	 */
	public static void asyncConnect(final String url,
			final Map<String, String> params, final HttpMethod method,
			final HttpConnectionCallback callback) {
		Runnable runnable = new Runnable() {
			public void run() {
				syncConnect(url, params, method, callback);
			}
		};
		
		execute(runnable);
	}

	/**
	 * 同步带参数方法
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            POST或GET要传递的参数
	 * @param method
	 *            方法,POST或GET
	 * @param callback
	 *            回调方法
	 */
	public static void syncConnect(final String url, final Map<String, String> params,
			final HttpMethod method, final HttpConnectionCallback callback) {
		String json = null;
		BufferedReader reader = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, params, method);
			HttpResponse response = client.execute(request);
			if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null; s = reader
						.readLine()) {
					sb.append(s);
				}

				json = sb.toString();
			}
			if (callback != null) {
				callback.execute(json);
			}
		} catch(Exception e){
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// ignore me
			}
		}
	}

	/**
	 * POST跟GET传递参数不同,POST是隐式传递,GET是显式传递
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            参数
	 * @param method
	 *            方法
	 * @return
	 */
	private static HttpUriRequest getRequest(String url, Map<String, String> params,
			HttpMethod method) {
		if (method.equals(HttpMethod.POST)) {
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String name : params.keySet()) {
					listParams.add(new BasicNameValuePair(name, params.get(name)));
				}
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listParams,HTTP.UTF_8);
				HttpPost request = new HttpPost(url);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e) {
				Log.e("<<HttpConnectionUtil>>", "getRequest error", e);
				throw new RuntimeException(e.getMessage(), e);
			}
		} else {
			
			if (params != null) {
				if (url.indexOf("?") < 0) {
					url += "?";
				}
				for (String name : params.keySet()) {
					try {
						url += "&" + name + "="
								+ URLEncoder.encode(params.get(name), "UTF-8");

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			HttpGet request = new HttpGet(url);
			
			return request;
		}
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface HttpConnectionCallback {

		void execute(String response);
	}

}
