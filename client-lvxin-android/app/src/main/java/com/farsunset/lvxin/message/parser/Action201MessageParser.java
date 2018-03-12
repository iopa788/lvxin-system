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

import com.farsunset.lvxin.adapter.viewholder.SystemMessageViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.model.PubLinkMessage;
import com.farsunset.lvxin.network.model.PubLinksMessage;
import com.farsunset.lvxin.network.model.PubTextMessage;
import com.google.gson.Gson;


public class Action201MessageParser extends MessageParser {


    @Override
    public MessageSource getMessageSource(Message msg) {
        return PublicAccountRepository.queryPublicAccount(msg.sender);
    }


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }


    @Override
    public String getMessagePreviewText(Message message) {

        if (Constant.MessageFormat.FORMAT_LINK.equals(message.format)) {
            return new Gson().fromJson(message.content, PubLinkMessage.class).title;
        }
        if (Constant.MessageFormat.FORMAT_LINKLIST.equals(message.format)) {
            return new Gson().fromJson(message.content, PubLinksMessage.class).title;
        }
        if (Constant.MessageFormat.FORMAT_TEXT.equals(message.format)) {
            return new Gson().fromJson(message.content, PubTextMessage.class).content;
        }
        return message.content;
    }


}
