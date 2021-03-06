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
import android.content.Intent;

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.activity.ForceOfflineAlertActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

public class Action999MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Global.removePassword();

        String deviceModel = message.getContent();
        String loginTime = AppTools.getDateTimeString(message.getTimestamp());
        Intent intent = new Intent(context, ForceOfflineAlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("title", context.getString(R.string.title_login_remind));
        intent.putExtra("message", context.getString(R.string.tip_force_offline, loginTime, deviceModel));
        context.startActivity(intent);
        MessageRepository.deleteById(message.getMid());
        return false;
    }

}
