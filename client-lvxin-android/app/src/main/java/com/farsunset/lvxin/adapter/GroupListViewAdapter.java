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

import com.farsunset.lvxin.activity.chat.GroupChatActivity;
import com.farsunset.lvxin.adapter.viewholder.LogoNameViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupListViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {

    private String account;
    private List<Group> groupList = new ArrayList<>();

    public GroupListViewAdapter() {
        super();
        account = Global.getCurrentAccount();
    }


    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_group, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.name.setText(group.name);
        holder.icon.load(FileURLBuilder.getGroupIconUrl(group.groupId), R.drawable.logo_group_normal);
        holder.badge.setVisibility(account.equals(group.founder) ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(group.getId());
    }

    public void addAll(List<Group> list) {
        int index = groupList.size();
        groupList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), GroupChatActivity.class);
        intent.putExtra(Constant.CHAT_OTHRES_ID, view.getTag().toString());
        view.getContext().startActivity(intent);
    }

    public void remove(Group group) {
        int index = groupList.indexOf(group);
        groupList.remove(index);
        notifyItemRemoved(index);
    }

    public void clearAll() {
        notifyItemRangeRemoved(0, groupList.size());
        groupList.clear();

    }
}
