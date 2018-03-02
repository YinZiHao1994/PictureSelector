package com.source.yin.pictureselector.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.source.yin.pictureselector.PictureLoaderCallback;
import com.source.yin.pictureselector.R;
import com.source.yin.pictureselector.adapter.PopupListWindowAdapter;
import com.source.yin.pictureselector.bean.Picture;
import com.source.yin.pictureselector.bean.PictureDirectory;
import com.source.yin.pictureselector.utils.ImageUtils;
import com.source.yin.yinadapter.BaseAdapter;
import com.source.yin.yinadapter.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PictureSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvDirectoryName;
    private View bottomLayout;
    private View cancelLayout;
    private Button btnSure;
    private ListPopupWindow directoryPopupWindow;
    private View directoryNameLayout;

    private List<Picture> pictureList;
    private BaseAdapter<Picture> pictureAdapter;
    private List<Picture> selectedPictureList = new ArrayList<>();
    private PictureDirectory currentDirectory;
    private List<PictureDirectory> pictureDirectoryList;
    private PopupListWindowAdapter<PictureDirectory> pictureDirectoryPopupListWindowAdapter;

    //选择图片列表的列数
    private int recyclerViewSpanCount;
    //最大选择数
    private int maxSelectPictureNum;
    //是否包含 gif 图片
    private boolean isContainGif;

    public static final String MAX_SELECT_PICTURE_NUM_KEY = "maxSelectPictureNum";
    public static final String IS_CONTAIN_GIF_KEY = "isContainGifKey";
    public static final String RECYCLER_VIEW_SPAN_COUNT_KEY = "recyclerViewSpanCountKey";

    public static final String SELECTED_PICTURE_LIST_KEY = "selectPictureListKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selector);

        recyclerView = findViewById(R.id.recycler_view);
        tvDirectoryName = findViewById(R.id.tv_directory_name);
        cancelLayout = findViewById(R.id.cancel_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        btnSure = findViewById(R.id.btn_sure);
        directoryNameLayout = findViewById(R.id.directory_name_layout);

        btnSure.setOnClickListener(this);
        directoryNameLayout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
        initSettingData();

    }

    private void initSettingData() {
        maxSelectPictureNum = getIntent().getIntExtra(MAX_SELECT_PICTURE_NUM_KEY, 9);
        isContainGif = getIntent().getBooleanExtra(IS_CONTAIN_GIF_KEY, false);
        recyclerViewSpanCount = getIntent().getIntExtra(RECYCLER_VIEW_SPAN_COUNT_KEY, 4);

        initAdapter();
        initPictureData();
    }

    private void initPopupWindow(List<PictureDirectory> pictureDirectories) {
        directoryPopupWindow = new ListPopupWindow(this);
        directoryPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        directoryPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        directoryPopupWindow.setAnchorView(bottomLayout);
        directoryPopupWindow.setModal(true);
        directoryPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        pictureDirectoryPopupListWindowAdapter = new PopupListWindowAdapter<PictureDirectory>(pictureDirectories, R.layout.directory_popup_list_item) {
            @Override
            public void onDataBind(ViewHolder viewHolder, PictureDirectory data) {
                TextView tvDirectoryName = viewHolder.getView(R.id.tv_directory_name);
                ImageView image = viewHolder.getView(R.id.image);
                Bitmap bitmap = data.getCoverBitmap();
                if (bitmap == null) {
                    bitmap = ImageUtils.decodeBitmapFromFileAutoSample(data.getCoverPath());
                }
                data.setCoverBitmap(bitmap);
                image.setImageBitmap(bitmap);
                tvDirectoryName.setText(data.getName());
            }
        };
        directoryPopupWindow.setAdapter(pictureDirectoryPopupListWindowAdapter);

        directoryPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                directoryPopupWindow.dismiss();
                changeCurrentDirectory(pictureDirectoryList.get(position));
            }
        });
    }

    private void initAdapter() {
        pictureList = new ArrayList<>();
        pictureAdapter = new BaseAdapter<Picture>(getApplicationContext(), pictureList, R.layout.picture_selector_list_item) {
            @Override
            public void onDataBind(CommonViewHolder viewHolder, Picture data, int position) {
                ImageView imageView = viewHolder.getImageView(R.id.image);
                CheckBox checkBox = viewHolder.getView(R.id.check_box);
                checkBox.setOnCheckedChangeListener(new ImageCheckBoxListener(data, position));

                checkBox.setChecked(data.isChecked());
                Bitmap bitmap = data.getBitmap();
                if (bitmap == null) {
                    bitmap = ImageUtils.decodeBitmapFromFileAutoSample(data.getPath());
                    data.setBitmap(bitmap);
                }
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onItemClick(CommonViewHolder commonViewHolder, View view, Picture data, int position) {

            }

            @Override
            public boolean onItemLongClick(CommonViewHolder commonViewHolder, View view, Picture data, int position) {
                return false;
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), recyclerViewSpanCount));
        recyclerView.setAdapter(pictureAdapter);
    }

    private void initPictureData() {
        Bundle args = new Bundle();
        args.putBoolean(PictureLoaderCallback.IS_CONTAIN_GIF_KEY, isContainGif);
        getSupportLoaderManager().initLoader(0, args, new PictureLoaderCallback(getApplicationContext(),
                new PictureLoaderCallback.ResultCallback() {
                    @Override
                    public void onResult(List<PictureDirectory> pictureDirectories) {
                        pictureDirectoryList = pictureDirectories;
                        if (pictureDirectories != null && pictureDirectories.size() > 0) {
                            changeCurrentDirectory(pictureDirectories.get(0));
                            initPopupWindow(pictureDirectories);
                        }
                    }
                }));
    }

    private void changeCurrentDirectory(PictureDirectory pictureDirectory) {
        currentDirectory = pictureDirectory;
        List<Picture> pictures = currentDirectory.getPictures();
        tvDirectoryName.setText(currentDirectory.getName());
        pictureList.clear();
        pictureList.addAll(pictures);
        pictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.directory_name_layout) {
            if (directoryPopupWindow.isShowing()) {
                directoryPopupWindow.dismiss();
            } else {
                adjustPopWindowHeight();
                directoryPopupWindow.show();
            }
        } else if (id == R.id.cancel_layout) {
            finish();
        } else if (id == R.id.btn_sure) {
//            Log.d("yzh", "selectedPictureList = " + selectedPictureList.toString());
//            for (Picture picture : selectedPictureList) {
//                Log.d("yzh", "picture = " + picture);
//            }

            Intent intent = new Intent();
            ArrayList<String> selectedPicturePathList = null;
            if (selectedPictureList != null) {
                selectedPicturePathList = new ArrayList<>();
                for (Picture picture : selectedPictureList) {
                    selectedPicturePathList.add(picture.getPath());
                }
            }
            intent.putStringArrayListExtra(SELECTED_PICTURE_LIST_KEY, selectedPicturePathList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void adjustPopWindowHeight() {
        if (pictureDirectoryPopupListWindowAdapter == null) {
            return;
        }
        int showDirectoryCount = pictureDirectoryPopupListWindowAdapter.getCount();
        showDirectoryCount = showDirectoryCount < 4 ? showDirectoryCount : 4;
        if (directoryPopupWindow != null) {
            directoryPopupWindow.setHeight(showDirectoryCount * getResources().getDimensionPixelOffset(R.dimen.directory_popup_window_item_height));
        }
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
            if (isChecked && selectedPictureList.size() >= maxSelectPictureNum) {
                Toast.makeText(getApplicationContext(), "最多只能选择" + maxSelectPictureNum + "张图片", Toast.LENGTH_SHORT).show();
                //此时已经点击到了超出最大数目的选项了，手动把它再设置为未选中
                buttonView.setChecked(false);
                return;
            }
            data.setChecked(isChecked);
            if (isChecked) {
                if (!selectedPictureList.contains(data)) {
                    selectedPictureList.add(data);
                }
            } else {
                selectedPictureList.remove(data);
            }
            selectedPictureListChange(selectedPictureList.size());
//            Log.d("yzh", "selectedPictureList.size() = " + selectedPictureList.size());
        }
    }

    private void selectedPictureListChange(int currentSize) {
        if (currentSize > 0) {
            btnSure.setEnabled(true);
            btnSure.setText(getString(R.string.btn_selected_size, currentSize, maxSelectPictureNum));
        } else {
            btnSure.setEnabled(false);
            btnSure.setText(getString(R.string.btn_text));
        }
    }
}
