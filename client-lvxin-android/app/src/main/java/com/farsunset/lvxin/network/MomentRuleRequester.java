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

import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.MomentRuleRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MomentRuleResult;

public class MomentRuleRequester {


    public static void execute() {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MOMENT_RULE_LIST, MomentRuleResult.class);
        HttpRequestLauncher.execute(requestBody, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult data, String url) {
                final MomentRuleResult result = (MomentRuleResult) data;
                if (result.isSuccess() && result.isNotEmpty()) {
                    MomentRuleRepository.saveAll(result.dataList);
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, String url) {

            }
        });
    }


}
