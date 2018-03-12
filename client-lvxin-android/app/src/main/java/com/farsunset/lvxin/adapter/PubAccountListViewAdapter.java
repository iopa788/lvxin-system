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

import com.farsunset.lvxin.adapter.viewholder.LogoNameViewHolder;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class PubAccountListViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {

    private List<PublicAccount> list = new ArrayList<>();

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_pubaccount, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        PublicAccount target = list.get(position);
        holder.name.setText(target.name);
        holder.icon.load(FileURLBuilder.getPubAccountLogoUrl(target.account), R.drawable.icon_pubaccount);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(target);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<PublicAccount> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

}
