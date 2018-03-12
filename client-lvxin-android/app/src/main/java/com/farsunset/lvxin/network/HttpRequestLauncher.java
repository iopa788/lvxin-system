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

import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.util.MLog;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequestLauncher {
    private final static String TAG = HttpRequestLauncher.class.getSimpleName();

    public static void executeQuietly(HttpRequestBody body) {
        execute(body, null);
    }

    public static void execute(final HttpRequestBody body, final HttpRequestListener listener) {
        prefromHttpRequest(body, new UIOkHttpCallback(body.getDataClass()) {
            @Override
            public void onResponse(Call call, BaseResult response) {
                if (listener != null) {
                    listener.onHttpRequestSucceed(response, body.getUrl());
                }
            }

            @Override
            public void onFailured(Call call, Exception e) {
                if (listener != null) {
                    listener.onHttpRequestFailure(e, body.getUrl());
                }
            }
        });
    }


    private static void prefromHttpRequest(HttpRequestBody body, Callback callback) {

        MLog.i(TAG, body.getUrl());
        MLog.i(TAG, new Gson().toJson(body.getHeader()));
        MLog.i(TAG, new Gson().toJson(body.getParameter()));

        OkHttpClient httpclient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        Request.Builder requestBuilder = new Request.Builder().url(body.getUrl());

        /**
         * 组装 键值对参数
         */
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : body.getParameter().keySet()) {
            formBuilder.add(key, body.getParameter().get(key));
        }
        requestBuilder.method(body.getMethod(), formBuilder.build());

        /**
         * 组装 request context参数
         */
        if (body.getContent() != null && body.getContentType() != null) {
            requestBuilder.post(RequestBody.create(MediaType.parse(body.getContentType()), body.getContent()));
        }

        /**
         * 组装 http header参数
         */
        for (String key : body.getHeader().keySet()) {
            String value = body.getHeaderValue(key);
            if (value != null) {
                requestBuilder.header(key, value);
            }
        }

        httpclient.newCall(requestBuilder.build()).enqueue(callback);
    }
}
