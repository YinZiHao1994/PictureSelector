package com.source.yin.pictureselectorsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.source.yin.pictureselector.PictureSelectorManager;
import com.source.yin.pictureselector.ui.activity.PictureSelectorActivity;
import com.source.yin.pictureselector.utils.ImageUtils;
import com.source.yin.yinadapter.BaseAdapter;
import com.source.yin.yinadapter.CommonViewHolder;
import com.source.yin.yinandroidutils.PermissionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnImageSelect;
    private RecyclerView recyclerView;

    private BaseAdapter<String> adapter;
    private List<String> imagePathList;
    private int maxImageCount = 9;

    private PermissionManager permissionManager;

    public static final int MY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImageSelect = findViewById(R.id.btn_image_select);
        recyclerView = findViewById(R.id.recycler_view);

        btnImageSelect.setOnClickListener(this);


        initAdapter();
    }

    private void initAdapter() {
        imagePathList = new ArrayList<>();
        adapter = new BaseAdapter<String>(getApplicationContext(), imagePathList, R.layout.picture_list_item) {
            @Override
            public void onDataBind(CommonViewHolder viewHolder, String data, int position) {
                final ImageView imageView = viewHolder.getImageView(R.id.image);
//                imageView.setImageBitmap(ImageUtils.decodeBitmapFromFileForPreview(data));
                ImageUtils.compressImageFileByWidth(data, 200, new ImageUtils.BitmapCallback() {
                    @Override
                    public void onSuccess(final Bitmap bitmap) {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onFail(String text) {

                    }
                });
            }

            @Override
            public void onItemClick(CommonViewHolder commonViewHolder, View view, String data, int position) {

            }

            @Override
            public boolean onItemLongClick(CommonViewHolder commonViewHolder, View view, String data, int position) {
                return false;
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image_select:
                selectPicture(maxImageCount - imagePathList.size());
                break;
        }
    }

    public void selectPicture(final int selectCount) {
        permissionManager = new PermissionManager(this, new PermissionManager.PermissionResultCallBack() {
            @Override
            public void onPermissionGranted(String permission) {
                PictureSelectorManager.builder()
                        .maxSelectPictureNum(selectCount)//做多可选中的图片数量限制
                        .recyclerViewSpanCount(4)//供选择的图片列表的显示列数
                        .previewImageWidth(150)
                        .requestCode(MY_REQUEST_CODE)
                        .start(getApplicationContext(), MainActivity.this);
            }

            @Override
            public void onPermissionDenied(String permission) {

            }

            @Override
            public void shouldShowRequestPermissionRationale(String permission) {

            }
        });
        permissionManager.readExternalStorage();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> picturePathList = data.getStringArrayListExtra(PictureSelectorActivity.SELECTED_PICTURE_LIST_KEY);
                        Log.d("yzh", "onActivityResult picturePathList = " + picturePathList);
                        imagePathList.addAll(picturePathList);
                        adapter.notifyDataSetChanged();
                        break;
                }

                break;

        }
    }
}
