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
package com.farsunset.lvxin.message.handler;


import android.content.Context;
import android.content.Intent;

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.GlideImageRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.util.FileURLBuilder;

public class Action110MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getMid());
        if (message.getSender().equals(message.getReceiver())) {
            return false;
        }

        String account = message.getContent();

        GlideImageRepository.save(FileURLBuilder.getUserIconUrl(account), String.valueOf(System.currentTimeMillis()));

        Friend friend = FriendRepository.queryFriend(account);
        ChatItem chatItem = new ChatItem(friend,null);
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);

        return false;

    }

}
