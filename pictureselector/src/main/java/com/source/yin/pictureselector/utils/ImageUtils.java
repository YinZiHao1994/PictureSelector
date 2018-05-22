package com.source.yin.pictureselector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by yin on 2018/3/1.
 */

public class ImageUtils {


    public static void compressImageFileByWidth(String imageFilePath, int targetWidth, BitmapCallback bitmapCallback) {
        File file = new File(imageFilePath);
        compressImageFileByWidth(file, targetWidth, bitmapCallback);
    }

    /**
     * 根据目标图片的宽压缩图片文件
     *
     * @param imageFile
     * @param targetWidth
     * @param bitmapCallback
     */
    public static void compressImageFileByWidth(File imageFile, int targetWidth, BitmapCallback bitmapCallback) {
        if (imageFile == null) {
            if (bitmapCallback != null) {
                bitmapCallback.onSuccess(null);
            }
            return;
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
        decodeSampledBitmapFromFile(imageFile.getPath(), inSampleSize, bitmapCallback);
//        Log.d("yzh", "before compress image outWidth  = " + outWidth + "\ninSampleSize = " + inSampleSize);
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
     * @param inSampleSize   缩小的倍数，例如 2 ，则宽高都压缩为原来的 1/2
     * @param bitmapCallback
     * @return
     */
    public static void decodeSampledBitmapFromFile(String filePath, int inSampleSize, BitmapCallback bitmapCallback) {
//         设置inJustDecodeBounds = true ,表示获取图像信息，但是不将图像的像素加入内存
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 压缩比例
        options.inSampleSize = inSampleSize;
        //将options.inPreferredConfig改成Bitmap.Config.RGB_565，
        // 是默认情况Bitmap.Config.ARGB_8888占用内存的一半
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        DecodeImageParam decodeImageParam = new DecodeImageParam(filePath, options);

        DecodeImageAsyncTask decodeImageAsyncTask = new DecodeImageAsyncTask(bitmapCallback);
        decodeImageAsyncTask.execute(decodeImageParam);
    }


    private static class DecodeImageParam {
        private String filePath;
        private BitmapFactory.Options options;

        DecodeImageParam(String filePath, BitmapFactory.Options options) {
            this.filePath = filePath;
            this.options = options;
        }

        public String getFilePath() {
            return filePath;
        }

        public BitmapFactory.Options getOptions() {
            return options;
        }
    }

    private static class DecodeImageAsyncTask extends AsyncTask<DecodeImageParam, Integer, Bitmap> {

        private BitmapCallback bitmapCallback;

        DecodeImageAsyncTask(BitmapCallback bitmapCallback) {
            this.bitmapCallback = bitmapCallback;
        }

        @Override
        protected Bitmap doInBackground(DecodeImageParam... decodeImageParams) {
            DecodeImageParam decodeImageParam = decodeImageParams[0];
            if (decodeImageParam == null) {
                return null;
            }
            BitmapFactory.Options options = decodeImageParam.getOptions();
            String filePath = decodeImageParam.getFilePath();
            return BitmapFactory.decodeFile(filePath, options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmapCallback != null) {
                bitmapCallback.onSuccess(bitmap);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            if (bitmapCallback != null) {
                bitmapCallback.onSuccess(bitmap);
            }
        }
    }

    public interface BitmapCallback {
        void onSuccess(Bitmap bitmap);

        void onFail(String text);
    }

    /**
     * 自动压缩图片为缩略图
     *
     * @param filePath
     * @param bitmapCallback
     * @return
     */
    public static void decodeBitmapFromFileForPreview(String filePath, BitmapCallback bitmapCallback) {
        File imageFile = new File(filePath);
        long mb = imageFile.length() / 1024 / 1024;
        int sampleSize = 4;
        if (mb > 0) {
            sampleSize = (int) (sampleSize * mb);
        }
        decodeSampledBitmapFromFile(filePath, sampleSize, bitmapCallback);
    }

    /**
     * 避免过大图片内存溢出
     *
     * @param filePath
     * @param bitmapCallback
     * @return
     */
    public static void decodeBitmapFromFileAutoSimple(String filePath, BitmapCallback bitmapCallback) {
        File imageFile = new File(filePath);
        long mb = imageFile.length() / 1024 / 1024;
        int sampleSize = 1;
        if (mb > 1) {
            sampleSize = (int) (sampleSize * mb);
        }
        decodeSampledBitmapFromFile(filePath, sampleSize, bitmapCallback);
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
