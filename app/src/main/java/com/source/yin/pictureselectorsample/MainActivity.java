package com.source.yin.pictureselectorsample;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnImageSelect;
    private RecyclerView recyclerView;

    private BaseAdapter<String> adapter;
    private List<String> imagePathList;
    private int maxImageCount = 9;

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
                ImageView imageView = viewHolder.getImageView(R.id.image);
                imageView.setImageBitmap(ImageUtils.decodeBitmapFromFileForPreview(data));
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

    public void selectPicture(int selectCount) {
        PictureSelectorManager.builder()
                .maxSelectPictureNum(selectCount)
                .recyclerViewSpanCount(3)
                .start(getApplicationContext(), this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureSelectorManager.REQUEST_CODE_SELECT_PICTURE:
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
