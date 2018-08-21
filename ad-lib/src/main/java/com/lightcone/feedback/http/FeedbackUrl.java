package com.lightcone.feedback.http;

/**
 * Created by chenkehui on 0005 2017/12/5.
 */

public class FeedbackUrl {
//    private static String testDomain = "http://10.17.1.132:8080/feedback/guest";
    private static String testDomain = "https://support.guangzhuiyuan.com/guest";
    private static String officialDomain = "https://support.guangzhuiyuan.com/guest";

    public static String SendMessage = "/message/send";
    public static String LoadMessages = "/message";
    public static String loadUnreadCount = "/message/unread_count";


    public static String fullUrl(String cmd) {
        return testDomain + cmd;
    }

}
