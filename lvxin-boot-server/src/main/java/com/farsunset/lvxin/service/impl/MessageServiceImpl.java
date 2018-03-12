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
import java.util.logging.Logger;

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

import com.farsunset.lvxin.message.bean.ChatFile;
import com.farsunset.lvxin.message.bean.ChatMap;
import com.farsunset.lvxin.message.bean.ChatVoice;
import com.farsunset.lvxin.message.bean.SNSImage;
import com.farsunset.lvxin.message.bean.SNSVideo;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.BatchAddRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.google.gson.Gson;

@Service
public class MessageServiceImpl implements MessageService {
	protected final Logger logger = Logger.getLogger(MessageServiceImpl.class.getName());

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private BatchAddRepository batchAddRepository;

	@Autowired
	FileManagerServiceImpl fileManagerService;

	@Override
	public void delete(String gid) {
		Message msg = queryById(gid);
		if (msg != null) {
			messageRepository.delete(msg);
			deleteFile(msg);
		}
	}

	@Override
	public Message queryById(String gid) {
		return messageRepository.findOne(gid);
	}

	@Override
	public List<Message> queryMessage(Message model) {
		Specification<Message> specification = new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(model.getSender())) {
					predicatesList.add(builder.equal(root.get("sender").as(String.class), model.getSender()));
				}
				if (StringUtils.isNotEmpty(model.getReceiver())) {
					predicatesList.add(builder.equal(root.get("receiver").as(String.class), model.getReceiver()));
				}
				if (StringUtils.isNotEmpty(model.getAction())) {
					predicatesList.add(builder.equal(root.get("action").as(String.class), model.getAction()));
				}
				if (StringUtils.isNotEmpty(model.getStatus())) {
					predicatesList.add(builder.equal(root.get("status").as(String.class), model.getStatus()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};
		return messageRepository.findAll(specification);
	}

	@Override
	public List<Message> queryOffLineMessages(String receiver) {
		Specification<Message> specification = new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				predicatesList.add(builder.equal(root.get("receiver").as(String.class), receiver));
				predicatesList.add(builder.equal(root.get("status").as(String.class), Message.STATUS_NOT_RECEIVED));
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.asc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};
		return messageRepository.findAll(specification);

	}

	@Override
	public void save(Message obj) {

		if (obj.getMid() == null) {
			obj.setMid(UUIDTools.getUUID());
		}
		// 9开头的为无需记录的动作消息
		if (obj.isActionMessage()) {
			return;
		}

		messageRepository.save(obj);

	}

	@Override
	public void update(Message obj) {
		// TODO Auto-generated method stub
		messageRepository.saveAndFlush(obj);
	}

	@Override
	public void updateStatus(String gid, String status) {
		messageRepository.updateStatus(gid, status);
	}

	@Override
	public void deleteObsoleted() {
		messageRepository.deleteObsoleted();

	}

	@Override
	public Page<Message> queryPage(Message model, Pageable pageable) {
		Specification<Message> specification = new Specification<Message>() {

			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(model.getSender())) {
					predicatesList.add(builder.equal(root.get("sender").as(String.class), model.getSender()));
				}
				if (StringUtils.isNotEmpty(model.getReceiver())) {
					predicatesList.add(builder.equal(root.get("receiver").as(String.class), model.getReceiver()));
				}
				if (StringUtils.isNotEmpty(model.getAction())) {
					Object[] actions = model.getAction().split(",");
					predicatesList.add(root.get("action").as(String.class).in(actions));
				}
				if (StringUtils.isNotEmpty(model.getStatus())) {
					predicatesList.add(builder.equal(root.get("status").as(String.class), model.getStatus()));
				}
				if (StringUtils.isNotEmpty(model.getFormat())) {
					predicatesList.add(builder.equal(root.get("format").as(String.class), model.getFormat()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};

		return this.messageRepository.findAll(specification, pageable);
	}

	@Override
	public void saveAll(List<Message> messageList) {
		batchAddRepository.saveMessage(messageList);
	}

	@Override
	public void deleteByReceiverAndAction(String receiver, String type) {
		messageRepository.deleteByReceiverAndAction(receiver, type);
	}

	@Override
	public void deleteBySenderAndAction(String sender, String type) {
		messageRepository.deleteBySenderAndAction(sender, type);
	}

	@Override
	public void updateBatchReceived(String account) {
		messageRepository.updateBatchReceived(account);
	}

	private void deleteFile(Message message) {
		if (Constants.MessageFormat.FORMAT_FILE.equals(message.getFormat())) {
			ChatFile chatFile = new Gson().fromJson(message.getContent(), ChatFile.class);
			fileManagerService.delete(chatFile.key);
		}
		if (Constants.MessageFormat.FORMAT_MAP.equals(message.getFormat())) {
			ChatMap chatMap = new Gson().fromJson(message.getContent(), ChatMap.class);
			fileManagerService.delete(chatMap.key);
		}
		if (Constants.MessageFormat.FORMAT_VOICE.equals(message.getFormat())) {
			ChatVoice chatVoice = new Gson().fromJson(message.getContent(), ChatVoice.class);
			fileManagerService.delete(chatVoice.key);
		}
		if (Constants.MessageFormat.FORMAT_IMAGE.equals(message.getFormat())) {
			SNSImage image = new Gson().fromJson(message.getContent(), SNSImage.class);
			fileManagerService.delete(image.image);
			fileManagerService.delete(image.thumbnail);
		}
		if (Constants.MessageFormat.FORMAT_VIDEO.equals(message.getFormat())) {
			SNSVideo video = new Gson().fromJson(message.getContent(), SNSVideo.class);
			fileManagerService.delete(video.video);
			fileManagerService.delete(video.thumbnail);
		}
	}

	@Override
	public void deleteCommentRemind(String sender, String receiver, String commentId) {
		messageRepository.deleteCommentRemind(sender, receiver, commentId);
	}

}
