package com.lightcone.feedback.http;

import com.lightcone.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lightcone.feedback.http.response.HttpResponse;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by chenkehui on 0005 2017/12/5.
 */

public class Http {

    public interface HttpCallback {
        void onSuccess(String result);

        void onError(ErrorType errorType, String info);
    }

    private static Http instance = new Http();

    public static Http getInstance() {
        return instance;
    }

    private OkHttpClient httpClient;

    private Http() {
        httpClient = new OkHttpClient();
    }

    public void request(final String url, final Map<String, String> params, final HttpCallback
            callback) {
        String paramStr = null;
        try {
            paramStr = JsonUtil.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            callback.onError(ErrorType.ParameterConstructError, "参数构造失败");
            return;
        }
        paramStr = EncryptUtil.encrypt(paramStr);
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("data", paramStr).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(ErrorType.RequestError, "请求发送失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        HttpResponse res = JsonUtil.readValue(response.body().string(), HttpResponse.class);
                        res.data = EncryptUtil.decrypt(res.data);
                        callback.onSuccess(res.data);
                    } catch (IOException e) {
                        callback.onError(ErrorType.ResponseParseError, "响应解析失败");
                    }
                } else {
                    callback.onError(ErrorType.ResponseError, response.message());
                }
            }
        });
    }

    public void cdnRequest(String url, final HttpCallback callback) {
        final Request request = new Request.Builder().url(url).get().build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(ErrorType.RequestError, "请求失败!!!");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody body = response.body();
                        if (body != null) {
                            callback.onSuccess(body.string());
                        }
                    } catch (IOException e) {
                        callback.onError(ErrorType.ResponseParseError, "响应解析失败");
                    }
                } else {
                    callback.onError(ErrorType.ResponseError, response.message());
                }
            }
        });
    }

}
