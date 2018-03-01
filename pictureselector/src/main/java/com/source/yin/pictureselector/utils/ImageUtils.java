package com.source.yin.pictureselector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
