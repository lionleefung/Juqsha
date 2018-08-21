package lightcone.com.pack.util.res;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import com.lightcone.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lightcone.com.pack.MyApplication;

public class AssetUtil {

    private static final Context context = MyApplication.appContext;

    public static final String AssetUriPrefix = "file:///android_asset/";

    public static final AssetUtil instance = new AssetUtil();

    private AssetUtil() {

    }

    private AssetManager assetManager = context.getAssets();

    /**
     * @param assetDir
     * @param outDir   输出目录
     */
    public void copyAssets(String assetDir, String outDir) {
        copyAssets(assetDir, outDir, false);
    }

    /**
     * @param assetDir eg:"ad","ad/ad.png"
     * @param outDir   eg:"file://..."
     * @param replace  替换同名文件
     */
    public void copyAssets(String assetDir, String outDir, boolean replace) {
        String[] files;
        try {
            files = assetManager.list(assetDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File mWorkingPath = new File(outDir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
                Log.e("--CopyAssets--", "cannot create directory.");
            }
        }
        InputStream in = null;
        OutputStream out = null;
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                // we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        copyAssets(fileName, outDir + fileName + "/", replace);
                    } else {
                        copyAssets(assetDir + "/" + fileName, outDir + fileName + "/", replace);
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists()) {
                    if (replace) {
                        outFile.delete();
                    } else {
                        continue;
                    }
                }

                if (0 != assetDir.length()) {
                    in = assetManager.open(assetDir + "/" + fileName);
                } else {
                    in = assetManager.open(fileName);
                }
                out = new FileOutputStream(outFile);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public void copyFileToDir(String assertFile, String outDir) {
        String fileName = assertFile.substring(assertFile.lastIndexOf("/") + 1);// -1+0=0
        copyFile(assertFile, outDir + fileName);
    }

    public void copyFile(String assertFile, String outFilePath) {
        copyFile(assertFile, outFilePath, false);
    }

    public void copyFile(String assertFile, String outFilePath, boolean replace) {
        File outFile = new File(outFilePath);
        OutputStream out = null;
        InputStream in = null;
        try {
            if (outFile.exists()) {
                if (replace) {
                    outFile.delete();
                } else {
                    return;
                }
            } else {
                FileUtil.createFile(outFilePath);
            }
            out = new FileOutputStream(outFile);
            in = assetManager.open(assertFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public int getFileCount(String assetDir) {
        String[] files;
        try {
            files = assetManager.list(assetDir);
        } catch (IOException e1) {
            return 0;
        }
        return files.length;
    }

    public InputStream getFileStream(String filepath) {
        try {
            return assetManager.open(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AssetFileDescriptor getAssetFileDescriptor(String filepath) {
        try {
            return assetManager.openFd(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUriString(String file) {
        return AssetUriPrefix + file;
    }

    public String getStringFromUri(String assetUri) {
        return assetUri.replace(AssetUriPrefix, "");
    }

    /**
     * zip格式
     */
    public boolean unZip(String assetName, String outdir) {
        try {
            return FileUtil.unZip(assetManager.open(assetName), outdir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
