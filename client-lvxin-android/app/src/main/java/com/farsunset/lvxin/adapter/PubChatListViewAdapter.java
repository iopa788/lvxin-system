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

import com.farsunset.lvxin.adapter.viewholder.PubChatRecordViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.util.Objects;

public class PubChatListViewAdapter extends BaseChatListViewAdapter<PubChatRecordViewHolder>{
    private static final int TYPE_TO_TEXT = 0;
    private static final int TYPE_FROM_TEXT = 1;
    private static final int TYPE_FROM_LINK = 2;
    private static final int TYPE_FROM_LINKPANEL = 3;

    public PubChatListViewAdapter(MessageSource source) {
        super(source);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataList.get(position);
        if(item instanceof Long){

            return TYPE_DATE_TIME;
        }
        Message message = (Message) item;
        String type = message.format;
        boolean isSelf = Objects.equals(self.account,message.sender);

        if (!isSelf && Constant.MessageFormat.FORMAT_TEXT.equals(type)) {
            return TYPE_FROM_TEXT;
        }
        if (!isSelf && Constant.MessageFormat.FORMAT_LINK.equals(type)) {
            return TYPE_FROM_LINK;
        }
        if (!isSelf && Constant.MessageFormat.FORMAT_LINKLIST.equals(type)) {
            return TYPE_FROM_LINKPANEL;
        }
        return TYPE_TO_TEXT;
    }

    @Override
    public PubChatRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chatItemView = null;
        if (viewType == TYPE_TO_TEXT) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pubchat_to_text, parent, false);
        }

        if (viewType == TYPE_FROM_TEXT) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pubchat_reply_text, parent, false);
        }
        if (viewType == TYPE_FROM_LINK) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pubchat_reply_link, parent, false);
        }
        if (viewType == TYPE_FROM_LINKPANEL) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pubchat_reply_linkpanel, parent, false);
        }
        if (viewType == TYPE_DATE_TIME) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatting_datetime, parent, false);
        }
        return new PubChatRecordViewHolder(chatItemView);
    }

    @Override
    public void onBindDateTimeViewHolder(PubChatRecordViewHolder viewHolder,long time){
        viewHolder.dateTime.setText(AppTools.howTimeAgo(time));
    }

    @Override
    public void onBindMessageViewHolder(PubChatRecordViewHolder viewHolder,Message message) {

        if (viewHolder.toTextMessageView != null)
        {
            viewHolder.toTextMessageView.displayMessage(message,others);
            viewHolder.toTextMessageView.setOnMessageDeleteListener(this);
            return;
        }
        if (viewHolder.replyMessageView != null)
        {
            viewHolder.replyMessageView.displayMessage(message,others);
            viewHolder.replyMessageView.setOnMessageDeleteListener(this);
        }
    }

}
