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

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.farsunset.lvxin.activity.chat.SystemMessageActivity;
import com.farsunset.lvxin.adapter.viewholder.SystemMessageViewHolder;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.message.builder.Action102Builder;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action102MessageParser extends MessageParser {

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }

    /**
     * 解析消息的来源对象
     *
     * @param content
     * @return
     */
    @Override
    public MessageSource getMessageBody(String content) {
        Action102Builder builder = decodeContent(content);
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = builder.account;
        return friend;
    }

    @Override
    public void displayInRecentView(final SystemMessageViewHolder holder, final Message message) {
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(getMessagePreviewText(message));
        Action102Builder builder = decodeContent(message.content);
        holder.tipText.setVisibility(View.VISIBLE);
        if (message.handleStatus == null) {
            holder.handleButton.setVisibility(View.VISIBLE);

            holder.handleButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ((SystemMessageActivity) holder.content.getContext()).onHandleButtonClick(message);
                }
            });

            holder.tipText.setVisibility(View.GONE);

        } else if (message.handleStatus.equals(SystemMessage.RESULT_AGREE)) {

            holder.tipText.setText(R.string.common_has_agree);
            holder.tipText.setBackgroundResource(R.drawable.badge_button_green);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), R.color.theme_green));
        } else if (message.handleStatus.equals(SystemMessage.RESULT_IGNORE)) {

            holder.tipText.setText(R.string.common_has_ignore);
            holder.tipText.setBackgroundResource(R.drawable.badge_button);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), R.color.text_grey));
        } else if (message.handleStatus.equals(SystemMessage.RESULT_REFUSE)) {
            holder.tipText.setBackgroundResource(R.drawable.badge_button_red);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), android.R.color.white));
            holder.tipText.setText(R.string.common_has_refuse);
        }
        holder.icon.load(FileURLBuilder.getUserIconUrl(builder.account), R.drawable.icon_def_head, 99);

    }


    public Action102Builder decodeContent(String content) {

        return new Gson().fromJson(content, Action102Builder.class);
    }


    @Override
    public String getMessagePreviewText(Message message) {
        StringBuffer sb = new StringBuffer();
        Action102Builder builder = decodeContent(message.content);
        sb.append(LvxinApplication.getInstance().getString(R.string.tip_request_joingroup, builder.name, builder.groupName)).append(TextUtils.isEmpty(builder.requestMsg) ? "" : "\n" + LvxinApplication.getInstance().getString(R.string.tip_request_verify, builder.requestMsg));
        return sb.toString();
    }

    @Override
    public String getCategoryText() {
        return LvxinApplication.getInstance().getString(R.string.tip_title_groupmessage);
    }

}
