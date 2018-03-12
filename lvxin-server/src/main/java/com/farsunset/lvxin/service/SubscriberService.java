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
package com.farsunset.lvxin.service;

import java.util.List;

import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;

public interface SubscriberService {

	void add(Subscriber subscriber);

	List<User> getSubscriberList(String paccount);

	void delete(Subscriber subscriber);

	List<String> getSubscriberAccounts(String paccount);

	/**
	 * 当有用户关注或者取消关注时，调用公众号服务端接口，因为是异步放法，需要在外部调用，所以分离开来
	 * 
	 * @param subscriber
	 *            关注着信息
	 * @param eventType
	 *            关注 或者 取消关注
	 */
	void notifySubscribeEvent(Subscriber subscriber, String eventType);

}
