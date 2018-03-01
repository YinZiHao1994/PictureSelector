package com.source.yin.pictureselector.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.source.yin.pictureselector.PictureLoaderCallback;
import com.source.yin.pictureselector.R;
import com.source.yin.pictureselector.bean.Picture;
import com.source.yin.pictureselector.bean.PictureDirectory;
import com.source.yin.pictureselector.utils.ImageUtils;
import com.source.yin.yinadapter.BaseAdapter;
import com.source.yin.yinadapter.CommonViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureSelectorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Picture> pictureList;
    private BaseAdapter<Picture> pictureAdapter;

    private List<Picture> selectedPictureList = new ArrayList<>();

    private int spanCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selector);

        recyclerView = findViewById(R.id.recycler_view);
        initAdapter();
        initData();
    }

    private void initAdapter() {
        pictureList = new ArrayList<>();
        pictureAdapter = new BaseAdapter<Picture>(getApplicationContext(), pictureList, R.layout.picture_selector_list_item) {
            @Override
            public void onDataBind(CommonViewHolder viewHolder, Picture data, int position) {
                ImageView imageView = viewHolder.getImageView(R.id.image);
                CheckBox checkBox = viewHolder.getView(R.id.check_box);
                Bitmap bitmap;
                if (data.getBitmap() != null) {
                    bitmap = data.getBitmap();
                } else {
                    File imageFile = new File(data.getPath());
                    long mb = imageFile.length() / 1024 / 1024;
                    int sampleSize = 4;
                    if (mb > 0) {
                        sampleSize = (int) (sampleSize * mb);
                    }
                    bitmap = ImageUtils.decodeSampledBitmapFromFile(data.getPath(), sampleSize);
                    data.setBitmap(bitmap);
                }
                imageView.setImageBitmap(bitmap);

                checkBox.setOnCheckedChangeListener(new ImageCheckBoxListener(data, position));

            }

            @Override
            public void onItemClick(CommonViewHolder commonViewHolder, View view, Picture data, int position) {

            }

            @Override
            public boolean onItemLongClick(CommonViewHolder commonViewHolder, View view, Picture data, int position) {
                return false;
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), spanCount));
        recyclerView.setAdapter(pictureAdapter);
    }

    private void initData() {
        Bundle args = new Bundle();
        getSupportLoaderManager().initLoader(0, args, new PictureLoaderCallback(getApplicationContext(),
                new PictureLoaderCallback.ResultCallback() {
                    @Override
                    public void onResult(List<PictureDirectory> pictureDirectories) {
                        if (pictureDirectories != null && pictureDirectories.size() > 0) {
                            Log.d("yzh", "pictureDirectories = " + pictureDirectories.toString());
                            for (PictureDirectory pictureDirectory : pictureDirectories) {
                                Log.d("yzh", "pictureDirectory = " + pictureDirectory);
                            }
                            PictureDirectory pictureDirectory = pictureDirectories.get(0);
                            List<Picture> pictures = pictureDirectory.getPictures();
                            pictureList.clear();
                            pictureList.addAll(pictures);
                            pictureAdapter.notifyDataSetChanged();
                        }
                    }
                }));
    }

    private class ImageCheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        private Picture data;
        private int position;

        public ImageCheckBoxListener(Picture data, int position) {
            this.data = data;
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                selectedPictureList.add(data);
            } else {
                selectedPictureList.remove(data);
            }

            Log.d("yzh", "selectedPictureList.size() = " + selectedPictureList.size());
        }
    }
}
