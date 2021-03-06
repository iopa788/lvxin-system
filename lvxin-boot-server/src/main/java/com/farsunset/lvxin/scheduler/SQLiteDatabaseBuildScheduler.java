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
package com.farsunset.lvxin.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.farsunset.lvxin.service.SQLiteDatabaseService;

/**
 * 把用户信息和组织信息 生成sqlite db文件，同步给客户端
 *
 */

@Component
public class SQLiteDatabaseBuildScheduler {

	@Autowired
	private SQLiteDatabaseService sqLiteDatabaseService;

	/**
	 * 每天晚上23点重新生成一次
	 */
	@Scheduled(cron = "0 0 23 * * ?")
	public void execute() {
		sqLiteDatabaseService.createDatabase(true);
	}
}
