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

import com.alibaba.fastjson.JSON;
import com.farsunset.lvxin.message.bean.ChatFile;
import com.farsunset.lvxin.message.bean.ChatMap;
import com.farsunset.lvxin.message.bean.ChatVoice;
import com.farsunset.lvxin.message.bean.SNSImage;
import com.farsunset.lvxin.message.bean.SNSVideo;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class MessageServiceImpl implements MessageService {
	protected final Logger logger = Logger.getLogger(MessageServiceImpl.class);

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	FileManagerServiceImpl fileManagerService;

	@Override
	public void delete(String gid) {
		Message msg = messageRepository.get(gid);
		if (msg != null) {
			messageRepository.delete(msg);
			deleteFile(msg);
		}
	}

	@Override
	public Message queryById(String gid) {
		// TODO Auto-generated method stub
		return messageRepository.get(gid);
	}

	@Override
	public List<Message> queryMessage(Message mo) {
		// TODO Auto-generated method stub
		return messageRepository.queryMessageList(mo);
	}

	@Override
	public List<Message> queryOffLineMessages(String receiver) {
		Message msg = new Message();
		msg.setReceiver(receiver);
		msg.setStatus(Message.STATUS_NOT_RECEIVED);
		return messageRepository.queryMessageList(msg);
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
		try {
			messageRepository.save(obj);
		}catch(Exception e) {
			logger.error("保存消息失败,可能是主键重复,mid:"+obj.getMid());
		}

	}

	@Override
	public void update(Message obj) {
		// TODO Auto-generated method stub
		messageRepository.update(obj);
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
	public void queryPage(Message mo, Page page) {
		int count = this.messageRepository.queryMessageAmount(mo);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return;
		}

		this.messageRepository.queryByPage(mo, page);
	}

	@Override
	public void save(List<Message> messageList) {
		// TODO Auto-generated method stub
		messageRepository.saveList(messageList);
	}

	@Override
	public void deleteByReceiverAndAction(String receiver, String type) {
		// TODO Auto-generated method stub
		messageRepository.deleteByReceiverAndAction(receiver, type);
	}

	@Override
	public void deleteBySenderAndAction(String sender, String type) {
		// TODO Auto-generated method stub
		messageRepository.deleteBySenderAndAction(sender, type);
	}

	@Override
	public void updateBatchReceived(String account) {
		// TODO Auto-generated method stub
		messageRepository.updateBatchReceived(account);
	}

	private void deleteFile(Message message) {
		if (Constants.MessageFormat.FORMAT_FILE.equals(message.getFormat())) {
			ChatFile chatFile = JSON.parseObject(message.getContent(), ChatFile.class);
			fileManagerService.delete(chatFile.key);
		}
		if (Constants.MessageFormat.FORMAT_MAP.equals(message.getFormat())) {
			ChatMap chatMap = JSON.parseObject(message.getContent(), ChatMap.class);
			fileManagerService.delete(chatMap.key);
		}
		if (Constants.MessageFormat.FORMAT_VOICE.equals(message.getFormat())) {
			ChatVoice chatVoice = JSON.parseObject(message.getContent(), ChatVoice.class);
			fileManagerService.delete(chatVoice.key);
		}
		if (Constants.MessageFormat.FORMAT_IMAGE.equals(message.getFormat())) {
			SNSImage image = JSON.parseObject(message.getContent(), SNSImage.class);
			fileManagerService.delete(image.image);
			fileManagerService.delete(image.thumbnail);
		}
		
		if (Constants.MessageFormat.FORMAT_VIDEO.equals(message.getFormat())) {
			SNSVideo video = JSON.parseObject(message.getContent(),SNSVideo.class);
			fileManagerService.delete(video.video);
			fileManagerService.delete(video.thumbnail);
		}
	}

	@Override
	public void deleteCommentRemind(String sender, String receiver, String commentId) {
		// TODO Auto-generated method stub
		messageRepository.deleteCommentRemind(sender, receiver, commentId);
	}

}
