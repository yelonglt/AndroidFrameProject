package com.yelong.androidframeproject.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap工具类
 * Created by eyetech on 16/4/17.
 */
public class BitmapUtil {
    public BitmapUtil() {
        // TODO Auto-generated constructor stub
    }

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
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }
}
