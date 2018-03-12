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
package com.farsunset.lvxin.message.parser;


import android.support.v4.util.ArrayMap;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.pro.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageParserFactory {

    private  ArrayMap<String, MessageParser> parsers = new ArrayMap<String, MessageParser>();
    private  Properties properties = new Properties();
    private static class InstanceHolder{
        private static MessageParserFactory factory = new MessageParserFactory();
    }
    private MessageParserFactory() {
        //加载各个类型消息解析器
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/parsers.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageParserFactory getFactory() {
        return InstanceHolder.factory;
    }

    public static String getPreviewText(String fileType, String content, boolean self) {
        if (Constant.MessageFormat.FORMAT_IMAGE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_image);
        }
        if (Constant.MessageFormat.FORMAT_FILE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_file);
        }
        if (Constant.MessageFormat.FORMAT_VOICE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_voice);
        }
        if (Constant.MessageFormat.FORMAT_MAP.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_map);
        }
        if (Constant.MessageFormat.FORMAT_VIDEO.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_video);
        }
        if (self && Constant.MessageFormat.FORMAT_TEXT.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_normal, content);
        }
        return content;
    }

    public MessageParser getMessageParser(String msgType) {

        MessageParser messageParser = parsers.get(msgType);
        if (messageParser == null) {
            try {
                messageParser = (MessageParser) Class.forName(properties.getProperty(msgType)).newInstance();
                parsers.put(msgType,messageParser);
            } catch (Exception e) {}
        }

        return messageParser;
    }

}
