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
package com.farsunset.lvxin.web.jstl;

import org.apache.commons.lang3.StringUtils;

public class Functions {

	public static String chatAt(String source, int index) {
		if (source == null || index > source.length() - 1 || index < 0) {
			return "";
		}
		return String.valueOf(source.charAt(index));
	}

	public static Long timeAgo(Long t) {
		if (t == 0) {
			return t;
		}
		return (System.currentTimeMillis() - t) / 1000;
	}

	public static String html(String text) {
		text = StringUtils.replace(text, "<", "&lt;");
		text = StringUtils.replace(text, ">", "&gt;");
		return text;
	}
}
