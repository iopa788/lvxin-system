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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.exception.IllegalUserAccountException;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.redis.template.UserRedisTemplate;
import com.farsunset.lvxin.repository.BatchAddRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.repository.UserRepository;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.service.SQLiteDatabaseService;
import com.farsunset.lvxin.service.UserService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.ProtectListManager;

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

	@Autowired
	private DefaultMessagePusher defaultMessagePusher;

	@Autowired
	private BatchAddRepository batchAddRepository;

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public User get(String account) {

		User user = userRedisTemplate.get(account);

		if (user == null) {
			user = userRepository.findOne(account);
			userRedisTemplate.saveOrRemove(account, user);
		}

		return user;
	}

	@Override
	public void update(User user) {
		userRepository.saveAndFlush(user);
		userRedisTemplate.save(user);
		sqLiteDatabaseService.update(user);
	}

	@Override
	public void updateQuietly(User user) {
		userRepository.saveAndFlush(user);
		userRedisTemplate.save(user);
	}

	@Override
	public void save(User user) {
		if (ProtectListManager.getInstance().contains(user.getAccount().toLowerCase())) {
			throw new IllegalUserAccountException();
		}
		User target = userRepository.findOne(user.getAccount());
		if (target != null) {
			throw new IllegalUserAccountException();
		}

		user.setState(User.STATE_NORMAL);
		userRepository.save(user);
		userRedisTemplate.save(user);

		sqLiteDatabaseService.add(user);
	}

	@Override
	public List<User> queryNearbyList(String account, String gender) {
		Specification<User> specification = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				predicatesList.add(builder.equal(root.get("online").as(String.class), User.ON_LINE));
				predicatesList.add(builder.isNotNull(root.get("latitude").as(String.class)));
				predicatesList.add(builder.isNotNull(root.get("longitude").as(String.class)));
				predicatesList.add(builder.notEqual(root.get("account").as(String.class), account));

				if (StringUtils.isNotEmpty(gender)) {
					predicatesList.add(builder.equal(root.get("gender").as(String.class), gender));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				return query.getRestriction();
			}
		};
		return userRepository.findAll(specification);
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
	public List<String> getAccounts() {
		return userRepository.getUserAccounts();
	}

	@Override
	public void saveAll(List<User> list) {
		batchAddRepository.saveUser(list);
		sqLiteDatabaseService.createUserDatabase();
	}

	@Override
	public void updateState(String account, String state) {
		if (state.equals(User.STATE_DISABLE)) {
			Message message = new Message();
			message.setAction(Constants.MessageAction.ACTION_444);
			message.setSender(Constants.SYSTEM);
			message.setReceiver(account);
			defaultMessagePusher.push(message);
		} else {
			messageRepository.deleteByReceiverAndAction(account, Constants.MessageAction.ACTION_444);
		}

		User target = get(account);
		target.setState(state);
		userRepository.saveAndFlush(target);
		userRedisTemplate.save(target);
	}

	@Override
	public int countOrganizationMember(String code) {
		return userRepository.countByOrgCode(code);
	}

	@Override
	public Page<User> queryPage(User model, Pageable pageable) {
		Specification<User> specification = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(model.getAccount())) {
					predicatesList
							.add(builder.like(root.get("account").as(String.class), "%" + model.getAccount() + "%"));
				}
				if (StringUtils.isNotEmpty(model.getName())) {
					predicatesList.add(builder.like(root.get("name").as(String.class), "%" + model.getName() + "%"));
				}
				if (StringUtils.isNotEmpty(model.getTelephone())) {
					predicatesList.add(
							builder.like(root.get("telephone").as(String.class), "%" + model.getTelephone() + "%"));
				}
				if (StringUtils.isNotEmpty(model.getEmail())) {
					predicatesList.add(builder.like(root.get("email").as(String.class), "%" + model.getEmail() + "%"));
				}
				if (StringUtils.isNotEmpty(model.getGender())) {
					predicatesList.add(builder.equal(root.get("gender").as(String.class), model.getGender()));
				}
				if (StringUtils.isNotEmpty(model.getOrgCode())) {
					predicatesList.add(builder.equal(root.get("orgCode").as(String.class), model.getOrgCode()));
				}
				if (StringUtils.isNotEmpty(model.getState())) {
					predicatesList.add(builder.equal(root.get("state").as(String.class), model.getState()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				return query.getRestriction();
			}
		};
		return this.userRepository.findAll(specification, pageable);
	}

}
