package com.source.yin.pictureselector.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹
 */
public class PictureDirectory {

    private String id;
    private String coverPath;
    private String name;
    private List<Picture> pictures = new ArrayList<>();

    public PictureDirectory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PictureDirectory(String id, String name, String coverPath) {
        this.id = id;
        this.coverPath = coverPath;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Picture> getPictures() {
        return pictures;
    }


    public List<String> getPicturePathList() {
        List<String> pathList = new ArrayList<>(pictures.size());
        for (Picture picture : pictures) {
            pathList.add(picture.getPath());
        }
        return pathList;
    }

    public void addPicture(int id, String path) {
        if (new File(path).exists()) {
            pictures.add(new Picture(id, path));
        }
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    @Override
    public String toString() {
        return "PictureDirectory{" +
                "id='" + id + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", name='" + name + '\'' +
                ", pictures=" + pictures +
                '}';
    }
}
