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

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.activity.trend.BottleChatActivity;
import com.farsunset.lvxin.adapter.viewholder.BottleRecordViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.util.ArrayList;
import java.util.List;

public class BottleRecordListAdapter extends RecyclerView.Adapter<BottleRecordViewHolder> implements View.OnClickListener {

    private List<Bottle> list = new ArrayList<>();

    @Override
    public BottleRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BottleRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_drift_bottle, parent, false));
    }

    @Override
    public void onBindViewHolder(BottleRecordViewHolder viewHolder, int position) {
        Bottle bottle = list.get(position);
        viewHolder.name.setText(viewHolder.itemView.getContext().getString(R.string.label_from_bottle, bottle.region == null ? viewHolder.itemView.getContext().getString(R.string.label_faraway) : bottle.region));
        viewHolder.content.setText(bottle.content);
        viewHolder.timeText.setText(AppTools.howTimeAgo(bottle.timestamp));
        long noReadSum = MessageRepository.countNewBySender(bottle.gid, Constant.MessageAction.ACTION_700);
        if (noReadSum > 0) {
            viewHolder.badge.setVisibility(View.VISIBLE);
            viewHolder.badge.setText(String.valueOf(noReadSum));
        } else {
            viewHolder.badge.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setTag(bottle);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<Bottle> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BottleChatActivity.class);
        intent.putExtra(Bottle.class.getSimpleName(), (Bottle) v.getTag());
        v.getContext().startActivity(intent);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

}
