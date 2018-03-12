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
package com.farsunset.lvxin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.farsunset.lvxin.service.FileManagerService;

@Service
public class FileManagerServiceImpl implements FileManagerService {

	@Autowired
	private OssFileManagerServiceImpl ossFileManagerService;

	@Autowired
	private LocalManagerServiceImpl localFileManagerService;

	@Value("${aliyun.oss.switch}")
	private String aliyunOssSwitch;

	private FileManagerService getService() {
		if ("on".equalsIgnoreCase(aliyunOssSwitch)) {
			return ossFileManagerService;
		}
		return localFileManagerService;
	}

	@Override
	public void delete(String key) {
		getService().delete(key);
	}

	@Override
	public void delete(String bucket, String key) {
		getService().delete(bucket, key);
	}

	@Override
	public void upload(MultipartFile file, String bucket, String key) {
		getService().upload(file, bucket, key);
	}

	@Override
	public Resource get(String bucket, String key) {
		return getService().get(bucket, key);
	}

}
