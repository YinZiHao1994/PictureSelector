# PictureSelector
一个相册图片选择库

# 引入
compile 'com.yinzihao:PictureSelector:{latest_version}'

# 使用指南

## 直接集成

```
        PictureSelectorManager.builder()
                .maxSelectPictureNum(9)//做多可选中的图片数量限制
                .recyclerViewSpanCount(4)//供选择的图片列表的显示列数
                .start(getApplicationContext(), MainActivity.this);
```

使用 Builder 模式传入需要自定义的参数，调用 `start()` 方法跳转到框架中的图片选择 Activity。
**注意:读取相册需要 `READ_EXTERNAL_STORAGE` 权限，调用时需自行考虑 Android 6.0 权限问题。**

在 `onActivityResult()` 中获取结果。
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureSelectorManager.REQUEST_CODE_SELECT_PICTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> picturePathList = data.getStringArrayListExtra(PictureSelectorActivity.SELECTED_PICTURE_LIST_KEY);
                        ......
                        break;
                }
                break;
        }
    }
```

## 自定义数据结果展示

如果你不想使用框架中的图片选择 Activity ，可直接使用框架更内部的 `PictureLoaderCallback` 来自行获取数据显示。

```
getSupportLoaderManager().initLoader(0, args, new PictureLoaderCallback(getApplicationContext(),
                new PictureLoaderCallback.ResultCallback() {
        @Override
        public void onResult(List<PictureDirectory> pictureDirectories) {
            pictureDirectoryList = pictureDirectories;
            if (pictureDirectories != null && pictureDirectories.size() > 0) {
              ......
            }
        }
    }));
```
