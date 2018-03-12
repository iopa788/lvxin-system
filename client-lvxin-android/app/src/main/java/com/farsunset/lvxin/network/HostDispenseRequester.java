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

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommonResult;

/**
 * 通过服务器调度IP分配
 */
public class HostDispenseRequester {


    public static void execute() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.HOST_DISPENSE_URL, CommonResult.class);
        HttpRequestLauncher.execute(requestBody, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, String url) {
                //data为服务端分配的服务端IP地址
                CIMPushManager.connect(LvxinApplication.getInstance(), ((CommonResult) result).data, Constant.CIM_SERVER_PORT);
            }
            @Override
            public void onHttpRequestFailure(Exception e, String url) {
            }
        });
    }

}
