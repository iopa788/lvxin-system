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
package com.farsunset.lvxin.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.farsunset.lvxin.service.AccessTokenService;

/**
 * 来自客户端的接口请求拦截器，验证token
 */
public class RestfulValidateInterceptor extends HandlerInterceptorAdapter {
	private AccessTokenService accessTokenService;
	private static final String ACCESS_TOKEN = "access-token";

	public RestfulValidateInterceptor(AccessTokenService accessTokenService) {
		this.accessTokenService = accessTokenService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		/**
		 * 有一些请求是不需要验证token的，跳过验证
		 */
		String action = request.getServletPath();
		if (AllowableActionsManager.getInstance().contains(action)) {
			return true;
		}

		String token = request.getHeader(ACCESS_TOKEN);
		String account;

		/**
		 * 直接拒绝无token的接口调用请求或者token没有查询到对应的登录用户
		 */
		if (token != null && (account = accessTokenService.get(token)) != null) {
			request.setAttribute("account", account);
			request.setAttribute("token", token);
			return true;
		}

		response.getWriter().print("{\"code\":401}");
		return false;

	}
}
