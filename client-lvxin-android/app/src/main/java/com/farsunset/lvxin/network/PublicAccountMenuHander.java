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
package com.farsunset.lvxin.network;

import android.content.Intent;
import android.text.TextUtils;

import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.network.model.PubMenuEvent;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.PubAccountApiResult;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 公众号菜单处理
 */
public class PublicAccountMenuHander {


    public static void execute(final PublicAccount account, com.farsunset.lvxin.model.Message message) {
        final PubMenuEvent event = new PubMenuEvent();
        event.eventType = PubMenuEvent.EVENT_FORMAT_TEXT;
        event.text = message.content;
        event.account = Global.getCurrentUser().account;

        dispatcherEvent(account, event, message);
    }

    public static void execute(final PublicAccount account, PublicMenu menu) {
        final PubMenuEvent event = new PubMenuEvent();
        event.eventType = PubMenuEvent.EVENT_ACTION_MENU;
        event.account = Global.getCurrentAccount();
        event.menuCode = menu.code;

        dispatcherEvent(account, event, menu);
    }


    private static void dispatcherEvent(final PublicAccount account, final PubMenuEvent event, final Serializable target) {

        //如果没有配置API地址，直接返回
        if (TextUtils.isEmpty(account.apiUrl)) {
            performMenuApiCallback(Constant.MessageStatus.STATUS_SEND, target);
            return;
        }
        HttpRequestBody requestBody = new HttpRequestBody(account.apiUrl, PubAccountApiResult.class);
        requestBody.setContentType(HttpRequestBody.JSON_MEDIATYPE);
        requestBody.setContent(new Gson().toJson(event));

        HttpRequestLauncher.execute(requestBody, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, String url) {

                sendMessageBroadcast(event.account, account.account, (PubAccountApiResult) result);
                performMenuApiCallback(Constant.MessageStatus.STATUS_SEND, target);
            }

            @Override
            public void onHttpRequestFailure(Exception e, String url) {
                performMenuApiCallback(Constant.MessageStatus.STATUS_SEND_FAILURE, target);

            }
        });

    }

    private static void sendMessageBroadcast(String selfAccount, String pubAccount, PubAccountApiResult apiResult) {
        final Message message = new Message();
        message.setMid(StringUtils.getUUID());
        message.setReceiver(selfAccount);
        message.setSender(pubAccount);
        message.setAction(Constant.MessageAction.ACTION_201);
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(apiResult.toString());
        message.setFormat(apiResult.contentType);

        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), message);
        intent.putExtra(Constant.NEED_RECEIPT, false);
        LvxinApplication.sendGlobalBroadcast(intent);

    }

    private static void performMenuApiCallback(String status, Object target) {
        if (target instanceof PublicMenu)
        {
            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_PUB_MENU_INVOKED));
        }

        /**
         * 通知聊天窗口刷新状态
         */
        if (target instanceof com.farsunset.lvxin.model.Message)
        {
            com.farsunset.lvxin.model.Message message = (com.farsunset.lvxin.model.Message) target;
            message.status = status;
            MessageRepository.updateStatus(message.mid, message.status);

            Intent intent = new Intent(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
            intent.putExtra(ChatItem.NAME, new ChatItem(null,message));
            LvxinApplication.sendLocalBroadcast(intent);
        }

    }

}
