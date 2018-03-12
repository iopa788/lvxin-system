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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.farsunset.lvxin.service.AccessTokenService;

public class RestfulValidateInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AccessTokenService accessTokenService;
	public static final String ACCESS_TOKEN = "access-token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		String action = request.getServletPath();
		if (AllowableActionsManager.getInstance().contains(action)) {
			return true;
		}
		 
		if (newAccessVerifyStrategy(request)) {
			return true;
		}

		response.getWriter().print("{\"code\":401}");
		return false;

	}

	private boolean newAccessVerifyStrategy(HttpServletRequest request) {
		String token = request.getHeader(ACCESS_TOKEN);

		if (token == null) {
			return false;
		}

		String account = accessTokenService.get(token);
		if (account != null) {
			request.setAttribute("account", account);
			request.setAttribute("token", token);
			return true;
		}

		return false;

	}
 
}
