package com.source.yin.pictureselector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.source.yin.pictureselector.ui.activity.PictureSelectorActivity;

/**
 * Created by yin on 2018/3/2.
 */

public class PictureSelectorManager {

    public static final int REQUEST_CODE_SELECT_PICTURE = 1011;
    public static final String SELECTED_PICTURE_LIST_KEY = PictureSelectorActivity.SELECTED_PICTURE_LIST_KEY;

    public static PictureSelectorManagerBuilder builder() {
        return new PictureSelectorManagerBuilder();
    }

    public static class PictureSelectorManagerBuilder {

        private int maxSelectPictureNum = 9;
        private boolean isContainGif = false;
        private int recyclerViewSpanCount = 4;
        private int previewImageWidth = 200;
        private int requestCode = REQUEST_CODE_SELECT_PICTURE;


        public PictureSelectorManagerBuilder previewImageWidth(int previewImageWidth) {
            if (previewImageWidth < 0) {
                previewImageWidth = 1;
            }
            this.previewImageWidth = previewImageWidth;
            return this;
        }

        public PictureSelectorManagerBuilder maxSelectPictureNum(int maxSelectPictureNum) {
            if (maxSelectPictureNum < 0) {
                throw new RuntimeException("maxSelectPictureNum mast bigger than 0");
            }
            this.maxSelectPictureNum = maxSelectPictureNum;
            return this;
        }

        public PictureSelectorManagerBuilder isContainGif(boolean isContainGif) {
            this.isContainGif = isContainGif;
            return this;
        }

        public PictureSelectorManagerBuilder recyclerViewSpanCount(int recyclerViewSpanCount) {
            this.recyclerViewSpanCount = recyclerViewSpanCount;
            return this;
        }

        public PictureSelectorManagerBuilder requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public void start(Context packageContext, Activity activityForResult) {
            if (isHaveReadExternalPermission(packageContext)) {
                Intent intent = getIntent(packageContext);
                activityForResult.startActivityForResult(intent, requestCode);
            } else {
                showNoReadExternalPermissionMessage(packageContext);
            }
        }


        public void start(Context packageContext, Fragment fragmentForResult) {
            if (isHaveReadExternalPermission(packageContext)) {
                Intent intent = getIntent(packageContext);
                fragmentForResult.startActivityForResult(intent, requestCode);
            } else {
                showNoReadExternalPermissionMessage(packageContext);
            }
        }

        public void start(Context packageContext, android.app.Fragment fragmentForResult) {
            if (isHaveReadExternalPermission(packageContext)) {
                Intent intent = getIntent(packageContext);
                fragmentForResult.startActivityForResult(intent, requestCode);
            } else {
                showNoReadExternalPermissionMessage(packageContext);
            }
        }

        private void showNoReadExternalPermissionMessage(Context context) {
            showToast(context, "没有阅读相册权限");
        }

        private void showToast(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        private boolean isHaveReadExternalPermission(Context context) {
            return isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        //如果返回true表示已经授权了
        private boolean isPermissionGranted(Context context, String permission) {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }


        private Intent getIntent(Context context) {
            Intent intent = new Intent(context, PictureSelectorActivity.class);
            intent.putExtra(PictureSelectorActivity.IS_CONTAIN_GIF_KEY, isContainGif);
            intent.putExtra(PictureSelectorActivity.MAX_SELECT_PICTURE_NUM_KEY, maxSelectPictureNum);
            intent.putExtra(PictureSelectorActivity.RECYCLER_VIEW_SPAN_COUNT_KEY, recyclerViewSpanCount);
            intent.putExtra(PictureSelectorActivity.PREVIEW_IMAGE_WIDTH_KEY, previewImageWidth);
            return intent;
        }
    }


}
