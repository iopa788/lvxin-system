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
package com.farsunset.lvxin.bean;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;

import java.io.Serializable;

/**
 * 对话实体
 */
public class ChatItem implements Serializable {

    /**
     *
     */
    public static final String NAME = ChatItem.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    public Message message;

    public MessageSource source;

    public ChatItem() {
    }

    public ChatItem(Message msg) {
        this.message = msg;
    }

    public ChatItem(MessageSource source) {
        this.source = source;
    }

    public ChatItem(Message msg, MessageSource source) {
        this.message = msg;
        this.source = source;
    }
    public ChatItem(MessageSource source, Message msg) {
        this(msg,source);
    }
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatItem) {
            ChatItem item = (ChatItem) o;
            if (source != null && item.source != null) {
                return source.equals(item.source);
            }
        }
        return false;
    }
    public Class<? extends MessageSource> getSourceClass() {
        return source.getClass();
    }

    public long getLastTime() {
        return message == null ? 0 : message.timestamp;
    }
}
