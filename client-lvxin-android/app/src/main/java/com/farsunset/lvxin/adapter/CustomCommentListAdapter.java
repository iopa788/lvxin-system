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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farsunset.lvxin.component.EmoticonTextView;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.CommentObject;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class CustomCommentListAdapter extends BaseAdapter {
    private Moment moment;

    public CustomCommentListAdapter(Moment moment) {
        super();
        this.moment = moment;
    }

    @Override
    public int getCount() {
        return moment.getTextCount();
    }

    @Override
    public Comment getItem(int position) {
        return moment.getTextList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int index, View itemView, ViewGroup parent) {

        Comment comment = getItem(index);
        CommentObject body = new Gson().fromJson(comment.content, CommentObject.class);
        ViewHolder viewHolder = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_detailed_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) itemView.findViewById(R.id.name);
            viewHolder.reply = (TextView) itemView.findViewById(R.id.reply);
            viewHolder.replyName = (TextView) itemView.findViewById(R.id.replyName);
            viewHolder.icon = (WebImageView) itemView.findViewById(R.id.icon);
            viewHolder.content = (EmoticonTextView) itemView.findViewById(R.id.content);
            viewHolder.time = (TextView) itemView.findViewById(R.id.time);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        itemView.setTag(R.id.target, comment);

        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(comment.account), R.drawable.icon_def_head);
        viewHolder.name.setText(FriendRepository.queryFriendName(comment.account));
        viewHolder.content.setText(body.content);
        viewHolder.time.setText(AppTools.howTimeAgo(Long.parseLong(comment.timestamp)));
        if (Comment.TYPE_1.equals(comment.type)) {
            viewHolder.reply.setVisibility(View.VISIBLE);
            viewHolder.replyName.setVisibility(View.VISIBLE);
            viewHolder.replyName.setText(FriendRepository.queryFriendName(body.replyAccount));
        } else {
            viewHolder.reply.setVisibility(View.GONE);
            viewHolder.replyName.setVisibility(View.GONE);
        }
        return itemView;
    }


    private static class ViewHolder {
        TextView time;
        EmoticonTextView content;
        TextView name;
        TextView reply;
        TextView replyName;
        WebImageView icon;
    }


}
