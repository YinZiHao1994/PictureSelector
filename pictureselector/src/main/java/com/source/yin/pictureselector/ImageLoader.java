package com.source.yin.pictureselector;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by yin on 2018/3/1.
 */

public class ImageLoader extends CursorLoader {


    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE
    };

    public ImageLoader(Context context, boolean containGIF) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(
                MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (containGIF ? ("or " + MIME_TYPE + "=?") : ""));
        String[] selectionArgs;
        if (containGIF) {
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
        } else {
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg"};
        }
        setSelectionArgs(selectionArgs);
    }

    public ImageLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
