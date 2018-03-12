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
package com.farsunset.lvxin.util;

import com.farsunset.lvxin.model.Message;

public class MessageUtil {

	public static com.farsunset.cim.sdk.server.model.Message transform(Message msg) {
		com.farsunset.cim.sdk.server.model.Message m = new com.farsunset.cim.sdk.server.model.Message();
		m.setContent(msg.getContent());
		m.setTitle(msg.getTitle());
		m.setFormat(msg.getFormat());
		m.setReceiver(msg.getReceiver());
		m.setSender(msg.getSender());
		m.setAction(msg.getAction());
		m.setMid(msg.getMid());
		m.setExtra(msg.getExtra());
		m.setTimestamp(msg.getTimestamp());
		return m;
	}

	public static Message clone(Message msg) {
		Message m = new Message();
		m.setContent(msg.getContent());
		m.setTitle(msg.getTitle());
		m.setFormat(msg.getFormat());
		m.setReceiver(msg.getReceiver());
		m.setSender(msg.getSender());
		m.setAction(msg.getAction());
		m.setMid(msg.getMid());
		m.setTimestamp(msg.getTimestamp());
		m.setExtra(msg.getExtra());
		return m;
	}

}
