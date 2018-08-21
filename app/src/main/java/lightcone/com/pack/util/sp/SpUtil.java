package lightcone.com.pack.util.sp;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.Map;

import lightcone.com.pack.MyApplication;

public class SpUtil {
    private static SpUtil instance;

    private Context context;
    private String tempFile;
    private Map<String, SpWrapper> sps = new LinkedHashMap<>();

    public static SpUtil getInstance() {
        if (instance == null) {
            instance = new SpUtil();
        }
        return instance;
    }

    private SpUtil() {
        init(MyApplication.appContext);
    }

    private void init(Context applicationContext) {
        context = applicationContext;
        tempFile = context.getPackageName() + SpKey.SP_BASE_TEMP;
        registerSp(tempFile);
    }

    public SpWrapper registerSp(String fileName) {
        SpWrapper spWrapper = sps.get(fileName);
        if (spWrapper != null) {
            return spWrapper;
        }

        SpWrapper wrapper = new SpWrapper(context, fileName);
        sps.put(fileName, wrapper);
        return wrapper;
    }

    public SpWrapper getSp(String fileName) {
        if (sps.get(fileName) == null) {
            return registerSp(fileName);
        }
        return sps.get(fileName);
    }

    public SpWrapper getTempSp() {
        return sps.get(tempFile);
    }

}
