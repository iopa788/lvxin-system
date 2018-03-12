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

import com.farsunset.lvxin.activity.LoginActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.MLog;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class UIOkHttpCallback implements Callback {
    private final static String TAG = UIOkHttpCallback.class.getSimpleName();
    private Class<? extends BaseResult> mClass;

    public UIOkHttpCallback(Class<? extends BaseResult> clazs) {
        mClass = clazs;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onInnerFailure(call, e);
    }

    private void onInnerFailure(final Call call, final Exception e) {
        MLog.e(TAG, "", e);
        if (mClass != null) {
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    onFailured(call, e);
                }
            });
        }
    }

    @Override
    public void onResponse(final Call call, Response response) throws IOException {
        try {
            String data = response.body().string();
            MLog.i(TAG, data);
            if (mClass == null) {
                return;
            }
            final BaseResult baseResult = new Gson().fromJson(data, mClass);
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    if (baseResult != null && Constant.ReturnCode.CODE_401.equals(baseResult.code)) {
                        Intent intent = new Intent(LvxinApplication.getInstance(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        LvxinApplication.getInstance().startActivity(intent);
                        return;
                    }
                    try {
                        onResponse(call, baseResult == null ? mClass.newInstance() : baseResult);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            onInnerFailure(call, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public abstract void onResponse(Call call, BaseResult response);

    public abstract void onFailured(Call call, Exception e);

}
