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

import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.result.BaseResult;


/**
 * 消息阅读状态发送
 */
public class MessageReadHandler {


    public static void sendReadStatus(Message message) {
        if (message.isNeedShowReadStatus()) {
            HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_READ_URL, BaseResult.class);
            requestBody.addParameter("sender", message.receiver);
            requestBody.addParameter("content", message.mid);
            requestBody.addParameter("receiver", message.sender);
            requestBody.addParameter("notify", ClientConfig.getMessageReceiptEnable() ? "1" : "0");// 1:给对方发送 已经阅读提示， 0 ：不发送
            HttpRequestLauncher.executeQuietly(requestBody);
        }
    }

}
