package com.lightcone.feedback.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.lightcone.utils.SharedContext;
import com.lightcone.feedback.http.ErrorType;
import com.lightcone.feedback.http.FeedbackUrl;
import com.lightcone.feedback.http.Http;
import com.lightcone.feedback.http.response.MsgLoadResponse;
import com.lightcone.feedback.http.response.MsgSendResponse;
import com.lightcone.feedback.message.callback.LoadMsgCallback;
import com.lightcone.feedback.message.callback.LoadUnreadCallback;
import com.lightcone.feedback.message.callback.SendMsgCallback;
import com.lightcone.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by chenkehui on 0005 2017/12/5.
 */

public class MessageManager {

    private static MessageManager instance = new MessageManager();

    private String adFileName;
    private Map deviceInfo;
    private SQLiteDatabase database;

    public static MessageManager getInstance() {
        return instance;
    }

    private MessageManager() {

    }

    public void init(String versionFileName) {
        if (versionFileName != null) {
            adFileName = versionFileName.split("\\.")[0];
        } else {
            adFileName = "没有传入广告名";
        }
        initDeviceInfoString();
        LitePal.initialize(SharedContext.context);
        database = LitePal.getDatabase();
    }

    public void loadUnreadCount(final LoadUnreadCallback callback) {
        long time = DataSupport.max(Message.class, "sendTime", long.class);

        Map params = new HashMap();
        params.put("appId", adFileName);
        params.put("token", getDeviceUUID());
        params.put("time", time);
        Http.getInstance().request(FeedbackUrl.fullUrl(FeedbackUrl.loadUnreadCount), params, new
                Http.HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        int unreadCount = 0;
                        try {
                            JSONObject jo = new JSONObject(result);
                            unreadCount = jo.optInt("unread");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (callback != null) {
                            callback.onResult(unreadCount);
                        }
                    }

                    @Override
                    public void onError(ErrorType errorType, String info) {
                        if (callback != null) {
                            callback.onResult(0);
                        }
                    }
                });
    }

    public List<Message> loadLocalMessages() {
        List<Message> msgs = DataSupport.order("sendTime").find(Message.class);
        return msgs;
    }

    public void sendMessage(final Message message, final SendMsgCallback callback) {
        Map params = new HashMap();
        params.put("appId", adFileName);
        params.put("token", getDeviceUUID());
        params.put("msg", message.getMsgContent());
        params.put("extend", message.getExtend());
        params.put("info", deviceInfo);
        Http.getInstance().request(FeedbackUrl.fullUrl(FeedbackUrl.SendMessage), params, new Http
                .HttpCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    long msgId = JsonUtil.readValue(result, MsgSendResponse.class).msgId;
                    message.setMsgId(msgId);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                message.save();

                if (callback != null) {
                    callback.onResult(false);
                }
            }

            @Override
            public void onError(ErrorType errorType, String info) {
                if (callback != null) {
                    callback.onResult(true);
                }
            }
        });
    }

    public void loadMessages(final long time, final LoadMsgCallback callback) {
        Map params = new HashMap();
        params.put("appId", adFileName);
        params.put("token", getDeviceUUID());
        params.put("time", time);
        Http.getInstance().request(FeedbackUrl.fullUrl(FeedbackUrl.LoadMessages), params, new
                Http.HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        List<Message> messages = null;
                        boolean hasMore = true;
                        try {
                            MsgLoadResponse r = JsonUtil.readValue(result, MsgLoadResponse.class);
                            messages = r.msgs;
                            hasMore = !r.eof;
                        } catch (IOException e) {
                            System.out.println("解析json错误");
                        }

                        if (null != messages && messages.size() > 0) {
                            if (time == 0) {
                                DataSupport.deleteAll(Message.class);
                            }

                            for (Message m : messages) {
                                m.save();
                            }
                        }

                        if (callback != null) {
                            callback.onResult(false, hasMore, messages);
                        }
                    }

                    @Override
                    public void onError(ErrorType errorType, String info) {
                        if (callback != null) {
                            callback.onResult(true, false, null);
                        }
                    }
                });
    }


    private void initDeviceInfoString() {
        String appVer;
        try {
            PackageManager manager = SharedContext.context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(SharedContext.context.getPackageName(), 0);
            appVer = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVer = "1.0";
        }

        deviceInfo = new HashMap();
        deviceInfo.put("device", android.os.Build.MODEL);
        deviceInfo.put("osVer", android.os.Build.VERSION.RELEASE);
        deviceInfo.put("osLang", Locale.getDefault().getLanguage());
        deviceInfo.put("appVer", appVer);
        deviceInfo.put("extend", "");
    }

    private String getDeviceUUID() {
        SharedPreferences preference = SharedContext.context.getSharedPreferences("feedback_config",
                Context.MODE_PRIVATE);
        String identity = preference.getString("device_uuid", null);
        if (identity == null) {
            identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("device_uuid", identity).commit();
        }
        return identity;
    }

}
