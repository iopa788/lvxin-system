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
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.builder.Action111Builder;
import com.farsunset.lvxin.model.Friend;
import com.google.gson.Gson;

public class Action111MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getMid());
        if (message.getSender().equals(message.getReceiver())) {
            return false;
        }

        Action111Builder builder = new Gson().fromJson(message.getContent(), Action111Builder.class);
        String account = builder.account;
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = account;

        FriendRepository.modifyNameAndMotto(account, builder.name, builder.motto);

        ChatItem chatItem = new ChatItem(friend,null);
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
        MessageRepository.deleteById(message.getMid());
        return false;
    }

}
