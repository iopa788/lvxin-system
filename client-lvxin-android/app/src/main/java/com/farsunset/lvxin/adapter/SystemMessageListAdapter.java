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

import com.farsunset.lvxin.adapter.viewholder.SystemMessageViewHolder;
import com.farsunset.lvxin.message.parser.MessageParser;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.util.ArrayList;
import java.util.List;


public class SystemMessageListAdapter extends RecyclerView.Adapter<SystemMessageViewHolder> {

    private List<Message> list = new ArrayList<>();

    @Override
    public SystemMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SystemMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_message, parent, false));
    }

    @Override
    public void onBindViewHolder(SystemMessageViewHolder holder, int position) {
        final Message msg = list.get(position);

        MessageParser messageParser = MessageParserFactory.getFactory().getMessageParser(msg.action);
        holder.categoryText.setText(messageParser.getCategoryText());
        holder.tipText.setVisibility(View.GONE);
        holder.handleButton.setVisibility(View.GONE);
        messageParser.displayInRecentView(holder, msg);
        holder.time.setText(AppTools.getDateTimeString(msg.timestamp));
        holder.colorTitle.setBackgroundResource(messageParser.getThemeColor());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addMessage(Message message) {
        list.add(0, message);
        notifyItemInserted(0);
    }

    public void addAllMessage(List<Message> list) {
        this.list.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
    }

    public void onItemChanged(Message target) {
        int index = list.indexOf(target);
        if (index >= 0) {
            list.set(index, target);
            notifyItemChanged(index);
        }
    }


    public Message getLastMessage() {
        return list == null || list.isEmpty() ? null :list.get(0);
    }
}
