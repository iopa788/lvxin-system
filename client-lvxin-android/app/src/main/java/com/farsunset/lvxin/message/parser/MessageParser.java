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
package com.farsunset.lvxin.message.parser;

import com.farsunset.lvxin.adapter.viewholder.SystemMessageViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.message.builder.BaseBuilder;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public abstract class MessageParser {


    /**
     * 将json格式消息内容解析为一个对象
     *
     * @param message
     * @return
     */
    public BaseBuilder decodeContent(Message message) {

        return new Gson().fromJson(message.content, BaseBuilder.class);
    }

    /**
     * 解析消息的来源对象
     *
     * @return
     */
    public MessageSource getMessageSource(Message message) {

        return new SystemMessage(message.action);
    }

    /**
     * 解析消息的内容对象
     *
     * @return
     */
    public MessageSource getMessageBody(String content) {

        return new SystemMessage(Constant.MessageAction.ACTION_2);
    }

    /**
     * 将消息内容再listview中显示
     *
     * @param holder
     * @param message
     */
    public abstract void displayInRecentView(SystemMessageViewHolder holder, Message message);


    /**
     * 将消息内容解码为一句字符串
     *
     * @param message
     * @return
     */
    public abstract String getMessagePreviewText(Message message);

    public String getCategoryText() {
        return LvxinApplication.getInstance().getString(R.string.common_sysmessage);
    }

    public int getThemeColor() {
        return R.color.theme_orange;
    }


}
