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
package com.farsunset.lvxin.message.bean;

import java.io.Serializable;

/**
 * 公众账号操作事件
 */
public class PubMenuEvent implements Serializable {

	public static final long serialVersionUID = 1L;

	public final static String EVENT_ACTION_TEXT = "text";// 发送文字事件
	public final static String EVENT_ACTION_MENU = "menu";// 点击菜单事件
	public final static String EVENT_ACTION_SUBSCRIBE = "subscribe";// 订阅事件
	public final static String EVENT_ACTION_UNSUBSCRIBE = "unsubscribe";// 取消订阅事件

	public String eventType;// 事件类型

	public String text;// 用户发送的文本内容

	public String account;// 用户账户

	public String menuCode;// 自定义菜单的key

}
