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
package com.farsunset.lvxin.service.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.service.SNSShakeService;

/**
 * 每天定时删除 无用的消息记录,以及摇一摇记录
 *
 */
public class ObsoletedDataCleanTask {
	@Autowired
	private MessageService messageServiceImpl;
	@Autowired
	private SNSShakeService snsShakeServiceImpl;

	public void doTask() {
		messageServiceImpl.deleteObsoleted();
		snsShakeServiceImpl.deleteObsoleted();
	}

}
