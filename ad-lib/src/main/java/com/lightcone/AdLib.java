package com.lightcone;

import android.content.Context;
import android.os.Debug;

import com.lightcone.ad.AdManager;
import com.lightcone.cdn.CdnResManager;
import com.lightcone.common.R;
import com.lightcone.feedback.FeedbackManager;
import com.lightcone.googleanalysis.GaManager;
import com.lightcone.reinforce.RogueKiller;
import com.lightcone.utils.SharedContext;

/**
 * Created by chenkehui on 0008 2017/12/8.
 */

public class AdLib {

    private static final String admobBannerId = "ca-app-pub-1882112346230448/4190684158";
    private static final String admobScreenId = "ca-app-pub-1882112346230448/4433500386";
    private static final String fbBannerId = "";
    private static final String fbScreenId = "";

    public static String admobNative = "ca-app-pub-1882112346230448/6071129070";
    public static String fbNative = "";
    /**
     * versionFileName的名字就是gzyName
     * 使用CDN后的新广告配置的versionFileName的后缀为json，旧应用仍为da, 因此使用最新版ad-lib时，versionFileName记得把后缀也写上
     */
    private static final String versionFileName = "a_wur6fi0zw6ulb6b.json";

    /**
     * 设置直接使用某个APP的线上资源（不包含广告配置，因此前面的versionFileName照常配），
     * otherAppGzyName即为要使用App的versionFileName，如果配置该字段，则下载的v.json
     * 为对应使用的App的v.json.
     * <p>
     * 本字段只和资源文件相关，不可用于广告配置中！
     */
    private static final String otherAppResGzyName = "";

    public static void init(Context context) {
        SharedContext.context = context;

        if (!Debug.isDebuggerConnected()) {
            RogueKiller.init(context, false);
        }

        CdnResManager.getInstance().init(versionFileName, otherAppResGzyName); //在AdManager之前初始化
        GaManager.initAppTracker(context, R.xml.app_tracker);
        AdManager.getInstance().init(admobBannerId, admobScreenId, fbBannerId, fbScreenId,
                versionFileName);
        FeedbackManager.getInstance().init(versionFileName);
    }
}
