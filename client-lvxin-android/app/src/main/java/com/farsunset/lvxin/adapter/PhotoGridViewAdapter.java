/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.farsunset.lvxin.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.farsunset.lvxin.adapter.viewholder.ImageViewHolder;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.BitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGridViewAdapter extends RecyclerView.Adapter<ImageViewHolder> implements View.OnLongClickListener, View.OnClickListener {
    private OnItemClickedListener onItemClickedListener;

    private List<File> list = new ArrayList<>();
    private ArrayMap<String, SNSImage> imageFileMap = new ArrayMap<>();
    private List<SNSImage> imageList = new ArrayList<>();
    private int itemWith;
    private int itemHeight;
    private int itemPadding;
    private String mSelectedKey;
    private File mSelectedFile;

    public PhotoGridViewAdapter() {
        super();
        itemPadding = AppTools.dip2px(5);
        itemWith = Resources.getSystem().getDisplayMetrics().widthPixels / 5;
        itemHeight = itemWith;
    }


    public int getListSize() {
        return list.size();
    }


    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<SNSImage> getImageList() {
        imageList.clear();
        imageList.addAll(imageFileMap.values());
        return imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        holder.imageView.setLayoutParams(new RecyclerView.LayoutParams(itemWith, itemHeight));
        holder.imageView.setTag(null);
        if (position == list.size()) {
            holder.imageView.setOnClickListener(this);
            holder.imageView.setPadding(0, 0, 0, 0);
            holder.imageView.setImageResource(R.drawable.icon_add_photo_selector);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            holder.imageView.setOnLongClickListener(this);
            final File file = list.get(position);
            holder.imageView.setTag(file);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setOnClickListener(null);
            holder.imageView.setImageResource(R.color.moment_image_color);
            holder.imageView.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);

            CloudImageLoaderFactory.get().loadAndApply(holder.imageView, file, R.color.moment_image_color);
            CloudImageLoaderFactory.get().downloadOnly(file, new CloudImageLoadListener() {
                @Override
                public void onImageLoadFailure(Object key) {
                }

                @Override
                public void onImageLoadSucceed(Object key, Bitmap resource) {
                    SNSImage snsImage = BitmapUtils.compressSNSImage(resource);
                    imageFileMap.put(snsImage.image, snsImage);
                    holder.imageView.setTag(snsImage.image);
                    holder.imageView.setTag(R.id.file, file);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    public void add(File image) {
        int index = list.size();
        list.add(image);
        notifyItemInserted(index);
    }

    @Override
    public boolean onLongClick(View v) {
        mSelectedKey = (String) v.getTag();
        mSelectedFile = (File) v.getTag(R.id.file);
        return false;
    }

    @Override
    public void onClick(View v) {
        onItemClickedListener.onItemClicked(null, v);
    }

    public void addAll(List<File> dataList) {
        int startIndex = list.size();
        list.addAll(dataList);
        notifyItemRangeInserted(startIndex, dataList.size());
    }

    public void removeSelected() {
        int index = list.indexOf(mSelectedFile);
        if (index >= 0) {
            list.remove(mSelectedFile);
            notifyItemRemoved(index);
        }
        imageFileMap.remove(mSelectedKey);
    }
}
