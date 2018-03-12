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
package com.farsunset.lvxin.util;

import com.farsunset.lvxin.model.Message;

public class MessageUtil {


    private MessageUtil() {
    }

    public static Message transform(com.farsunset.cim.sdk.android.model.Message msg) {
        Message m = new Message();
        m.content = msg.getContent();
        m.title = msg.getTitle();
        m.format = msg.getFormat();
        m.receiver = msg.getReceiver();
        m.sender = msg.getSender();
        m.action = msg.getAction();
        m.extra = msg.getExtra();
        m.mid = msg.getMid();
        m.timestamp = msg.getTimestamp();
        m.status = Message.STATUS_NOT_READ;
        return m;
    }

    public static com.farsunset.cim.sdk.android.model.Message transform(Message msg) {
        com.farsunset.cim.sdk.android.model.Message m = new com.farsunset.cim.sdk.android.model.Message();
        m.setContent(msg.content);
        m.setTitle(msg.title);
        m.setFormat(msg.format);
        m.setReceiver(msg.receiver);
        m.setSender(msg.sender);
        m.setAction(msg.action);
        m.setExtra(msg.extra);
        m.setMid(msg.mid);
        m.setTimestamp(msg.timestamp);
        return m;
    }

    public static Message clone(Message msg) {
        Message m = new Message();
        m.content = msg.content;
        m.title = msg.title;
        m.format = msg.format;
        m.receiver = msg.receiver;
        m.sender = msg.sender;
        m.action = msg.action;
        m.mid = msg.mid;
        m.extra = msg.extra;
        m.timestamp = msg.timestamp;
        m.status = msg.status;
        return m;
    }

}
