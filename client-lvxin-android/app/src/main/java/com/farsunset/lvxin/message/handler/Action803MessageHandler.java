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
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.MomentRepository;


public class Action803MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        String articleId = message.getContent();
        MomentRepository.deleteById(articleId);
        CommentRepository.deleteByArticleId(articleId);
        MessageRepository.deleteById(message.getMid());
        MessageRepository.deleteByActionAndContent(Constant.MessageAction.ACTION_800, articleId);
        return true;
    }

}
