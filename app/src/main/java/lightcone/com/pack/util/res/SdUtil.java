package lightcone.com.pack.util.res;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import lightcone.com.pack.MyApplication;

/**
 * 注意添加权限.外部SD和内部cache均有可能被用户手动清理,内部cache更容易被清理
 */
public class SdUtil {

    private static final String FilePrefix = "GZY";

    private static final Context context = MyApplication.appContext;

    public static final SdUtil instance = new SdUtil();

    private SdUtil() {
        init();
    }

    private String appPath;

    private void init() {
        String packageName = context.getPackageName();
        String appRootDir = FilePrefix + packageName.substring(packageName.lastIndexOf(".") + 1);
        resetAppPath(appRootDir);
    }

    public boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public String getRootPath() {
        String rootPath = null;
        if (isSdcardExist()) {
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            rootPath = context.getCacheDir().getAbsolutePath() + File.separator;
            Log.d(SdUtil.class.getName(),
                    "maybe need apply for SDcard permission (MOUNT_UNMOUNT_FILESYSTEMS/WRITE_EXTERNAL_STORAGE) or sd is not exist");
        }
        return rootPath;
    }

    public String resetAppPath(String appRootDir) {
        if (!StringUtil.isEmpty(appPath)) {
            new File(appPath).delete();
        }
        this.appPath = getRootPath() + appRootDir + File.separator;
        File fileDir = new File(appPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return appPath;
    }

    public String getAppPath() {
        File fileDir = new File(appPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return appPath;
    }

    /**
     * 返回SD子目录
     *
     * @param subDir eg:"first","1sub/2sub"
     */
    public String getAppSubPath(String subDir) {
        String path = getAppPath() + subDir + File.separator;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return path;
    }

    public String getAppSubPath(int subDirResStringId) {
        return getAppSubPath(context.getString(subDirResStringId));
    }

    public String getCacheDir(String subDir) {
        return context.getCacheDir().getPath() + File.separator + subDir + File.separator;
    }

    public String getCacheDir() {
        return context.getCacheDir().getPath() + File.separator;
    }

    /***********************************************************************************************************/

    /**
     * 根据fileNameFormat获取文件路径
     */
    public String getFileNameFormats(String preDir, String fileNameFormat, Object... formatParams) {
        String _format = preDir + fileNameFormat;
        return String.format(_format, formatParams);
    }

    public static String getExternDir(String dir) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        if (dir != null) {
            path += dir;
        }
        return path;
    }

    public static String getSubOfRootDir(String target) {
        String rootDir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootDir = getExternDir(rootDir) + "/" + target;
        } else {
            rootDir = context.getCacheDir().getPath() + "/" + target;
        }
        return rootDir;
    }
}
