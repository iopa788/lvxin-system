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
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;

public class Action700MessageParser extends MessageParser {

    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }


    @Override
    public String getMessagePreviewText(Message message) {
        return LvxinApplication.getInstance().getString(R.string.title_received_bottle_msg);
    }

    @Override
    public MessageSource getMessageSource(Message message) {
        return null;
    }


}
