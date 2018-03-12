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

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.message.bean.PubMenuEvent;
import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.network.SubscriberEventHandler;
import com.farsunset.lvxin.repository.SubscriberRepository;
import com.farsunset.lvxin.service.SubscriberService;
import com.farsunset.lvxin.util.UUIDTools;

@Service
public class SubscriberServiceImpl implements SubscriberService {
	protected final Logger logger = Logger.getLogger(SubscriberServiceImpl.class.getName());

	@Autowired
	private SubscriberRepository subscriberRepository;
	@Autowired
	private SubscriberEventHandler subscriberEventHandler;

	@Override
	public void add(Subscriber subscriber) {

		boolean isSubscribed = subscriberRepository.exists(Example.of(subscriber));
		if (isSubscribed) {
			return;
		}
		subscriber.setTimestamp(String.valueOf(System.currentTimeMillis()));
		subscriber.setGid(UUIDTools.getUUID());
		subscriberRepository.save(subscriber);
		subscriberEventHandler.handle(subscriber, PubMenuEvent.EVENT_ACTION_SUBSCRIBE);

	}

	@Override
	public void delete(Subscriber subscriber) {

		subscriberRepository.delete(subscriber.getAccount(), subscriber.getPublicAccount());
		subscriberEventHandler.handle(subscriber, PubMenuEvent.EVENT_ACTION_UNSUBSCRIBE);
	}

	@Override
	public List<User> getSubscriberList(String publicAccount) {

		return subscriberRepository.getSubscriberList(publicAccount);
	}

	@Override
	public List<String> getSubscriberAccounts(String account) {
		return subscriberRepository.getSubscriberAccounts(account);
	}

}
