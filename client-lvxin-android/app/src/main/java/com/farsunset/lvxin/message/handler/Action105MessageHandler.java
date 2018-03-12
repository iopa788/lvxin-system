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
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.builder.Action105Builder;
import com.google.gson.Gson;

import java.util.List;


public class Action105MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Action105Builder builder = new Gson().fromJson(message.getContent(), Action105Builder.class);
        String groupId = builder.groupId;
        List<com.farsunset.lvxin.model.Message> list = MessageRepository.queryMessageByAction(Constant.MessageAction.ACTION_105);
        for (com.farsunset.lvxin.model.Message msg : list) {
            if (groupId.equals(new Gson().fromJson(msg.content, Action105Builder.class).groupId)
                    && !msg.mid.equals(message.getMid())) {
                MessageRepository.deleteById(msg.mid);
            }
        }
        MessageRepository.updateSender(message.getMid(), Constant.SYSTEM);
        return true;
    }

}
