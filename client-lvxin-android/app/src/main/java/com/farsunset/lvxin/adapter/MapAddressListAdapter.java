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

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.farsunset.lvxin.adapter.viewholder.MapAddressViewHolder;
import com.farsunset.lvxin.comparator.AddressAscComparator;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapAddressListAdapter extends RecyclerView.Adapter<MapAddressViewHolder> implements View.OnClickListener {

    private static final int TYPE_CLEAR = 0;
    private static final int TYPE_ITEM = 1;
    private List<PoiInfo> list = new ArrayList<>();
    private OnItemClickedListener mOnItemClickedListener;

    public MapAddressListAdapter() {
        list.add(null);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CLEAR;
        }
        return TYPE_ITEM;
    }

    @Override
    public MapAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CLEAR) {
            return new MapAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clear_address, parent, false));
        }
        return new MapAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_address, parent, false));
    }

    @Override
    public void onBindViewHolder(MapAddressViewHolder holder, int position) {
        if (position == 0) {
            holder.itemView.setTag(null);
        } else {
            PoiInfo target = list.get(position);
            holder.content.setText(target.address);
            holder.title.setText(target.name);
            holder.itemView.setTag(target);
        }
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

    public void addAll(List<PoiInfo> list, LatLng current) {
        this.list.addAll(list);
        Collections.sort(this.list, new AddressAscComparator(current));
        notifyDataSetChanged();
    }

    public void add(PoiInfo bean) {
        this.list.add(bean);
        notifyItemInserted(list.size());
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

}
