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
import android.view.ViewGroup;

import com.farsunset.lvxin.adapter.viewholder.ChatRecordViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

public class ChatRecordListViewAdapter extends BaseChatListViewAdapter<ChatRecordViewHolder>{
    private static final int FACTOR = 9999;
    public ChatRecordListViewAdapter(MessageSource friend) {
        super(friend);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataList.get(position);
        if(item instanceof Long){

            return TYPE_DATE_TIME;
        }
        Message message = (Message) item;
        String type = message.format;
        boolean isSelf = self.account.equals(message.sender);
        return isSelf ? Integer.parseInt(type) : Integer.parseInt(type) + FACTOR;

    }

    @Override
    public ChatRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_TEXT)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_text, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_image, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_VOICE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_voice, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_FILE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_file, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_MAP)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_map, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_video, parent, false));
        }

        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_TEXT) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_text, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_IMAGE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_image, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_VOICE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_voice, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_FILE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_file, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_MAP) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_map, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_VIDEO) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_video, parent, false));
        }
        if (viewType == TYPE_DATE_TIME) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatting_datetime, parent, false));
        }
        return null;
    }


    @Override
    public void onBindMessageViewHolder(ChatRecordViewHolder viewHolder,Message message){
        if (viewHolder.toMessageView != null) {
            viewHolder.toMessageView.displayMessage(message, self, others);
            viewHolder.toMessageView.setOnMessageDeleteListener(this);

        } else {
            viewHolder.fromMessageView.displayMessage(message, others);
            viewHolder.fromMessageView.setOnMessageDeleteListener(this);
        }
    }

    @Override
    public void onBindDateTimeViewHolder(ChatRecordViewHolder viewHolder,long time){
        viewHolder.dateTime.setText(AppTools.howTimeAgo(time));
    }
}
