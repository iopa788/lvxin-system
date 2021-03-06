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
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.farsunset.lvxin.adapter.viewholder.AlbumItemViewHolder;
import com.farsunset.lvxin.listener.OnItemCheckedListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageChooseViewAdapter extends RecyclerView.Adapter<AlbumItemViewHolder> implements OnCheckedChangeListener, View.OnClickListener {
    private List<Uri> list = new ArrayList<>();
    private ArrayList<Uri> selectedList = new ArrayList<Uri>();
    private int max = 1;
    private int itemHeight;
    private OnItemCheckedListener onItemCheckedListener;
    private OnItemClickedListener onItemClickedListener;

    public ImageChooseViewAdapter(int max) {
        super();
        this.max = max;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        itemHeight = ((width - AppTools.dip2px(15)) / 3);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    @Override
    public AlbumItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (max > 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_choose, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_choose_single, parent, false);
        }
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, itemHeight));
        return new AlbumItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumItemViewHolder holder, int position) {
        Uri item = list.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        if (holder.checkbox != null) {
            holder.checkbox.setOnCheckedChangeListener(null);
            holder.checkbox.setChecked(selectedList.contains(item));
            holder.checkbox.setTag(item);
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setOnCheckedChangeListener(this);
            holder.imageView.setPopuShowAble();
        }
        holder.imageView.load(item, R.color.theme_window_color);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
        if (isChecked) {
            selectedList.add((Uri) compoundbutton.getTag());
        }
        if (!isChecked || selectedList.size() > max) {
            compoundbutton.setOnCheckedChangeListener(null);
            compoundbutton.setChecked(false);
            selectedList.remove(compoundbutton.getTag());
            compoundbutton.setOnCheckedChangeListener(this);
        }

        onItemCheckedListener.onItemChecked(compoundbutton.getTag(), compoundbutton, isChecked);
    }

    public int getSelectedSize() {
        return selectedList.size();
    }

    public ArrayList<File> getFinalSelectedFiles() {
        ArrayList<File> list = new ArrayList<>(selectedList.size());
        for (Uri uri : selectedList) {
            list.add(new File(uri.toString()));
        }
        return list;
    }

    public Uri getItem(int position) {
        return list.get(position);
    }

    public void add(Uri album) {
        list.add(album);
    }

    @Override
    public void onClick(View view) {
        onItemClickedListener.onItemClicked(view.getTag(), view);
    }

    public void clear() {
        list.clear();
    }

    public void addAll(List<Uri> list) {
        this.list.addAll(list);
    }
}
