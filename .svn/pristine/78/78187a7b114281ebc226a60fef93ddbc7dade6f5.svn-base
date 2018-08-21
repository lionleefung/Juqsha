package lightcone.com.pack.util.res;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lightcone.utils.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.BitmapFactory.Options;

/**
 * Created by panjianye on 2017/6/20.
 */

public class BitmapUtil {

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    // 等比缩放图片
    public static Bitmap createZoomImg(Bitmap src, int newWidth, int newHeight) {
        if (src == null) {
            return null;
        }
        // 获得图片的宽高
        int width = src.getWidth();
        int height = src.getHeight();
        float scale;
        float nw = width * 1f / newWidth;
        float nh = height * 1f / newHeight;
        if (nw > nh) scale = 1 / nw;
        else scale = 1 / nh;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 非等比缩放图片  如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        if (src == null) {
            return null;
        }
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
//        Bitmap dst = ThumbnailUtils.extractThumbnail(src, dstWidth, dstHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    public static int getSrcBitmapWidth(String pathName) {
        Options opt = new Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(pathName, opt);
        if (bm != null) {
            bm.recycle(); // 释放Bitmap的native像素数组
        }
        // 获取到这个图片的原始宽度和高度
        return opt.outWidth;
    }

    public static int getSrcBitmapHeight(String pathName) {
        Options opt = new Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(pathName, opt);
        if (bm != null) {
            bm.recycle(); // 释放Bitmap的native像素数组
        }
        // 获取到这个图片的原始宽度和高度
        return opt.outHeight;
    }

    // 从Resources中加载图片（等比或非等比缩放）
    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长宽
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
//        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
        return createZoomImg(src, reqWidth, reqHeight);
    }

    // 从sd卡上加载压缩图片
    public static Bitmap decodeSampledBitmapFromFd(String pathName) {
        Options newOpts = new Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        return BitmapFactory.decodeFile(pathName, newOpts);
    }

    // 从sd卡上加载图片（等比或非等比缩放）
    public static Bitmap decodeBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
