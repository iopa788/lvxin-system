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

import com.farsunset.lvxin.app.LvxinApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class RequestHandlerFactory {

    static RequestHandlerFactory factory;
    HashMap<String, RequestHandler> parsers = new HashMap<String, RequestHandler>();

    Properties properties = new Properties();

    private RequestHandlerFactory() {
        //加载各个类型消息解析器
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/request.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static RequestHandlerFactory getFactory() {
        if (factory == null) {
            factory = new RequestHandlerFactory();
        }

        return factory;
    }

    public RequestHandler getMessageHandler(String msgType) {

        if (parsers.get(msgType) == null) {

            try {
                parsers.put(msgType, (RequestHandler) Class.forName(properties.getProperty(msgType)).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return parsers.get(msgType);
    }

}
