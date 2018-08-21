package com.lightcone.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UrlUtil {

    private static final String SITE_DNS_PRE = "www.";
    private static final String DOWNLOAD_DNS_PRE = "dl.";
    private static final String DEFAULT_DNS = "smilingwhitebear.com";

    private static final String FEEDBACK_URL = "http://%s/feedbackserver/feedback.jsp";
    public static final String AD_CONFIG_URL = "http://#0/as/config/#1";
    public static final String AD_BANNER_CONFIG_URL = "http://#0/as/config/banner/#1";

    private static Map<String, String> packagePrefix_dns;

    static {
        packagePrefix_dns = new HashMap<>();
        packagePrefix_dns.put("org.gzy.", "guangzhuiyuan.com");
        packagePrefix_dns.put("org.cloudcity.", "guangzhuiyuan.com");
        packagePrefix_dns.put("org.skydomain.", "jumpjumpcat.com");
        packagePrefix_dns.put("com.lightcone.", "guangzhuiyuan.com");
        packagePrefix_dns.put("com.koerupton.", "deepthinkingcat.com");
        packagePrefix_dns.put("com.overchunk.", "smilingwhitebear.com");
        packagePrefix_dns.put("com.flankerch.", "guangzhuiyuan.com");
        packagePrefix_dns.put("com.primedemo.", "guangzhuiyuan.com");
        packagePrefix_dns.put("com.risingure.", "guangzhuiyuan.com");
        packagePrefix_dns.put("com.gernersis.", "guangzhuiyuan.com");
        packagePrefix_dns.put("com.dilychang.", "deepthinkingcat.com");
        packagePrefix_dns.put("com.auditoner.", "deepthinkingcat.com");
    }

    public static String getSiteDns(Context context) {
        String dns = getDns(context);
        return SITE_DNS_PRE + dns;
    }

    public static String getDownloadDns(Context context) {
        String dns = getDns(context);
        return DOWNLOAD_DNS_PRE + dns;
    }

    public static String getFeedbackUrl(Context context) {
        String dns = getSiteDns(context);
        return String.format(FEEDBACK_URL, dns);
    }

    public static String getAdConfigDownloadUrl(Context context, String fileName) {
        String dns = getDownloadDns(context);
        return AD_CONFIG_URL.replaceAll("#0", dns).replaceAll("#1", fileName);
    }

    public static String getAdBannerConfigDownloadUrl(Context context, String fileName) {
        String dns = getDownloadDns(context);
        return AD_BANNER_CONFIG_URL.replaceAll("#0", dns).replaceAll("#1", fileName);
    }

    private static String getDns(Context context) {
        String packageName = context.getPackageName();
        String dns = null;
        for (Entry<String, String> en : packagePrefix_dns.entrySet()) {
            if (packageName.startsWith(en.getKey())) {
                dns = en.getValue();
                break;
            }
        }
        if (dns == null) {
            dns = DEFAULT_DNS;
        }
        return dns;
    }
    }

