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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.cim.push.PubAccountMessagePusher;
import com.farsunset.lvxin.exception.IllegalExceedRootLimitException;
import com.farsunset.lvxin.exception.IllegalExceedSubLimitException;
import com.farsunset.lvxin.exception.IllegalExistCodeException;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.repository.PublicMenuRepository;
import com.farsunset.lvxin.service.PublicMenuService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.google.gson.Gson;

@Service
public class PublicMenuServiceImpl implements PublicMenuService {

	@Autowired
	private PublicMenuRepository publicMenuRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private PubAccountMessagePusher pubAccountMessagePusher;

	@Override
	public PublicMenu queryById(String gid) {
		return publicMenuRepository.findOne(gid);
	}

	@Override
	public void update(PublicMenu publicMenu) {

		PublicMenu hasMenu = publicMenuRepository.getSingleMenu(publicMenu.getAccount(), publicMenu.getCode());
		if (hasMenu != null && !publicMenu.getGid().equals(hasMenu.getGid())) {
			throw new IllegalExistCodeException();
		}
		publicMenuRepository.saveAndFlush(publicMenu);

		boradcastOnMenuUpdated(publicMenu.getAccount());
	}

	@Override
	public void add(PublicMenu publicMenu) {
		publicMenu.setGid(UUIDTools.getUUID());
		if (publicMenuRepository.hasExistThisCode(publicMenu.getAccount(), publicMenu.getCode())) {
			throw new IllegalExistCodeException();
		}
		if (publicMenu.getType() == PublicMenu.ACTION_ROOT
				&& publicMenuRepository.countRoot(publicMenu.getAccount()) >= Constants.MAX_PUB_ROOT_MENU) {
			throw new IllegalExceedRootLimitException();
		}
		if (publicMenu.getFid() != null
				&& publicMenuRepository.countChild(publicMenu.getFid()) >= Constants.MAX_PUB_SUB_MENU) {
			throw new IllegalExceedSubLimitException();
		}
		if (StringUtils.isBlank(publicMenu.getFid())) {
			publicMenu.setFid(null);
		}
		if (StringUtils.isBlank(publicMenu.getContent())) {
			publicMenu.setContent(null);
		}
		if (StringUtils.isBlank(publicMenu.getLink())) {
			publicMenu.setLink(null);
		}
		publicMenuRepository.save(publicMenu);

		boradcastOnMenuUpdated(publicMenu.getAccount());
	}

	@Override
	public void delete(PublicMenu publicMenu) {
		publicMenuRepository.deleteById(publicMenu.getGid());
		boradcastOnMenuUpdated(publicMenu.getAccount());
	}

	@Override
	public List<PublicMenu> queryList(String paccount) {
		return publicMenuRepository.getPublicMenuList(paccount);
	}

	@Override
	public List<PublicMenu> queryChildList(String fid) {
		return publicMenuRepository.queryChildList(fid);
	}

	@Override
	public List<PublicMenu> queryRootList(String account) {
		return publicMenuRepository.queryRootList(account);
	}

	/**
	 * 通知关注的用户，公账号菜单更新了
	 * 
	 * @param account
	 *            公众号账号
	 */
	private void boradcastOnMenuUpdated(String account) {

		messageRepository.deleteBySenderAndAction(account, Constants.MessageAction.ACTION_204);

		Message message = new Message();
		message.setSender(account);
		message.setFormat(Constants.MessageFormat.FORMAT_TEXT);
		message.setAction(Constants.MessageAction.ACTION_204);
		message.setContent(new Gson().toJson(publicMenuRepository.getPublicMenuList(account)));
		pubAccountMessagePusher.push(message, account);
	}

}
