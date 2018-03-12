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

import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.result.SendMessageResult;

public class SendMessageRequester {

    public static void send(Message message, HttpRequestListener responser) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_SEND_URL, SendMessageResult.class);
        requestBody.addParameter("extra", message.extra);
        requestBody.addParameter("content", message.content);
        requestBody.addParameter("sender", message.sender);
        requestBody.addParameter("receiver", message.receiver);
        requestBody.addParameter("action", message.action);
        requestBody.addParameter("format", message.format);
        HttpRequestLauncher.execute(requestBody, responser);
    }
}
