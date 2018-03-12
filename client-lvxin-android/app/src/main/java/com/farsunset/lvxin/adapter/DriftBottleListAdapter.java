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

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.adapter.viewholder.DriftBottleViewHolder;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DriftBottleListAdapter extends RecyclerView.Adapter<DriftBottleViewHolder> implements View.OnClickListener {
    private OnItemClickedListener onBottleSelectedListener;
    private List<Bottle> list = new ArrayList<>();

    public void setOnBottleSelectedListener(OnItemClickedListener onBottleSelectedListener) {
        this.onBottleSelectedListener = onBottleSelectedListener;
    }

    @Override
    public DriftBottleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DriftBottleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drift_bottle, parent, false));
    }

    @Override
    public void onBindViewHolder(DriftBottleViewHolder holder, int position) {

        int color = Color.rgb(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        holder.colorTitle.setBackgroundColor(color);
        holder.carView.setOnClickListener(null);
        if (position < list.size()) {
            final Bottle bottle = list.get(position);
            holder.content.setText(bottle.content);
            holder.region.setText(bottle.region);
            holder.name.setVisibility(View.GONE);
            holder.carView.setTag(bottle);
            if (bottle.region == null) {
                holder.region.setText(R.string.label_faraway);
            }
            holder.carView.setOnClickListener(this);
            return;
        }

        holder.name.setVisibility(View.VISIBLE);
        holder.name.setText(R.string.label_bottle_goddess);
        holder.content.setText(R.string.tip_pulget_driftbottle);
        holder.region.setText(R.string.label_sea_world);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }


    public void add(Bottle bottle) {
        this.list.add(bottle);
        notifyDataSetChanged();
    }

    public void remove(Bottle bottle) {
        int index = list.indexOf(bottle);
        if (index >= 0) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onClick(View v) {
        onBottleSelectedListener.onItemClicked(v.getTag(), v);
    }

}
