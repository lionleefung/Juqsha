package com.lightcone.feedback.misc;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by chenkehui on 0006 2017/12/6.
 */

public class KeyboardHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface Listener {
        void onKeyboardOpened(int keyboardHeightInPx);

        void onKeyboardClosed();
    }

    private final View activityRootView;
    public boolean isSoftKeyboardOpened;
    private Listener listener;

    public KeyboardHelper(View activityRootView, Listener listener) {
        this.listener = listener;
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = false;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still
        // visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        int screenHeight = activityRootView.getRootView().getHeight();
        int heightDiff = screenHeight - (r.bottom - r.top);
        if (!isSoftKeyboardOpened && heightDiff > 200) { // if more than 100
            // pixels, its probably
            // a keyboard...
            isSoftKeyboardOpened = true;
            if (listener != null) {
                listener.onKeyboardOpened(heightDiff);
            }
        } else if (isSoftKeyboardOpened && heightDiff < 200) {
            isSoftKeyboardOpened = false;
            if (listener != null) {
                listener.onKeyboardClosed();
            }
        }
    }
}
