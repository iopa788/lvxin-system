/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.cim.push;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.mvc.container.ContextHolder;

public class MessagePusherFactory {
	private static final String DEF_CLASS = "0";
	private static MessagePusherFactory factory;
	private HashMap<String, CIMMessagePusher> pushers = new HashMap<String, CIMMessagePusher>();

	private Properties properties = new Properties();

	private MessagePusherFactory() {
		// 加载各个类型消息发送析器
		try {
			InputStream in =getClass().getClassLoader().getResourceAsStream("pusher.properties");
			properties.load(in);
			IOUtils.closeQuietly(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static MessagePusherFactory getFactory() {
		if (factory == null) {
			factory = new MessagePusherFactory();
		}

		return factory;
	}

	public void push(Message message) {

		CIMMessagePusher pusher = getMessagePusher(message.getAction());
		if (pusher != null) {
			pusher.push(message);
		} else {
			ContextHolder.getBean(DefaultMessagePusher.class).push(message);
		}

	}

	private CIMMessagePusher getMessagePusher(String code) {
		CIMMessagePusher pusher = pushers.get(code);
		if (pusher == null) {

			String className = properties.getProperty(code);
			if (StringUtils.isEmpty(className)) {
				className = properties.getProperty(DEF_CLASS);
			}

			try {
				pusher = (CIMMessagePusher) ContextHolder.getBean(Class.forName(className));
				pushers.put(code, pusher);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return pusher;
	}

}
