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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import com.farsunset.lvxin.service.FileManagerService;
import com.farsunset.lvxin.util.Constants;

@Service
/**
 * 文件存储在tomcat的策略下 文件管理实现
 *
 */
public class LocalManagerServiceImpl implements FileManagerService, ServletContextAware {
	private final String DEF_DIR = "other-files";
	protected final Logger logger = Logger.getLogger(LocalManagerServiceImpl.class);
	ServletContext servletContext;
	@Override
	public void delete(String key) {
		delete(DEF_DIR, key);
	}

	@Override
	public void delete(String bucket, String key) {
		if (StringUtils.isNotBlank(bucket) && StringUtils.isNotBlank(key)) {
			String path = servletContext.getRealPath(Constants.LOCAL_BUCKET) + "/" + bucket;
			File targetFile = new File(path, key);
			FileUtils.deleteQuietly(targetFile);

			logger.info("filedelete:" + bucket + "/" + key);
		}

	}

	
	@Override
	public void setServletContext(ServletContext contxt) {
		servletContext = contxt;
	}

	@Override
	public void upload(MultipartFile file, String bucket, String key) {
		String path = servletContext.getRealPath(Constants.LOCAL_BUCKET) + "/" + bucket;
		File desFile = new File(path, key);
		try {
			file.transferTo(desFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String get(HttpServletRequest request,String bucket, String key)  {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getScheme());
		builder.append("://");
		builder.append(request.getServerName());
		builder.append(":");
		builder.append(request.getServerPort());
		builder.append(request.getContextPath());
		builder.append("/");
		builder.append(Constants.LOCAL_BUCKET);
		builder.append("/");
		builder.append(bucket);
		builder.append("/");
		builder.append(key);

		return builder.toString();
	}

}
