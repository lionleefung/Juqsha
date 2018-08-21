package lightcone.com.pack;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lightcone.AdLib;

import lightcone.com.pack.model.PlotDescribeModel;
import lightcone.com.pack.util.ThreadHelper;

/**
 * Created by panjianye on 2018/3/7.
 */

public class MyApplication extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        AdLib.init(this);
        initData();
    }

    private void initData() {
        ThreadHelper.runBackground(new Runnable() {
            @Override
            public void run() {
                PlotDescribeModel.getInstance().init();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
