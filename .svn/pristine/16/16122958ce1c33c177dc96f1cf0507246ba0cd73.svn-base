package com.lightcone.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;


import com.lightcone.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareBuilder {

    private static final String ShareFormat = "%1$s: https://play.google.com/store/apps/details?id=%2$s";

    final Activity activity;

    String mimeType = "image/*";
    String shareText;
    Uri shareUri;
    String chooseTitle = "share";

    public ShareBuilder(Activity activity , String r) {
        this.activity = activity;
        try {
            String pkgName = activity.getPackageName();
            PackageInfo pkg = activity.getPackageManager().getPackageInfo(pkgName, 0);
            String appName = pkg.applicationInfo.loadLabel(activity.getPackageManager()).toString();
            this.shareText = String.format(ShareFormat, appName, pkgName) + r ;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ShareBuilder buildMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public ShareBuilder buildShareText(String shareText) {
        this.shareText = shareText;
        return this;
    }

    public ShareBuilder buildShareText(int stringResId) {
        this.shareText = shareText + activity.getString(stringResId);
        return this;
    }

    public ShareBuilder prefixShareText(String shareText) {
        this.shareText = shareText + this.shareText;
        return this;
    }

    public ShareBuilder buildShareUri(Uri shareUri) {
        this.shareUri = shareUri;
        return this;
    }

    public ShareBuilder buildChooseTitle(String chooseTitle) {
        this.chooseTitle = chooseTitle;
        return this;
    }

    /**
     * shareUri Uri需要向第三方公开访问权限.一般保存到SD即可
     */
    public void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(mimeType);
        if (shareUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
        }
        if (shareText != null && !"".equals(shareText)) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(shareIntent, chooseTitle));
    }

    /**
     * @param bitmap
     */
    public void shareImage(Bitmap bitmap) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String sharePath = activity.getExternalFilesDir("share") + "/share.png";
        FileUtil.writeBitmapToFile(bitmap, sharePath);
        shareIntent.setType(mimeType);
        shareUri = shareUri != null ? shareUri : Uri.fromFile(new File(sharePath));
        shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
        if (shareText != null && !"".equals(shareText)) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(shareIntent, chooseTitle));
    }

    public void shareToApp(String packageName, Uri shareUri) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(packageName);
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
            shareIntent.setType(mimeType);
            activity.startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app?
            Toast.makeText(activity, "You haven't installed yet.", Toast.LENGTH_SHORT).show();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            activity.startActivity(intent);
        }
    }

    public void shareBitmapToApp(String packageName, Bitmap bitmap) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(packageName);
            String sharePath = activity.getExternalFilesDir("share") + "/share.png";
            FileUtil.writeBitmapToFile(bitmap, sharePath);
            shareUri = shareUri != null ? shareUri : Uri.fromFile(new File(sharePath));
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
            shareIntent.setType(mimeType);
            activity.startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app?
            Toast.makeText(activity, "You haven't installed yet.", Toast.LENGTH_SHORT).show();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            activity.startActivity(intent);
        }
    }

    public void shareMultipleImage(ArrayList<Uri> uriList) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(shareIntent, chooseTitle));
    }

    /**
     * 自定义分享获取可分享列表
     *
     * @param context
     * @param mimeType
     * @return
     */
    public static List<ResolveInfo> getShareApps(Context context, String mimeType) {
        List<ResolveInfo> mApps;
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType(mimeType);
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

}