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

import org.springframework.stereotype.Component;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.util.Constants;

/**
 * 推送系统消息
 */
@Component
public class DefaultMessagePusher extends CIMMessagePusher {

	@Override
	public void push(Message msg) {

		if (msg.getSender() == null) {
			msg.setSender(Constants.SYSTEM);
		}

		super.push(msg);

		// 保存数据库
		messageService.save(msg);

	}

}
