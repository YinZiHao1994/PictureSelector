package com.source.yin.pictureselector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by yin on 2018/3/1.
 */

public class ImageUtils {


    public static Bitmap compressImageFileByWidth(String imageFilePath, int targetWidth) {
        File file = new File(imageFilePath);
        return compressImageFileByWidth(file, targetWidth);
    }

    /**
     * 根据目标图片的宽压缩图片文件
     *
     * @param imageFile
     * @param targetWidth
     */
    public static Bitmap compressImageFileByWidth(File imageFile, int targetWidth) {
        Bitmap bitmap = null;
        if (imageFile == null) {
            return bitmap;
        }
        if (targetWidth <= 0) {
            targetWidth = 1;
        }
        BitmapFactory.Options optionsInJustDecodeBounds = getOptionsInJustDecodeBounds(imageFile.getPath());
        int outWidth = optionsInJustDecodeBounds.outWidth;
        int inSampleSize = 1;
        if (outWidth > targetWidth) {
            inSampleSize = outWidth / targetWidth;
        }
        bitmap = decodeSampledBitmapFromFile(imageFile.getPath(), inSampleSize);
//        Log.d("yzh", "before compress image outWidth  = " + outWidth + "\ninSampleSize = " + inSampleSize);
        return bitmap;
    }

    /**
     * 在不解析Bitmap对象到内存中的情况下，只获取其原始宽高等属性
     *
     * @return
     */
    public static BitmapFactory.Options getOptionsInJustDecodeBounds(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }

    /**
     * 解析并压缩图片
     *
     * @param inSampleSize 缩小的倍数，例如 2 ，则宽高都压缩为原来的 1/2
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int inSampleSize) {

        // 设置inJustDecodeBounds = true ,表示获取图像信息，但是不将图像的像素加入内存
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 压缩比例
        options.inSampleSize = inSampleSize;
        //将options.inPreferredConfig改成Bitmap.Config.RGB_565，
        // 是默认情况Bitmap.Config.ARGB_8888占用内存的一半
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 自动压缩图片为缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap decodeBitmapFromFileForPreview(String filePath) {
        File imageFile = new File(filePath);
        long mb = imageFile.length() / 1024 / 1024;
        int sampleSize = 4;
        if (mb > 0) {
            sampleSize = (int) (sampleSize * mb);
        }
        return decodeSampledBitmapFromFile(filePath, sampleSize);
    }

    /**
     * 避免过大图片内存溢出
     *
     * @param filePath
     * @return
     */
    public static Bitmap decodeBitmapFromFileAutoSimple(String filePath) {
        File imageFile = new File(filePath);
        long mb = imageFile.length() / 1024 / 1024;
        int sampleSize = 1;
        if (mb > 1) {
            sampleSize = (int) (sampleSize * mb);
        }
        return decodeSampledBitmapFromFile(filePath, sampleSize);
    }

    /**
     * 图片转字节
     *
     * @param bmp
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
