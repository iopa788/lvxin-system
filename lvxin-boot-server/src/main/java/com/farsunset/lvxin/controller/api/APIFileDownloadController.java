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
package com.farsunset.lvxin.controller.api;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.farsunset.lvxin.service.impl.FileManagerServiceImpl;

@RestController
@RequestMapping("/")
public class APIFileDownloadController extends ResourceHttpRequestHandler {

	@Autowired
	private FileManagerServiceImpl fileManagerService;

	/**
	 * 文件下载，支持断点续传
	 * 
	 * @see ResourceHttpRequestHandler
	 * @param bucket
	 * @param filename
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/files/{bucket}/{key:.+}")
	public void download(@PathVariable String bucket, @PathVariable String key,String name, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		Object resource = fileManagerService.get(bucket, key);

		if (resource == null) {
			response.sendError(HttpStatus.NOT_FOUND.value());
			return;
		}

		/**
		 * 来自OSS的文件地址，重定向到第三方文件地址
		 */
		if (resource instanceof UrlResource) {
			response.sendRedirect(((UrlResource) resource).getURL().toString());
			return;
		}

		
		/**
		 * 来自本机文件，则使用spring mvc 自带的文件下载组件处理
		 */
		if(StringUtils.isNotEmpty(name)) {
		    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + URLEncoder.encode(name,"UTF-8"));
		}
		if (resource instanceof FileSystemResource) {
			request.setAttribute("resource", resource);
			handleRequest(request, response);
		}
	}

	@Override
	protected Resource getResource(HttpServletRequest request) {
		return (Resource) request.getAttribute("resource");
	}

}
