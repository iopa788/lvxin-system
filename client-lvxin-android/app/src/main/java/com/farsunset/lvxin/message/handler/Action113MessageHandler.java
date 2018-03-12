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
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.builder.Action113Builder;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;


public class Action113MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action113Builder builder = new Gson().fromJson(message.getContent(), Action113Builder.class);
        GroupMember member = new GroupMember();
        member.account = builder.acount;
        member.groupId = builder.groupId;
        member.name = builder.name;
        member.gid = StringUtils.getUUID();
        member.host = GroupMember.RULE_NORMAL;
        GroupMemberRepository.delete(member.groupId, member.account);
        GroupMemberRepository.saveMember(member);
        MessageRepository.deleteById(message.getMid());
        return false;
    }
}
