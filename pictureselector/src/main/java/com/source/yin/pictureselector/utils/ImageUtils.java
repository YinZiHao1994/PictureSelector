package com.source.yin.pictureselector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by yin on 2018/3/1.
 */

public class ImageUtils {

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
    public static Bitmap decodeBitmapFromFileAutoSimaple(String filePath) {
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
