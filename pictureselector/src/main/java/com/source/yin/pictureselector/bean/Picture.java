package com.source.yin.pictureselector.bean;

import android.graphics.Bitmap;
import android.widget.Checkable;

/**
 * 图片类
 */
public class Picture implements Checkable {

    private int id;
    private String path;

    private Bitmap bitmap;

    private boolean isChecked;

    public Picture(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }
}
