package com.lightcone.rate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lightcone.common.R;
import com.lightcone.feedback.FeedbackActivity;
import com.lightcone.googleanalysis.GaManager;

public class LikePopupWindow {

    private Context context;
    private ImageButton close;
    private Button unlike;
    private Button fivestar;
    private PopupWindow likeWindow;
    private ImageView star;
    private View view;
    private AnimationDrawable ad;

    public LikePopupWindow(Context context) {
        this.context = context;
        initLikePopupWindow();
    }

    public void show(View parent) {
        likeWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        initStarAnimotion(view);
    }

    public void dismiss() {
        if (likeWindow != null) {
            ad.stop();
            likeWindow.dismiss();
        }
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void initLikePopupWindow() {
        view = LayoutInflater.from(context).inflate(R.layout.like_popup_window, null);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        likeWindow = new PopupWindow(view, screenWidth, screenHeight);
        likeWindow.setFocusable(true);
        likeWindow.setAnimationStyle(R.anim.likepop_window_anim);

        TextView popText = (TextView) view.findViewById(R.id.pop_text);
        popText.setText(view.getContext().getString(R.string.like_text_under_star, view
                .getContext().getString(R.string.app_name)));

        close = (ImageButton) view.findViewById(R.id.close_btn);
        close.setOnClickListener(new CloseListener());
        fivestar = (Button) view.findViewById(R.id.fivestar);
        fivestar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				GaManager.sendEvent("评分- 给好评");
                try {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(context, "network is not available!", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        String packageName = context.getPackageName();
                        moveToGooglePlay(packageName);
                    }
                } catch (Exception e) {
                    Log.e("LikePopupWindow", "moveToGooglePlay error!", e);
                }
            }
        });
        unlike = (Button) view.findViewById(R.id.unlike);
        unlike.setOnClickListener(new unlikeListener());

    }

    private void initStarAnimotion(View view) {
        star = (ImageView) view.findViewById(R.id.star);
        ad = (AnimationDrawable) star.getBackground();
        ad.start();
    }

    private WindowManager getWindowManager() {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void moveToGooglePlay(String packageName) throws Exception {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    private class CloseListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    private class unlikeListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            GaManager.sendEvent("评分-不喜欢");
            Intent intent = new Intent(context, FeedbackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dismiss();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

}
