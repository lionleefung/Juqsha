package com.lightcone.cdn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yueweiwei on 2018/4/19.
 */

public class VersionConfig {

    private JSONObject versionJsonObject;

    public VersionConfig(JSONObject versionJsonObject) {
        this.versionJsonObject = versionJsonObject;
    }

    public boolean isConfigContainTheKey(String key) {
        return versionJsonObject.has(key);
    }

    public String getValueByKey(String key) {
        if (versionJsonObject.has(key)) {
            try {
                return versionJsonObject.getString(key);
            } catch (JSONException e) {
                return "";
            }
        } else {
            return "";
        }
    }

}
