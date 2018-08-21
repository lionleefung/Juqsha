package com.lightcone.cdn;

/**
 * Created by yueweiwei on 2018/4/19.
 */

import android.support.annotation.Nullable;
import android.util.Log;

import com.lightcone.feedback.http.ErrorType;
import com.lightcone.feedback.http.Http;
import com.lightcone.utils.SharedContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * *********************************************************************************************
 * 详情见：CDN文件缓存管理办法.doc(张丹)
 * v.json的key-value格式为："image/title.png":"1521600765992"
 * （其中image对应文件类型的文件夹，title.png对应文件名称,可能还存在"video/hello.mp4":"1521600764959"）
 * *********************************************************************************************
 */

public class CdnResManager {

    private final String TAG = getClass().getName();

    /**
     * 分发服务器（源服务器）域名，版本控制文件v.json在这里下载
     */
    public static final String DISPATCH_SERVER_URL = "https://dispatch.guangzhuiyuan.com/";

    /**
     * 资源服务器（CDN服务器）域名，各种配置、图片、视频等资源文件在这里下载
     */
    public static final String RESOURCE_SERVER_URL = "https://src.guangzhuiyuan.com/";

    /**
     * 分发服务器上的版本控制文件名
     */
    public static final String LAST_CONFIG_JSON_NAME = "gzy/v.json";

    private static final String LOCAL_SELF_VERSION_FILE_NAME = "saved_self_v.json";

    private static final String LOCAL_OTHER_VERSION_FILE_NAME = "saved_other_v.json";

    private static CdnResManager instance = null;

    private String selfDispatchBaseUrl;

    private String selfResourceBaseUrl;

    private String otherDispatchBaseUrl;

    private String otherResourceBaseUrl;

    private VersionConfig selfVersionConfig = null;

    private VersionConfig otherVersionConfig = null;

    public static CdnResManager getInstance() {
        if (instance == null) {
            synchronized (CdnResManager.class) {
                if (instance == null) {
                    instance = new CdnResManager();
                }
            }
        }
        return instance;
    }

    private CdnResManager() {

    }

    /**
     * 初始化方法在AdLib初始化中自动调用了，不需要手动调用
     *
     * @param gzyName
     * @param otherAppGzyName
     */
    public void init(String gzyName, String otherAppGzyName) {
        if (isEmpty(gzyName) && isEmpty(otherAppGzyName)) {
            Log.e(TAG, "CDN资源服务器所需的gzyName没有配置，请检查！如果确实不需要，请忽略。");
        }
        otherDispatchBaseUrl = DISPATCH_SERVER_URL + formatGzyName(otherAppGzyName);
        otherResourceBaseUrl = RESOURCE_SERVER_URL + formatGzyName(otherAppGzyName);
        selfDispatchBaseUrl = DISPATCH_SERVER_URL + formatGzyName(gzyName);
        selfResourceBaseUrl = RESOURCE_SERVER_URL + formatGzyName(gzyName);
        selfVersionConfig = loadLocalVersionConfig(LOCAL_SELF_VERSION_FILE_NAME);
        otherVersionConfig = loadLocalVersionConfig(LOCAL_OTHER_VERSION_FILE_NAME);
        downLoadVersionConfig(true, null);
        downLoadVersionConfig(false, null);
    }

