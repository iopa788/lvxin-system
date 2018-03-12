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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.adapter.viewholder.FileItemViewHolder;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class FileChooseViewAdapter extends RecyclerView.Adapter<FileItemViewHolder> implements View.OnClickListener {

    private ArrayList<File> list = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    public void addAll(ArrayList<File> tempList) {
        list.clear();
        list.addAll(tempList);
        super.notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public FileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filechoose, parent, false));
    }

    @Override
    public void onBindViewHolder(FileItemViewHolder holder, int position) {
        File file = list.get(position);
        holder.name.setText(file.getName());

        if (file.isDirectory()) {
            holder.arrow.setVisibility(View.VISIBLE);
            holder.size.setVisibility(View.GONE);
            holder.icon.setImageResource(R.drawable.icon_folder);

        } else {
            holder.size.setVisibility(View.VISIBLE);
            holder.size.setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(file.length()));
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.icon.setImageResource(FileUtils.getFileIcon(file.getName()));
        }
        holder.itemView.setTag(file);
        holder.itemView.setOnClickListener(this);
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
    public void onClick(View v) {
        onItemClickedListener.onItemClicked(v.getTag(), v);
    }

    public File getItem(int position) {
        return list.get(position);
    }
}
