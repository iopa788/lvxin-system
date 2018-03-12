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
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.builder.Action107Builder;
import com.farsunset.lvxin.model.Group;
import com.google.gson.Gson;


public class Action107MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action107Builder builder = new Gson().fromJson(message.getContent(), Action107Builder.class);

        String account = Global.getCurrentAccount();
        GroupMemberRepository.delete(builder.groupId, builder.accountList);
        if (builder.accountList.contains(account)) {
            GroupMemberRepository.delete(builder.groupId, account);
            GroupRepository.deleteById(builder.groupId);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(new Group(builder.groupId)));
            LvxinApplication.sendLocalBroadcast(intent);
            MessageRepository.deleteBySenderOrReceiver(builder.groupId);
            MessageRepository.updateSender(message.getMid(), Constant.SYSTEM);
            return true;
        }

        MessageRepository.deleteById(message.getMid());
        return false;
    }

}
