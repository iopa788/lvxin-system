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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.farsunset.lvxin.activity.contact.GroupMemberListActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.adapter.viewholder.GroupMemberViewHolder;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberListViewAdapter extends RecyclerView.Adapter<GroupMemberViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;
    private List<GroupMember> list = new ArrayList<>();
    private String creator;
    private GroupMember selected;
    private boolean isMangmentMode = false;

    public GroupMemberListViewAdapter(Context c, String creator) {
        super();
        this.context = c;
        this.creator = creator;
    }

    public boolean getMangmentMode() {
        return isMangmentMode;
    }

    public void setMangmentMode(boolean f) {
        isMangmentMode = f;
        notifyDataSetChanged();
    }

    @Override
    public GroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupMemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_member, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupMemberViewHolder holder, int position) {
        GroupMember target = list.get(position);
        holder.icon.load(FileURLBuilder.getUserIconUrl(target.account), R.drawable.icon_def_head);
        holder.name.setText(target.name);
        if (target.name == null) {
            holder.name.setText(FriendRepository.queryFriendName(target.account));
        }
        holder.radioButton.setTag(target);
        holder.radioButton.setVisibility(View.GONE);
        holder.radioButton.setOnCheckedChangeListener(null);
        holder.radioButton.setChecked(false);
        if (target.account.equals(creator)) {
            holder.mark.setVisibility(View.VISIBLE);
        } else {
            holder.mark.setVisibility(View.GONE);
            if (isMangmentMode) {
                if (selected == target) {
                    holder.radioButton.setChecked(true);
                }
                holder.radioButton.setVisibility(View.VISIBLE);
                holder.radioButton.setOnCheckedChangeListener(this);
            }
        }
        holder.itemView.setTag(target);
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

    public void addAll(List<GroupMember> groupMembers) {
        list.clear();
        list.addAll(groupMembers);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (isMangmentMode) {
            ((RadioButton) view.findViewById(R.id.radioButton)).toggle();
            return;
        }
        GroupMember member = (GroupMember) view.getTag();
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(Friend.class.getName(), FriendRepository.queryFriend(member.account));
        context.startActivity(intent);
    }

    public GroupMember getSelected() {
        return selected;
    }

    public void remove(GroupMember member) {
        int index = list.indexOf(member);
        list.remove(index);
        notifyItemRemoved(index);
        selected = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        selected = (GroupMember) compoundButton.getTag();
        notifyDataSetChanged();
        ((GroupMemberListActivity) context).onMemberSelected();
    }


}
