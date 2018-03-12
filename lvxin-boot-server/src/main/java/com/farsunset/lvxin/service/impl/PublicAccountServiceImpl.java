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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.cim.push.PubAccountMessagePusher;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.redis.template.PubAccountRedisTemplate;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.repository.PublicAccountRepository;
import com.farsunset.lvxin.repository.PublicMenuRepository;
import com.farsunset.lvxin.service.PublicAccountService;
import com.farsunset.lvxin.util.Constants;
import com.google.gson.Gson;

@Service
public class PublicAccountServiceImpl implements PublicAccountService {

	private final static String ICON_DIR = "pub-icon";

	@Autowired
	private PublicAccountRepository publicAccountRepository;

	@Autowired
	private PublicMenuRepository publicMenuRepository;

	@Autowired
	private PubAccountMessagePusher pubAccountMessagePusher;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private FileManagerServiceImpl fileManagerService;

	@Autowired
	private PubAccountRedisTemplate pubAccountRedisTemplate;

	@Override
	public PublicAccount queryById(String paccount) {

		PublicAccount account = pubAccountRedisTemplate.get(paccount);

		if (account == null) {
			account = publicAccountRepository.findOne(paccount);
			pubAccountRedisTemplate.saveOrRemove(paccount, account);
		}

		return account;
	}

	@Override
	public void update(PublicAccount publicAccount) {
		publicAccountRepository.saveAndFlush(publicAccount);
		pubAccountRedisTemplate.save(publicAccount);
		boradcastOnInfoUpdated(publicAccount);
	}

	@Override
	public void save(PublicAccount publicAccount) {
		PublicAccount target = publicAccountRepository.findOne(publicAccount.getAccount());
		if (target != null) {
			throw new IllegalArgumentException();
		}
		publicAccount.setTimestamp(System.currentTimeMillis());
		publicAccountRepository.save(publicAccount);
		pubAccountRedisTemplate.save(publicAccount);

	}

	@Override
	public void delete(String account) {
		PublicAccount publicAccount = new PublicAccount();
		publicAccount.setAccount(account);
		publicAccountRepository.delete(publicAccount);
		pubAccountRedisTemplate.remove(account);

	}

	@Override
	public Page<PublicAccount> queryPage(PublicAccount publicAccount, Pageable pageable) {
		Specification<PublicAccount> specification = new Specification<PublicAccount>() {

			@Override
			public Predicate toPredicate(Root<PublicAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (StringUtils.isNotEmpty(publicAccount.getAccount())) {
					predicatesList.add(
							builder.like(root.get("account").as(String.class), "%" + publicAccount.getAccount() + "%"));
				}
				if (StringUtils.isNotEmpty(publicAccount.getName())) {
					predicatesList
							.add(builder.like(root.get("name").as(String.class), "%" + publicAccount.getName() + "%"));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				return query.getRestriction();
			}
		};
		return this.publicAccountRepository.findAll(specification, pageable);
	}

	@Override
	public List<PublicAccount> queryListByUserAccount(String account) {
		List<PublicAccount> list = publicAccountRepository.queryListByUserAccount(account);
		for (PublicAccount pub : list) {
			Set<PublicMenu> menuList = new HashSet<PublicMenu>(publicMenuRepository.getPublicMenuList(pub.getAccount()));
			pub.setMenuList(menuList);
		}
		return list;
	}

	@Override
	public void updateLogo(String account, MultipartFile file) {
		fileManagerService.upload(file, ICON_DIR, account);
		boradcastOnLogoUpdated(account);
	}

	private void boradcastOnInfoUpdated(PublicAccount publicAccount) {

		messageRepository.deleteBySenderAndAction(publicAccount.getAccount(), Constants.MessageAction.ACTION_203);
		Message message = new Message();
		message.setSender(publicAccount.getAccount());
		message.setAction(Constants.MessageAction.ACTION_203);
		message.setContent(new Gson().toJson(publicAccount));
		pubAccountMessagePusher.push(message, publicAccount.getAccount());

	}

	private void boradcastOnLogoUpdated(String account) {

		messageRepository.deleteBySenderAndAction(account, Constants.MessageAction.ACTION_205);

		Message message = new Message();
		message.setSender(account);
		message.setAction(Constants.MessageAction.ACTION_205);
		pubAccountMessagePusher.push(message, account);
	}

	@Override
	public List<PublicAccount> queryAll() {
		return publicAccountRepository.findAll();
	}

}
