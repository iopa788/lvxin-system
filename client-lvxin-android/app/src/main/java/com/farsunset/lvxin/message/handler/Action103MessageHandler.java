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

import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.parser.MessageParser;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;


public class Action103MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        MessageParser messageParser = MessageParserFactory.getFactory().getMessageParser(message.getAction());
        Group group = (Group) messageParser.getMessageBody(message.getContent());
        GroupRepository.add(group);


        com.farsunset.cim.sdk.android.model.Message msg = new com.farsunset.cim.sdk.android.model.Message();
        msg.setTimestamp(System.currentTimeMillis());
        msg.setMid(StringUtils.getUUID());
        msg.setAction(Constant.MessageAction.ACTION_3);
        msg.setSender(group.groupId);
        msg.setReceiver(message.getReceiver());
        msg.setFormat(Constant.MessageFormat.FORMAT_TEXT);
        msg.setContent(context.getString(R.string.tip_group_hello_message));
        msg.setTitle(group.founder);

        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), msg);
        intent.putExtra(Constant.NEED_RECEIPT, false);
        LvxinApplication.getInstance().sendGlobalBroadcast(intent);

        MessageRepository.updateSender(message.getMid(), Constant.SYSTEM);

        return true;
    }

}
