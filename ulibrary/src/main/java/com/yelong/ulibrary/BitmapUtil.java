package com.yelong.ulibrary;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap工具类
 * Created by eyetech on 16/4/17.
 */
public class BitmapUtil {

    /**
     * 把二进制数组转化成位图
     *
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeData(byte[] data, int reqWidth, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 对位图进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // 真正输出位图
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 把资源文件转化成位图
     *
     * @param resources 资源文件
     * @param ResId     解码位图的ID
     * @param reqWidth  指定输出位图的宽度
     * @param reqHeight 指定输出位图的高度
     * @return
     */
    public static Bitmap decodeResources(Resources resources, int ResId,
                                         int reqWidth, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, ResId, options);
        // 对位图进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // 真正输出位图
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, ResId, options);
    }

    /**
     * 把文件转化成位图
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeFile(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSimpleSizeByMath(reqWidth, reqHeight, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 从网络中获取bitmap，文件缓存，推荐使用
     *
     * @param inputStream
     * @param cacheFilePath
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static Bitmap decodeInputStream(InputStream inputStream, String cacheFilePath, int width, int height)
            throws Exception {
        return decodeFile(streamToFile(inputStream, cacheFilePath), width, height);
    }

    /**
     * 把InputStream转化成文件,返回文件路径
     *
     * @param inStream
     * @param catchFile
     * @return
     * @throws Exception
     */
    public static String streamToFile(InputStream inStream, String catchFile) throws Exception {

        File tempFile = new File(catchFile);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        inStream.close();
        fileOutputStream.close();
        return catchFile;
    }

    /**
     * 计算压缩比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSimpleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 获取图片的原始宽高
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        // 压缩比例，假如是4就是压缩到原来大小的1/4
        int inSimpleSize = 1;
        if (imageWidth > reqWidth || imageHeight > reqHeight) {
            final int widthRatio = Math.round((float) imageWidth
                    / (float) reqWidth);
            final int heightRatio = Math.round((float) imageHeight
                    / (float) reqHeight);
            inSimpleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSimpleSize;
    }

    /**
     * 精确计算压缩比
     *
     * @param reqWidth
     * @param reqHeight
     * @param options
     * @return
     */
    public static int calculateInSimpleSizeByMath(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 旋转图片到一定的角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        if (bitmap == null) return null;
        if (angle == 0) return bitmap;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap newBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        if (!bitmap.isRecycled()) bitmap.recycle();
        if (!scaledBitmap.isRecycled()) scaledBitmap.recycle();

        return newBitmap;
    }

    /**
     * 旋转图片到指定的角度
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, final int degree) {
        if (bitmap == null) return null;
        if (degree == 0) return bitmap;

        Matrix matrix = new Matrix();
        matrix.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        float targetX, targetY;
        if (degree == 90) {
            targetX = bitmap.getHeight();
            targetY = 0;
        } else {
            targetX = bitmap.getHeight();
            targetY = bitmap.getWidth();
        }

        final float[] values = new float[9];
        matrix.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        matrix.postTranslate(targetX - x1, targetY - y1);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, matrix, paint);

        if (!bitmap.isRecycled()) bitmap.recycle();

        return newBitmap;
    }

    /**
     * 获取全屏图片
     *
     * @param filePath       文件路径
     * @param maxNumOfPixels 手机长宽像素的乘积
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int maxNumOfPixels) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = computeSampleSize(options, -1, maxNumOfPixels);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从AR

        int angle = readNativePictureDegree(filePath);

        return rotateBitmap(angle, BitmapFactory.decodeFile(filePath, options));
    }

    /**
     * 获取全屏图片
     *
     * @param res
     * @param resId
     * @param maxNumOfPixels 手机长宽像素的乘积
     * @return
     */
    public static Bitmap getSmallBitmap(Resources res, int resId, int maxNumOfPixels) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = computeSampleSize(options, -1, maxNumOfPixels);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从AR

        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), new Matrix(), true);
    }

    /**
     * 处理计算出的初始压缩比，得到一个最优压缩比
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 计算初始压缩比
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;

        }
    }

    /**
     * 获取本地图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int readNativePictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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

}
