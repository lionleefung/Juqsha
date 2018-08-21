package com.lightcone.feedback.misc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chenkehui on 2017/2/17.
 */

public class BitmapHelper {

    public static String saveTempBitmapFile(Bitmap bitmap) {
        File cacheDir = new File(Environment.getExternalStorageDirectory(), "feedback_cache");
        cacheDir.mkdirs();
        File f = new File(cacheDir, "tempimage_" + System.currentTimeMillis() + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 按照最大宽度 700采样图片
     *
     * @param path
     * @return
     */
    public static Bitmap sampleDecodeFile(String path) {
        /**
         *  BitmapFactory.decodeFile(path, options)有两个过程
         *  输入和输出
         *  容易OOM的是输入过程
         *  唯一不让OOM发生的方式是指定 输入 sampleSize，
         *  只指定 输出 宽高不能节省内存，因为输入过程还是把整张图读进内存了
         *  设定 inJustDecodeBounds 为 true 可以只采样边界，获取宽高，返回位图为空
         *  获得的宽高用于等比例输出
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        /**
         *  一般根据输出大小设置 sampleSize，
         *  设置outWidth是没用的，输出大小取决于 inSampleSize
         */
        int maxWidth = 700;
        if (maxWidth < options.outWidth) {
            options.inSampleSize = Math.round(1.f * options.outWidth / maxWidth);
        }

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

}
