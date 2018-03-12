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
package com.farsunset.lvxin.message.request;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.pro.R;

public abstract class RequestHandler {

    Message message;
    BaseActivity context;
    User self;

    public void initialized(BaseActivity context, Message msg) {
        this.message = msg;
        this.context = context;
        self = Global.getCurrentUser();
    }


    public abstract CharSequence getMessage();

    public abstract CharSequence getDescription();

    public abstract String getTitle();

    /**
     * 解析消息的来源对象
     *
     * @return
     */
    public abstract MessageSource decodeMessageSource();

    public abstract void handleRefuse();


    public void handleIgnore() {
        MessageRepository.updateHandleStatus(SystemMessage.RESULT_IGNORE, message.mid);
        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }

    public abstract void handleAgree();

}
