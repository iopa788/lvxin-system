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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.farsunset.lvxin.service.FileManagerService;
/**
 * 文件存储在阿里云OSS的策略下 文件管理实现
 *
 */
@Service
public class OssFileManagerServiceImpl implements FileManagerService {
	protected final Logger logger = Logger.getLogger(OssFileManagerServiceImpl.class);

	@Value("${aliyun.oss.endpoint}")
	private String endpoint;

	@Value("${aliyun.oss.accessid}")
	private String accessId;

	@Value("${aliyun.oss.accesskey}")
	private String accessKey;

	@Value("${aliyun.oss.bucket}")
	private String bucket;

	@Override
	public void delete(String key) {

	}

	@Override
	public void upload(MultipartFile file, String dir, String key) {
		OSSClient client = new OSSClient(endpoint, accessId, accessKey);
		try {
			client.putObject(bucket, dir + "/" +key, file.getInputStream());
		} catch (OSSException | ClientException | IOException e) {
			e.printStackTrace();
		}
		client.shutdown();
	}

	@Override
	public String get(HttpServletRequest request, String dir, String key) {
		String[] array = endpoint.split("//");
		StringBuilder builder = new StringBuilder();
		builder.append(array[0]);
		builder.append("//");
		builder.append(bucket);
		builder.append(".");
		builder.append(array[1]);
		builder.append("/");
		builder.append(dir);
		builder.append("/");
		builder.append(key);
		return builder.toString();
	}

	@Override
	public void delete(String dir, String key) {
		OSSClient client = new OSSClient(endpoint, accessId, accessKey);
		client.deleteObject(bucket, dir + "/" +key);
		client.shutdown();
	}

}
