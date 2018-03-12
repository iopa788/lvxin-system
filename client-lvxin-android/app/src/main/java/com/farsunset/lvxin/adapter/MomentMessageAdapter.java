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

import com.farsunset.lvxin.adapter.viewholder.MomentMessageViewHolder;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MomentMessageAdapter extends RecyclerView.Adapter<MomentMessageViewHolder> implements View.OnClickListener {

    private List<Message> list = new ArrayList<>();

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener mOnItemClickedListener) {
        this.mOnItemClickedListener = mOnItemClickedListener;
    }

    @Override
    public MomentMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MomentMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MomentMessageViewHolder viewHolder, int position) {

        Comment comment = new Gson().fromJson(list.get(position).content, Comment.class);
        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(comment.account), R.drawable.icon_def_head, 200);
        viewHolder.name.setText(FriendRepository.queryFriendName(comment.account));
        viewHolder.time.setText(AppTools.howTimeAgo(Long.parseLong(comment.timestamp)));
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setTag(comment);
        if (Comment.TYPE_2.equals(comment.type)) {
            viewHolder.content.setText(R.string.tip_moment_praise);
        } else {
            String content = new JsonParser().parse(comment.content).getAsJsonObject().get("content").getAsString();
            viewHolder.content.setText(content);
        }

    }

    public void addAll(List<Message> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return list.isEmpty();
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
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

    public void remove(Message notice) {
        int i = list.indexOf(notice);
        notifyItemRemoved(i);
        list.remove(i);
    }

}
