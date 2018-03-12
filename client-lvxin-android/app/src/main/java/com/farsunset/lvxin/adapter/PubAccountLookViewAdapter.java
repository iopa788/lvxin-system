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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsunset.lvxin.adapter.viewholder.LogoNameViewHolder;
import com.farsunset.lvxin.component.ListFooterView;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class PubAccountLookViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {
    protected int TYPE_FOOTER = 0;
    protected int TYPE_ITEM = 1;
    private List<PublicAccount> list = new ArrayList<>();
    private ListFooterView mFooterView;

    private OnItemClickedListener mOnItemClickedListener;

    public PubAccountLookViewAdapter(Context context) {
        super();
        mFooterView = (ListFooterView) LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new LogoNameViewHolder(mFooterView);
        } else {
            return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look_pubaccount, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        if (position == list.size()) {
            return;
        }
        PublicAccount target = list.get(position);
        holder.name.setText(target.name);
        holder.icon.load(FileURLBuilder.getPubAccountLogoUrl(target.account), R.drawable.icon_pubaccount);
        if (isSubscriabled(target.account)) {
            ((TextView) holder.badge).setText(R.string.label_subscribed);
            holder.badge.setSelected(true);
        } else {
            holder.badge.setSelected(false);
            ((TextView) holder.badge).setText(R.string.label_unsubscribed);
        }
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(target);
    }

    private boolean isSubscriabled(String account) {
        return PublicAccountRepository.queryPublicAccount(account) != null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void addAll(List<PublicAccount> list) {
        int start = list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

    public ListFooterView getFooterView() {
        return mFooterView;
    }
}
