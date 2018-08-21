package lightcone.com.pack.ad;

import android.view.View;

import com.lightcone.ad.AdManager;
import com.lightcone.ad.helper.Callback;
import com.lightcone.ad.popad.OnPopAdWindowAdClickListener;
import com.lightcone.ad.popad.OnPopAdWindowCloseListener;

/**
 * Created by panjianye on 2018/4/1.
 */
public class AdHelper {
    private static int threeCount = 0;
    private static int twiceCount = 0;

    public static void popAd(View view, final Callback<Boolean> callback) {
        if (view != null && !AdManager.getInstance().popupNextAd(view, new OnPopAdWindowAdClickListener() {
                    @Override
                    public void onPopAdWindowClick() {
                    }
                },
                new OnPopAdWindowCloseListener() {
                    @Override
                    public void onPopAdWindowClose() {
                        callback.onCallback(true);
                    }
                })) {
            callback.onCallback(false);
        }
    }

    public static void threeAdPop(final View view, final Callback<Boolean> callback) {
        threeCount++;
        if (view != null && threeCount % 3 == 0) {
            if (!AdManager.getInstance().popupNextAd(view, new OnPopAdWindowAdClickListener() {
                        @Override
                        public void onPopAdWindowClick() {
                        }
                    },
                    new OnPopAdWindowCloseListener() {
                        @Override
                        public void onPopAdWindowClose() {
                            callback.onCallback(true);
                        }
                    })) {
                callback.onCallback(false);
            }
        } else {
            callback.onCallback(false);
        }
    }

    public static void teiceSellAdPop(final View view, final Callback<Boolean> callback) {
        twiceCount++;
        if (view != null && twiceCount % 2 == 0 && twiceCount < 6) {
            if (!AdManager.getInstance().popupNextAd(view, new OnPopAdWindowAdClickListener() {
                        @Override
                        public void onPopAdWindowClick() {
                        }
                    },
                    new OnPopAdWindowCloseListener() {
                        @Override
                        public void onPopAdWindowClose() {
                            callback.onCallback(true);
                        }
                    })) {
                callback.onCallback(false);
            }
        } else {
            callback.onCallback(false);
        }
    }
}