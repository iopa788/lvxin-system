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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.BottleRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.BottleService;
import com.farsunset.lvxin.util.Constants;

@Service
public class BottleServiceImpl implements BottleService {

	@Autowired
	private BottleRepository bottleRepository;
	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	DefaultMessagePusher defaultMessagePusher;

	@Override
	public void delete(Bottle bottle) {
		messageRepository.deleteBottleMessage(bottle.getSender(), bottle.getReceiver());
		bottleRepository.delete(bottle);

		Message message = new Message();
		message.setContent(bottle.getGid());
		message.setSender(Constants.SYSTEM);
		message.setReceiver(bottle.getReceiver());
		message.setAction(Constants.MessageAction.ACTION_701);
		defaultMessagePusher.push(message);
	}

	@Override
	public Bottle queryById(String gid) {
		return bottleRepository.findOne(gid);
	}

	@Override
	public void save(Bottle obj) {
		bottleRepository.save(obj);
	}

	@Override
	public void update(Bottle bottle) {
		bottleRepository.saveAndFlush(bottle);
	}

	@Override
	public Bottle getRandomOne(String account) {
		Pageable page = new PageRequest(0, 1);
		List<Bottle> list = bottleRepository.getRandomOne(account, page);
		return list.isEmpty() ? null : list.get(0);
	}
}
