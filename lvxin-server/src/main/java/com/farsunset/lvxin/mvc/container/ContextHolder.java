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
package com.farsunset.lvxin.mvc.container;

import org.springframework.context.ApplicationContext;

public class ContextHolder {

	private static ApplicationContext context;

	public static void setContext(ApplicationContext ac) {
		context = ac;
	}

	public static <T> T getBean(Class<T> c) {
		return context.getBean(c);
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}
}
