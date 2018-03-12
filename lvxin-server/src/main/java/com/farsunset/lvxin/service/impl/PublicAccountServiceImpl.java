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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
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
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class PublicAccountServiceImpl implements PublicAccountService {
	private final static String CACHE_NAME = "pub";
	private final static String CACHE_PREFIX = "'pubid_'+";
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
	@Cacheable(key = CACHE_PREFIX + "#paccount", value = CACHE_NAME)
	public PublicAccount queryById(String paccount) {

        PublicAccount account = pubAccountRedisTemplate.get(paccount);
		
		if (account == null) {
			account = publicAccountRepository.get(paccount);
			pubAccountRedisTemplate.saveOrRemove(paccount, account);
		}
		
		return account;

	}

	@Override
	public void update(PublicAccount publicAccount) {
		publicAccountRepository.update(publicAccount);
		pubAccountRedisTemplate.save(publicAccount);
		boradcastOnInfoUpdated(publicAccount);
	}

	@Override
	public void save(PublicAccount publicAccount) {
		if (publicAccountRepository.queryByAccount(publicAccount.getAccount()) != null) {
			throw new IllegalArgumentException();
		}
		publicAccount.setTimestamp(String.valueOf(System.currentTimeMillis()));
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
	public void queryPage(PublicAccount publicAccount, Page page) {
		int count = this.publicAccountRepository.queryPublicAccountAmount(publicAccount);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}
		this.publicAccountRepository.queryPublicAccountList(publicAccount, page);
	}

	@Override
	public List<PublicAccount> queryListByUserAccount(String account) {
		List<PublicAccount> list = publicAccountRepository.queryListByUserAccount(account);
		for (PublicAccount pub : list) {
			Set<PublicMenu> menuList = new HashSet<PublicMenu>();
			menuList.addAll(publicMenuRepository.getPublicMenuList(pub.getAccount()));
			pub.setMenuList(menuList);
		}
		return list;
	}

	@Override
	public void updateLogo(String account,MultipartFile file) {
		fileManagerService.upload(file, ICON_DIR, account);
		boradcastOnLogoUpdated(account);
	}

	private void boradcastOnInfoUpdated(PublicAccount publicAccount) {

		messageRepository.deleteBySenderAndAction(publicAccount.getAccount(), Constants.MessageAction.ACTION_203);
		Message message = new Message();
		message.setSender(publicAccount.getAccount());
		message.setAction(Constants.MessageAction.ACTION_203);
		message.setContent(JSON.toJSONString(publicAccount));
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
		return publicAccountRepository.getAll();
	}

}
