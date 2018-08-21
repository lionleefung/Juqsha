package lightcone.com.pack.util.res;

import android.content.Context;

import lightcone.com.pack.MyApplication;


public class PxUtil {

    private static final Context context = MyApplication.appContext;

    public static final PxUtil instance = new PxUtil();

    private PxUtil() {
    }

    public float dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    public float px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public float px2sp(float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale + 0.5f;
    }

    public float sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

}
