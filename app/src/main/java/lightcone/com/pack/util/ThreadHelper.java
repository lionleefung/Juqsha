package lightcone.com.pack.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenkehui on 0019 2018/3/19.
 */

public class ThreadHelper {

    private static Handler handler;

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void runBackground(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.postDelayed(runnable, delay);
    }


    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }


}