    /**
     * 下载dispatch服务器上该App的资源版本配置文件v.json,下载完的刷新操作在OnCdnVersionConfigLoadCompleteListener中自己完成，
     * 注意loadCompleteListener是在子线程中调用的，更新UI自行抛到UI线程！！！
     *
     * @param loadCompleteListener
     */
    public void downLoadVersionConfig(final boolean isSelfCDN, @Nullable final OnCdnVersionConfigLoadCompleteListener loadCompleteListener) {
        String jsonUrl = (isSelfCDN ? selfDispatchBaseUrl : otherDispatchBaseUrl) + LAST_CONFIG_JSON_NAME;
        final String versionJsonUrl = String.format("%s?v=%s", jsonUrl, System.currentTimeMillis() + "");
        Http.getInstance().cdnRequest(versionJsonUrl, new Http.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!isEmpty(result)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        jsonObject = null;
                    }
                    if (jsonObject != null) {
                        saveStringToPrivate(result, isSelfCDN ? LOCAL_SELF_VERSION_FILE_NAME : LOCAL_OTHER_VERSION_FILE_NAME);
                        VersionConfig versionConfig = new VersionConfig(jsonObject);
                        if (loadCompleteListener != null) {
                            loadCompleteListener.onComplete(true, versionConfig);
                        }
                    } else {
                        if (loadCompleteListener != null) {
                            loadCompleteListener.onComplete(false, null);
                        }
                    }
                } else {
                    if (loadCompleteListener != null) {
                        loadCompleteListener.onComplete(false, null);
                    }
                }
            }

            @Override
            public void onError(ErrorType errorType, String info) {
                if (loadCompleteListener != null) {
                    loadCompleteListener.onComplete(false, null);
                }
            }
        });
    }

    /**
     * 传入资源的相对路径（即v.json里的key值），返回资源最新版本的URL，如果没有，则返回原URL
     * 如：传入资源https://src.guangzhuiyuan.com/a_sidt3p2pe6q0ls/image/title.png的相对路径为：image/title.png
     * 返回：https://src.guangzhuiyuan.com/a_sidt3p2pe6q0ls/image/title.png?v=1521600765992
     *
     * @param resRelativeUrl
     * @return
     */
    public String getResLatestUrlByRelativeUrl(boolean isSelfCDN, String resRelativeUrl) {
        resRelativeUrl = resRelativeUrl.trim();
        if (isEmpty(resRelativeUrl)) {
            return null;
        }
        String absoluteUrl = (isSelfCDN ? selfResourceBaseUrl : otherDispatchBaseUrl) + resRelativeUrl;
        VersionConfig versionConfig = isSelfCDN ? selfVersionConfig : otherVersionConfig;
        if (versionConfig != null && versionConfig.isConfigContainTheKey(resRelativeUrl)) {
            String versionVale = versionConfig.getValueByKey(resRelativeUrl);
            absoluteUrl = String.format("%s?v=%s", absoluteUrl, versionVale);
        }
        return absoluteUrl;
    }

    /**
     * 传入资源的绝对路径，返回资源最新版本的URL，如果没有，则返回原URL,传入的resAbsoluteUrl的域名必须是RESOURCE_SERVER_URL
     * 如：传入资源https://src.guangzhuiyuan.com/a_sidt3p2pe6q0ls/image/title.png
     * 返回：https://src.guangzhuiyuan.com/a_sidt3p2pe6q0ls/image/title.png?v=1521600765992
     *
     * @param resAbsoluteUrl
     * @return
     */
    public String convertToLatestUrl(boolean isSelfCDN, String resAbsoluteUrl) {
        resAbsoluteUrl = resAbsoluteUrl.trim();
        if (isEmpty(resAbsoluteUrl)) {
            return null;
        }
        String resRelativeUrl = getRelativeUrlByAbsoluteUrl(isSelfCDN, resAbsoluteUrl);
        return getResLatestUrlByRelativeUrl(isSelfCDN, resRelativeUrl);
    }

    /**
     * 传入资源的相对路径（即v.json里的key值），返回资源最新版本的版本号，如果没有则返回null
     *
     * @param resRelativeUrl
     * @return
     */
    public String getResLatestVersionParamByRelativeUrl(boolean isSelfCDN, String resRelativeUrl) {
        resRelativeUrl = resRelativeUrl.trim();
        if (isEmpty(resRelativeUrl)) {
            return "";
        }
        VersionConfig versionConfig = isSelfCDN ? selfVersionConfig : otherVersionConfig;
        if (versionConfig != null && versionConfig.isConfigContainTheKey(resRelativeUrl)) {
            String versionValue = versionConfig.getValueByKey(resRelativeUrl);
            return versionValue;
        }
        return "";
    }

    private String getRelativeUrlByAbsoluteUrl(boolean isSelfCDN, String resAbsoluteUrl) {
        String relativeUrl = resAbsoluteUrl.trim().replace(isSelfCDN ? selfResourceBaseUrl : otherResourceBaseUrl, "");
        if (isEmpty(relativeUrl)) {
            return resAbsoluteUrl;
        }
        //key的头尾不包含斜杠/
        char firstLetter = relativeUrl.charAt(0);
        if (firstLetter == '/') {
            relativeUrl = relativeUrl.substring(1);
        }
        char lastLetter = relativeUrl.charAt(relativeUrl.length() - 1);
        if (lastLetter == '/') {
            relativeUrl = relativeUrl.substring(0, relativeUrl.length() - 1);
        }
        return relativeUrl;
    }

    public String getSelfDispatchBaseUrl() {
        return selfDispatchBaseUrl;
    }

    public String getSelfResourceBaseUrl() {
        return selfResourceBaseUrl;
    }

    public String getOtherDispatchBaseUrl() {
        return otherDispatchBaseUrl;
    }

    public String getOtherResourceBaseUrl() {
        return otherResourceBaseUrl;
    }

    private String formatGzyName(String gzyName) {
        return gzyName.split("\\.")[0] + "/";
    }

    private boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    private VersionConfig loadLocalVersionConfig(String fileName) {
        String savedStr = null;
        try {
            FileInputStream fis = SharedContext.context.openFileInput(fileName);
            int len = fis.available();
            byte[] buffer = new byte[len];
            fis.read(buffer);
            savedStr = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!isEmpty(savedStr)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(savedStr);
            } catch (JSONException e) {
                e.printStackTrace();
                jsonObject = null;
            }
            if (jsonObject != null) {
                return new VersionConfig(jsonObject);
            }
        }
        return null;

    }

    private void saveStringToPrivate(String data, String fileName) {
        try {
            FileOutputStream fout = SharedContext.context.openFileOutput(fileName, MODE_PRIVATE);
            byte[] bytes = data.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
