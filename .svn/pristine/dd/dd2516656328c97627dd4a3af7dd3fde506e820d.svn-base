////////////////////////////////////////////////////////////////////////////////
//
//  AdmobBannarActivity 
//
//  Copyright (C) 2015	huxiaofei
//
//  使用方法：
//  1.如果activity中需要使用banner广告，请将自己的Activity继承AdmobBannerActivity
//  2.如需改变AdView属性，请在onCreate方法中调用
//	3.在需要显示banner位置的地方加上以下布局代码    
//
//	<RelativeLayout
//  	android:id="@+id/ad_layout"
//   	android:layout_width="match_parent"
//      android:layout_height="wrap_content">
//      <include layout="@layout/admob_banner_layout"/>
//  </RelativeLayout>
//
//
///////////////////////////////////////////////////////////////////////////////
package com.lightcone.ad.admob.banner;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.google.android.gms.ads.AdSize;

import java.util.Locale;

/**
 * 使用说明见顶部↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 *
 * @author huxiaofei
 */
public class AdmobBannarActivity extends Activity {

    private AdmobBannerHandler admobBannerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        showLanguage(language);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    protected void setBannerSize(AdSize adSize) {
        admobBannerHandler.setBannerSize(adSize);
    }

    protected void setAdViewVisible(int visibility) {
        admobBannerHandler.setAdViewVisible(visibility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (admobBannerHandler == null) {
            admobBannerHandler = new AdmobBannerHandler(this);
        }
        admobBannerHandler.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        admobBannerHandler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        admobBannerHandler.onDestroy();
    }

    protected void showLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //保存设置语言的类型（这个里面我先保存一下，下面会讲到他的用处）
        //PreferenceUtil.commitString("language", language);
    }
}
