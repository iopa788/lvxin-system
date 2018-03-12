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
package com.farsunset.lvxin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.farsunset.lvxin.model.Message;

public interface MessageService {

	/**
	 * 保存通知信息
	 * 
	 * @param message
	 */
	void save(Message message);

	void saveAll(List<Message> messageList);

	/**
	 * 修改通知信息
	 * 
	 * @param message
	 */
	void update(Message message);

	/**
	 * 修改通知信息状态
	 * 
	 * @param MessageMO
	 */
	void updateStatus(String gid, String status);

	/**
	 * 删除通知
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 删除通知
	 * 
	 * @param id
	 */
	void deleteBySenderAndAction(String sender, String action);

	/**
	 * 删除通知
	 * 
	 * @param id
	 */
	void deleteByReceiverAndAction(String receiver, String action);

	/**
	 * 删除评论 点赞相关通知
	 * 
	 * @param commentId
	 *            评论的ID 存与extra字段
	 */
	void deleteCommentRemind(String sender, String receiver, String commentId);

	/**
	 * 查看通知
	 * 
	 * @param id
	 * @return MessageMO
	 */
	Message queryById(String gid);

	/**
	 * 根据条件查询通知列表
	 * 
	 * @param mo
	 *            查询条件
	 * @return List<MessageMO>
	 */
	List<Message> queryMessage(Message mo);

	/**
	 * 查询用户未正常接收的通知
	 * 
	 * @param mo
	 *            查询条件
	 * @return List<MessageMO>
	 */
	List<Message> queryOffLineMessages(String receiver);

	/**
	 * 批量删除
	 * 
	 * @param MessageMOs
	 */
	void deleteObsoleted();

	Page<Message> queryPage(Message mo, Pageable pageable);

	void updateBatchReceived(String account);

}
