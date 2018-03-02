package com.source.yin.pictureselector.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by yin on 2018/3/1.
 */

public abstract class PopupListWindowAdapter<T> extends BaseAdapter {

    private List<T> dataList;
    @LayoutRes
    private int layoutRes;

    public PopupListWindowAdapter(List<T> dataList, int layoutRes) {
        this.dataList = dataList;
        this.layoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(layoutRes, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(dataList.get(position));

        return convertView;
    }


    public abstract void onDataBind(ViewHolder viewHolder, T data);


    public class ViewHolder {
        private View rootView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public void bindData(T data) {
            onDataBind(this, data);
        }

        public <V extends View> V getView(@IdRes int viewId) {
            View childView = rootView.findViewById(viewId);
            if (childView == null) {
                throw new IllegalArgumentException("This viewId is not match a childView in the itemView");
            }
            return (V) childView;
        }
    }
}