//        return createScaleBitmap(src, reqWidth, reqHeight);
        return createZoomImg(src, reqWidth, reqHeight);
    }

    // 从Assets卡上加载图片（等比或非等比缩放）
    public static Bitmap decodeBitmapFromAssets(String path, int reqWidth, int reqHeight) {
        InputStream fileStream = AssetUtil.instance.getFileStream(path);
        Options options = new Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(fileStream,null,options);
        byte[] data = new byte[0];//将InputStream转为byte数组，可以多次读取
        try {
            data = inputStream2ByteArr(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Bitmap src = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
//        Bitmap src = BitmapFactory.decodeStream(fileStream,null,options);
        Bitmap src = BitmapFactory.decodeByteArray(data, 0, data.length, options);
//        return createScaleBitmap(src, reqWidth, reqHeight);
        return createZoomImg(src, reqWidth, reqHeight);
    }

    private static byte[] inputStream2ByteArr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

    //三星手机图片旋转问题:通过图片路径得到图片的旋转角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //三星手机图片旋转问题:根据图片的选装角度将图片旋转回去
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    //三星手机图片旋转问题:根据图片的选装角度将图片旋转回去
    public static Bitmap rotateBitmap(Bitmap bm, String path) {
        return rotateBitmapByDegree(bm, readPictureDegree(path));
    }

    /**
     * 得到View的缓存图片bitmap
     */
    public static Bitmap getViewBitmap(View v) {
        if (v == null) {
            return null;
        }
        //得到该view是否 设置了绘画缓存
        boolean willNotCache = v.willNotCacheDrawing();
        // 设置该View绘画 缓存  如果设置true 则不缓存    默认是false开启的
        v.setWillNotCacheDrawing(false);

//        int color = v.getDrawingCacheBackgroundColor();
//        v.setDrawingCacheBackgroundColor(0);
//        if (color != 0) {
//          //清除绘画缓存
//            v.destroyDrawingCache();
//        }
        //buildDrawingCache建立drawingCache的同时，会将上次的DrawingCache回收掉，在源码中buildDrawingCache会调用destroyDrawingCache方法对之前的DrawingCache回收
        v.buildDrawingCache();

        if (v.getDrawingCache() == null) {
            //防止未加载完成为空
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.buildDrawingCache();
//            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());

        //清除缓存
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
//        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    /**
     * 得到View的缓存图片路径Path
     */
    public static String getViewBitmapPath(View v) {
        if (v == null) {
            return null;
        }
        //得到该view是否 设置了绘画缓存
        boolean willNotCache = v.willNotCacheDrawing();
        // 设置该View绘画 缓存  如果设置true 则不缓存    默认是false开启的
        v.setWillNotCacheDrawing(false);
        v.buildDrawingCache();

        if (v.getDrawingCache() == null) {
            //防止v未加载完成为空
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.buildDrawingCache();
//            return null;
        }
        String filePath = Environment.getExternalStorageDirectory() + "/ViewPath/";
        String fileName = "ViewPath.jpeg";
        BitmapUtil.writeBitmapToFile(v.getDrawingCache(), filePath, fileName);

        //清除缓存
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);

        return filePath + fileName;
    }

    // 图片添加水印处理
    public static Bitmap makeWatermarkBitmap(Bitmap src, Bitmap watermark) {
        String tag = "createBitmap";
        Log.d(tag, "create a new bitmap");
        if (src == null) {
            return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
//        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(w, h + wh, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC+watermark长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
//        cv.drawBitmap(watermark, w - ww, h - wh, null);// 在src的右下角画入水印
        Rect mSrcRect = new Rect(0, 0, ww, wh);
        Rect mDestRect = new Rect(0, h, w, h + wh);
        cv.drawBitmap(watermark, mSrcRect, mDestRect, null);// 在src的下方画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    public static void writeBitmapToFile(Bitmap image, String filePath, String fileName) {
        if (image != null) {
            File fp = new File(filePath);
            if (!fp.exists()) {
                fp.mkdirs();
            }
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fo = new FileOutputStream(file);
                Bitmap.CompressFormat format;
                switch (FileUtil.getExtensionName(fileName)) {
                    case "png":
                        format = Bitmap.CompressFormat.PNG;
                        break;
                    case "webp":
                        format = Bitmap.CompressFormat.WEBP;
                        break;
                    default:
                        format = Bitmap.CompressFormat.JPEG;
                        break;
                }
                image.compress(format, 90, fo);
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到指定目录并更新图库
     *
     * @param context  context
     * @param image    image
     * @param filePath filePath
     * @param fileName fileName
     */
    public static void saveImageToGallery(Context context, Bitmap image, String filePath, String fileName) {
        writeBitmapToFile(image, filePath, fileName);
        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath + "/" + fileName)));
    }

    /**
     * 复制图片到指定目录并更新图库
     *
     * @param context context
     * @param srcFile srcFile
     * @param dirFile dirFile
     */
    public static void saveImageToGallery(Context context, String srcFile, String dirFile) {
        copyImageByFile(srcFile, dirFile);
        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + dirFile)));
    }

    /**
     * 复制图片到指定目录
     *
     * @param srcFile srcFile
     * @param dirFile dirFile
     */
    public static void copyImageByFile(String srcFile, String dirFile) {
        try {
            FileUtil.createFile(dirFile);
            FileUtil.copyFile(srcFile, dirFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片的四角圆弧化
     *
     * @param bitmap      原图
     * @param roundPixels 弧度
     * @param half        （上/下/左/右）半部分圆角
     * @return
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels, HalfType half) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap roundCornerImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个和原始图片一样大小的位图
        Canvas canvas = new Canvas(roundCornerImage);//创建位图画布
        Paint paint = new Paint();//创建画笔

        Rect rect = new Rect(0, 0, width, height);//创建一个和原始图片一样大小的矩形
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);// 抗锯齿

        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);//画一个基于前面创建的矩形大小的圆角矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置相交模式
        canvas.drawBitmap(bitmap, null, rect, paint);//把图片画到矩形去

        switch (half) {
            case LEFT:
                return Bitmap.createBitmap(roundCornerImage, 0, 0, width - roundPixels, height);
            case RIGHT:
                return Bitmap.createBitmap(roundCornerImage, width - roundPixels, 0, width - roundPixels, height);
            case TOP: // 上半部分圆角化 “- roundPixels”实际上为了保证底部没有圆角，采用截掉一部分的方式，就是截掉和弧度一样大小的长度
                return Bitmap.createBitmap(roundCornerImage, 0, 0, width, height - roundPixels);
            case BOTTOM:
                return Bitmap.createBitmap(roundCornerImage, 0, height - roundPixels, width, height - roundPixels);
            case ALL:
                return roundCornerImage;
            default:
                return roundCornerImage;
        }
    }

    /**
     * 图片圆角规则 eg. TOP：上半部分
     */
    public enum HalfType {
        LEFT, // 左上角 + 左下角
        RIGHT, // 右上角 + 右下角
        TOP, // 左上角 + 右上角
        BOTTOM, // 左下角 + 右下角
        ALL // 四角
    }
}
