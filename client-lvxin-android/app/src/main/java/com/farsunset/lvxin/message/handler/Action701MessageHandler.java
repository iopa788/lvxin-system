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
import com.farsunset.lvxin.database.BottleRepository;
import com.farsunset.lvxin.database.MessageRepository;


public class Action701MessageHandler implements CustomMessageHandler {

    @Override
    public boolean handle(Context context, Message message) {
        String bottleId = message.getContent();

        MessageRepository.deleteByAction(bottleId, Constant.MessageAction.ACTION_700);

        BottleRepository.deleteById(bottleId);
        MessageRepository.deleteById(message.getMid());

        return true;
    }

}
