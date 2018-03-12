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
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.exception.IllegalUserAccountException;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.mvc.container.ContextHolder;
import com.farsunset.lvxin.redis.template.UserRedisTemplate;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.repository.UserRepository;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.ProtectListManager;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private SQLiteDatabaseService sqLiteDatabaseService;

	@Autowired
	private UserRedisTemplate userRedisTemplate;
	
	@Override
	public List<User> getAll() {
		return userRepository.getAll();
	}

	@Override
	public User get(String account) {
		User user = userRedisTemplate.get(account);
		if (user == null) {
			user = userRepository.get(account);
			userRedisTemplate.saveOrRemove(account, user);
		}
		
		return user;
	}

	@Override
	public void update(User user) {
		userRepository.update(user);
		userRedisTemplate.save(user);
		sqLiteDatabaseService.update(user);
	}

	@Override
	public void updateQuietly(User user) {
		userRepository.update(user);
		userRedisTemplate.save(user);
	}

	@Override
	public void save(User user) {
		if (ProtectListManager.getInstance().contains(user.getAccount().toLowerCase())) {
			throw new IllegalUserAccountException();
		}
		if (userRepository.get(user.getAccount()) != null) {
			throw new IllegalUserAccountException();
		}

		user.setState(User.STATE_NORMAL);
		userRepository.save(user);
		userRedisTemplate.save(user);

		sqLiteDatabaseService.add(user);
	}

	@Override
	public List<User> queryNearbyList(String account,String gender) {
		return userRepository.queryNearbyList(account,gender);
	}

	@Override
	public void delete(String account) {
		User user = new User();
		user.setAccount(account);
		userRepository.delete(user);
		userRedisTemplate.remove(account);
		articleService.deleteBatch(account);
		sqLiteDatabaseService.delete(user);
	}

	@Override
	public void queryList(User user, Page page) {
		int count = this.userRepository.queryUserAmount(user);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}
		this.userRepository.queryUserList(user, page);
	}

	@Override
	public List<String> getAccounts() {
		return userRepository.getUserAccounts();
	}

	@Override
	public int saveBatch(List<User> list) {
		int count = userRepository.saveBatch(list);
		sqLiteDatabaseService.createUserDatabase();
		return count;
	}

	@Override
	public void updateState(String account, String state) {
		if (state.equals(User.STATE_DISABLE)) {
			Message message = new Message();
			message.setAction(Constants.MessageAction.ACTION_444);
			message.setSender(Constants.SYSTEM);
			message.setReceiver(account);
			ContextHolder.getBean(DefaultMessagePusher.class).push(message);
		} else {
			messageRepository.deleteByReceiverAndAction(account, Constants.MessageAction.ACTION_444);
		}

		User target = userRepository.get(account);
		target.setState(state);
		userRepository.update(target);
		userRedisTemplate.save(target);
	}
	 

	@Override
	public int countOrganizationMember(String code) {
		return userRepository.countOrganizationMember(code);
	}

}
