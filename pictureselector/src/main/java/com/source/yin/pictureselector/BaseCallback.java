package com.source.yin.pictureselector;

public interface BaseCallback<T> {

    void onSuccess(T data);

    void onFail(String text);
}
