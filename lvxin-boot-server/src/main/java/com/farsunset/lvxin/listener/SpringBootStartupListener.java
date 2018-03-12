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
package com.farsunset.lvxin.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.farsunset.lvxin.service.ManagerService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.service.impl.LocalManagerServiceImpl;

@Component
public class SpringBootStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private ManagerService managerService;

	@Autowired
	private SQLiteDatabaseService sqliteDatabaseService;

	@Autowired
	private LocalManagerServiceImpl localManagerServiceImpl;

	@Value("${sys.local.bucket.list}")
	private String bucketList;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		createLocalBuckets();

		managerService.insertDefaultManager();
		sqliteDatabaseService.initDatabase();
	}

	private void createLocalBuckets() {

		try {
			String bucketPath = localManagerServiceImpl.getBaseBucketPath();
			File bucketDir = new File(bucketPath);
			FileUtils.forceMkdir(bucketDir);
			String[] bucketArray = StringUtils.split(bucketList, '|');
			for (String bucket : bucketArray) {
				FileUtils.forceMkdir(new File(bucketDir, bucket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
