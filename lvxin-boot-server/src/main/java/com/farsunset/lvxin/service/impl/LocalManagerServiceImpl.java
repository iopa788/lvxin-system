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

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.farsunset.lvxin.service.FileManagerService;
import com.farsunset.lvxin.util.Constants;

/**
 * 文件存储在tomcat的策略下 文件管理实现
 *
 */
@Service
public class LocalManagerServiceImpl implements FileManagerService {
	private final String DEF_DIR = "other-files";
	private final Logger logger = Logger.getLogger(LocalManagerServiceImpl.class.getName());

	@Override
	public void delete(String key) {
		delete(DEF_DIR, key);
	}

	@Override
	public void delete(String bucket, String key) {
		if (StringUtils.isNotBlank(bucket) && StringUtils.isNotBlank(key)) {
			String path = getBaseBucketPath() + "/" + bucket;
			File targetFile = new File(path, key);
			FileUtils.deleteQuietly(targetFile);

			logger.info("filedelete:" + bucket + "/" + key);
		}

	}

	@Override
	public void upload(MultipartFile file, String bucket, String key) {

		String path = getBaseBucketPath() + "/" + bucket;
		File desFile = new File(path, key);
		try {
			file.transferTo(desFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getFile(String bucket, String key) {

		String path = getBaseBucketPath() + "/" + bucket;
		return  new File(path, key);
	}

	@Override
	public Resource get(String bucket, String key) {
		File destFile = getFile(bucket, key);
		return destFile.exists() ? new FileSystemResource(destFile) : null;
	}

	public String getBaseBucketPath() {
		return new File(getBaseJarPath(), Constants.LOCAL_BUCKET).getAbsolutePath();
	}

	private File getBaseJarPath() {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarPath = home.getSource();
		if (jarPath.getName().toLowerCase().endsWith(".jar")) {
			return jarPath.getParentFile();
		}
		return jarPath;
	}
}
