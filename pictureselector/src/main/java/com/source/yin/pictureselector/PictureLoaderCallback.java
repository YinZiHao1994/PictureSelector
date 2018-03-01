package com.source.yin.pictureselector;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.source.yin.pictureselector.bean.Picture;
import com.source.yin.pictureselector.bean.PictureDirectory;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by yin on 2018/3/1.
 */

public class PictureLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {


    public static String IS_CONTAIN_GIF_KEY = "isContainGifKey";

    private Context context;
    private ResultCallback resultCallback;


    public interface ResultCallback {
        void onResult(List<PictureDirectory> photoDirectories);
    }

    public PictureLoaderCallback(Context context, ResultCallback resultCallback) {
        this.context = context;
        this.resultCallback = resultCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        boolean containGif = args.getBoolean(IS_CONTAIN_GIF_KEY);
        return new ImageLoader(context, containGif);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        List<PictureDirectory> pictureDirectories = new ArrayList<>();
        PictureDirectory allPictureDirectory = new PictureDirectory("ALL", "所有图片");

        while (data.moveToNext()) {
            int pictureId = data.getInt(data.getColumnIndexOrThrow(_ID));
            //图片所在文件夹ID
            String directoryId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            //图片所在文件夹名称
            String directoryName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
//            long addTime = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));
            long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

            if (size < 1) {
                continue;
            }

            boolean isAddToList = false;
            for (PictureDirectory pictureDirectory : pictureDirectories) {
                if (pictureDirectory.getId().equals(directoryId)) {
                    pictureDirectory.addPicture(new Picture(pictureId, path));
                    isAddToList = true;
                    break;
                }
            }
            if (!isAddToList) {
                PictureDirectory pictureDirectory = new PictureDirectory(directoryId, path, directoryName);
                pictureDirectory.addPicture(new Picture(pictureId, path));
                pictureDirectories.add(pictureDirectory);
            }
            allPictureDirectory.addPicture(new Picture(pictureId, path));
        }
        if (allPictureDirectory.getPicturePathList().size() > 0) {
            allPictureDirectory.setCoverPath(allPictureDirectory.getPicturePathList().get(0));
        }
        pictureDirectories.add(0, allPictureDirectory);
        if (resultCallback != null) {
            resultCallback.onResult(pictureDirectories);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
