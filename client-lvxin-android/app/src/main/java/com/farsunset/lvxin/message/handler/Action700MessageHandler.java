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
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.BottleRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.BottleMessage;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.BottleResult;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

public class Action700MessageHandler implements CustomMessageHandler, HttpRequestListener {

    @Override
    public boolean handle(Context context, Message message) {

        MessageRepository.deleteById(message.getMid());

        BottleMessage bottle = new Gson().fromJson(message.getExtra(), BottleMessage.class);
        if (bottle == null) {
            return false;
        }

        message.setSender(bottle.bottleId);
        MessageRepository.add(MessageUtil.transform(message));

        if (BottleRepository.isNotExist(bottle.bottleId)) {
            performGetBottleRequest(bottle.bottleId);
        }
        return true;
    }

    private void performGetBottleRequest(String gid) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.BOTTLE_DETAILED_URL, BottleResult.class);
        requestBody.addParameter("gid", gid);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        if (result.isSuccess() && result instanceof BottleResult) {
            Bottle bottle = ((BottleResult) result).data;
            if (BottleRepository.hasExist(bottle.gid)) {
                return;
            }
            com.farsunset.lvxin.model.Message msg = new com.farsunset.lvxin.model.Message();
            msg.format = Constant.MessageFormat.FORMAT_TEXT;
            msg.mid = StringUtils.getUUID();
            msg.receiver = bottle.gid;
            msg.content = bottle.content;
            msg.sender = bottle.sender;
            msg.action = Constant.MessageAction.ACTION_700;
            msg.timestamp = bottle.timestamp;
            msg.status = com.farsunset.lvxin.model.Message.STATUS_READ;
            MessageRepository.add(msg);
            BottleRepository.add(bottle);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
    }


}
