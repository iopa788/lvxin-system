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
package com.farsunset.lvxin.receiver;

import android.content.Intent;

import com.farsunset.cim.sdk.android.CIMEventBroadcastReceiver;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.message.handler.CustomMessageHandlerFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.MessageReceiptHandler;
import com.farsunset.lvxin.network.PullOfflineMessageRequester;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.service.MessageNotifyService;
import com.farsunset.lvxin.util.MLog;
import com.farsunset.lvxin.util.MessageUtil;

/**
 * 消息入口，所有消息都会经过这里
 */
public final class CIMPushMessageReceiver extends CIMEventBroadcastReceiver {

    public static final String TAG = CIMPushMessageReceiver.class.getSimpleName();

    /**
     * 当收到消息时调用此方法
     */
    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message, Intent intent) {


        MLog.i(TAG, message.toString());


        Message msg = MessageUtil.transform(message);

        if (intent.getBooleanExtra(Constant.NEED_RECEIPT, true)) {
            //第一件事情做消息状态回执,将消息状态设为已接收状态
            MessageReceiptHandler.handleReceipt(msg);
        }

        MessageRepository.add(msg);

        boolean isNeedDispatch = CustomMessageHandlerFactory.getFactory().handle(super.context, message);
        if (!isNeedDispatch) {
            return;
        }

        CIMListenerManager.notifyOnMessageReceived(message);

        if (BuildConfig.DEBUG) {
            CIMListenerManager.logListenersName();
        }

        //显示通知栏消息
        if (Global.getAppInBackground()) {
            Intent notufyIntent = new Intent(context, MessageNotifyService.class);
            notufyIntent.putExtra(Message.NAME, msg);
            context.startService(notufyIntent);
        }
    }

    @Override
    public void onConnectionSuccessed(boolean hasAutoBind) {
        super.onConnectionSuccessed(hasAutoBind);
        if (!hasAutoBind) {
            // 绑定账号到服务端
            CIMPushManager.bindAccount(LvxinApplication.getInstance(), Global.getCurrentAccount());
        }
    }

    @Override
    public void onReplyReceived(ReplyBody reply) {
        super.onReplyReceived(reply);
        // 当账号绑定到服务端成功，拉取离线消息
        if (reply.getKey().equals(CIMConstant.RequestKey.CLIENT_BIND) && reply.getCode().equals(CIMConstant.ReturnCode.CODE_200)) {
            getOfflineMessage();
        }
        if (reply.getKey().equals(CIMConstant.RequestKey.CLIENT_BIND) && reply.getCode().equals(CIMConstant.ReturnCode.CODE_500)) {
            // 绑定失败，重新绑定账号到服务端
            MLog.e(TAG, reply.getMessage());
        }
    }

    /**
     * 登录成功后，拉取离线消息
     */
    public void getOfflineMessage() {
        /**
         * 第一次，通讯录数据还没有获取到的时候，等待通讯录获取成功再获取离线消息
         */
        if (FriendRepository.count() == 0)
        {
            return;
        }
        PullOfflineMessageRequester.pull();
    }
}
