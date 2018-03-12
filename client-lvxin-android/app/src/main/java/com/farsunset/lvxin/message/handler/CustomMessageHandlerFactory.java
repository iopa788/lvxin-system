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
import android.util.ArrayMap;

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.LvxinApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 各种消息解析处理器
 */
public class CustomMessageHandlerFactory {

    private ArrayMap<String, CustomMessageHandler> parsers = new ArrayMap<String, CustomMessageHandler>();
    private Properties properties = new Properties();

    private static class InstanceHolder{
        private static  CustomMessageHandlerFactory factory = new CustomMessageHandlerFactory();
    }
    private CustomMessageHandlerFactory() {
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/handler.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static CustomMessageHandlerFactory getFactory() {
        return InstanceHolder.factory;
    }

    public boolean handle(Context context, Message message) {

        CustomMessageHandler handler = getMessageHandler(message.getAction());
        if (handler == null) {
            return true;
        }
        try {
            return handler.handle(context, message);
        } catch (Exception e) {
            return true;
        }


    }

    public CustomMessageHandler getMessageHandler(String action) {
        CustomMessageHandler messageHandler = parsers.get(action);
        if (messageHandler == null) {
            try {
                messageHandler = (CustomMessageHandler) Class.forName(properties.getProperty(action)).newInstance();
                parsers.put(action,messageHandler);
            } catch (Exception e) {
            }
        }
        return messageHandler;
    }

}
