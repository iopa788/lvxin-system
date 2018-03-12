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

import android.content.Intent;

import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MessageListResult;
import com.farsunset.lvxin.util.MessageUtil;

public class PullOfflineMessageRequester {
    public static void pull() {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_OFFLINELIST_URL, MessageListResult.class);
        HttpRequestLauncher.execute(requestBody, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult data, String url) {
                MessageListResult result = (MessageListResult) data;
                if (result.isSuccess() && result.isNotEmpty()) {
                    for (Message message : result.dataList) {
                        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
                        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), MessageUtil.transform(message));
                        intent.putExtra(Constant.NEED_RECEIPT, false);
                        LvxinApplication.sendGlobalBroadcast(intent);
                    }

                    performBatchReceivedRequest();
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, String url) {

            }
        });

    }

    private static void performBatchReceivedRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_BATCH_RECEIVE_URL, BaseResult.class);
        HttpRequestLauncher.executeQuietly(requestBody);
    }


}
