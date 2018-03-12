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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.BottleRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.BottleService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class BottleServiceImpl implements BottleService {
	protected final Logger logger = Logger.getLogger(BottleServiceImpl.class);

	@Autowired
	private BottleRepository bottleRepository;
	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	DefaultMessagePusher defaultMessagePusher;

	@Override
	public void delete(Bottle bottle) {
		messageRepository.deleteBottleMessage(bottle);
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
		// TODO Auto-generated method stub
		return bottleRepository.get(gid);
	}

	@Override
	public void save(Bottle obj) {

		bottleRepository.save(obj);

	}

	@Override
	public void queryPage(Bottle mo, Page page) {
		int count = this.bottleRepository.queryBottleAmount(mo);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}

		this.bottleRepository.queryByPage(mo, page);
	}

	@Override
	public List<Bottle> queryBottle(Bottle mo) {
		return bottleRepository.queryList(mo);
	}

	@Override
	public Bottle queryRandom(String account) {
		return bottleRepository.queryRandom(account);
	}

	@Override
	public void updateReset(String gid) {
		bottleRepository.reset(gid);
	}

	@Override
	public void update(Bottle bottle) {
		// TODO Auto-generated method stub
		bottleRepository.update(bottle);
	}

	@Override
	public List<Bottle> queryNewList(String account, Page page) {
		return bottleRepository.queryNewList(account, page);
	}
}
