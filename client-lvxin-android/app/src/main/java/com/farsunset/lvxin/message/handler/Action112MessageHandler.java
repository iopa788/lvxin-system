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

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.builder.Action112Builder;
import com.farsunset.lvxin.model.Group;
import com.google.gson.Gson;


public class Action112MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action112Builder builder = new Gson().fromJson(message.getContent(), Action112Builder.class);
        String account = builder.acount;
        String groupId = builder.groupId;
        GroupMemberRepository.delete(groupId, account);
        Group group = GroupRepository.queryById(groupId);
        if (group.founder.equals(Global.getCurrentAccount())) {
            MessageRepository.updateSender(message.getMid(), Constant.SYSTEM);
            return true;
        }

        MessageRepository.deleteById(message.getMid());
        return false;
    }

}
