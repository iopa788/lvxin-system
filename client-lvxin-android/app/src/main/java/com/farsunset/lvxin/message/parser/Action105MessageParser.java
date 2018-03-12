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
package com.farsunset.lvxin.message.parser;

import android.view.View;

import com.farsunset.lvxin.adapter.viewholder.SystemMessageViewHolder;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.message.builder.Action105Builder;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public class Action105MessageParser extends Action102MessageParser {


    @Override
    public Action105Builder decodeContent(String content) {

        return new Gson().fromJson(content, Action105Builder.class);
    }


    @Override
    public String getMessagePreviewText(Message msg) {
        Action105Builder builder = decodeContent(msg.content);
        return (LvxinApplication.getInstance().getString(R.string.tip_request_invitegroup, builder.name, builder.groupName));

    }

    /**
     * 解析消息的来源对象
     *
     * @param content
     * @return
     */
    @Override
    public MessageSource getMessageBody(String content) {
        Action105Builder builder = decodeContent(content);
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = builder.account;
        return friend;
    }

    @Override
    public void displayInRecentView(final SystemMessageViewHolder holder, final Message message) {
        super.displayInRecentView(holder, message);
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(getMessagePreviewText(message));
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }
}
