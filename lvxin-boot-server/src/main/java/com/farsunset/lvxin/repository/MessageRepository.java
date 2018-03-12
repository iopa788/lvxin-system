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
package com.farsunset.lvxin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.util.Constants;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

	Page<Message> findAll(Specification<Message> sp, Pageable pageable);

	List<Message> findAll(Specification<Message> sp);

	@Transactional
	@Modifying
	@Query("update Message set  status=?2  where mid= ?1")
	void updateStatus(String mid, String status);

	@Transactional
	@Modifying
	@Query("update Message set  status='" + Message.STATUS_RECEIVED + "' where status='" + Message.STATUS_NOT_RECEIVED
			+ "' and receiver=?1")
	void updateBatchReceived(String account);

	@Transactional
	@Modifying
	@Query("delete Message where (action like '1__' or action like '8__' or action = '"
			+ Constants.MessageAction.ACTION_444 + "') and status ='" + Message.STATUS_NOT_RECEIVED + "'")
	void deleteObsoleted();

	@Transactional
	@Modifying
	@Query("delete from Message where ( sender=?2 and action = '" + Constants.MessageAction.ACTION_3
			+ "' and receiver in( ?1 )) or ( sender in(?1) and action ='" + Constants.MessageAction.ACTION_1
			+ "' and receiver =?2)")
	void deleteGroupMessage(List<String> list, String groupId);

	@Transactional
	@Modifying
	@Query("delete from Message where ( sender=?2 and action = '" + Constants.MessageAction.ACTION_3
			+ "' and receiver = ?1) or (sender = ?1 and action ='" + Constants.MessageAction.ACTION_1
			+ "' and receiver = ?2)")
	void deleteGroupMessage(String account, String groupId);

	
	@Transactional
	@Modifying
	@Query("delete from Message where ( sender=?1 and action = '" + Constants.MessageAction.ACTION_3
			+ "') or ( action ='" + Constants.MessageAction.ACTION_3 + "' and receiver =?1)")
	void deleteGroupMessage(String groupId);

	@Transactional
	@Modifying
	@Query("delete from Message where (sender=?1 and receiver =?2) or  (sender=?2 and receiver =?1) and action ='"
			+ Constants.MessageAction.ACTION_700 + "'")
	void deleteBottleMessage(String sender, String receiver);

	@Transactional
	@Modifying
	@Query("delete from Message where  receiver = ?1  and action = ?2")
	void deleteByReceiverAndAction(String receiver, String action);

	@Transactional
	@Modifying
	@Query("delete from Message where  sender = ?1  and action = ?2")
	void deleteBySenderAndAction(String sender, String action);

	@Transactional
	@Modifying
	@Query("delete from Message where  content = ?2 and sender = ?1  and action = ?3")
	void delete(String sender, String content, String action);

	@Transactional
	@Modifying
	@Query("delete from Message where  content = ?1 and action = ?2")
	void deleteByContentAndAction(String content, String action);

	@Transactional
	@Modifying
	@Query("delete from Message where  extra = ?1 and action = ?2")
	void deleteByExtraAndAction(String extra, String action);

	@Transactional
	@Modifying
	@Query("delete from Message where  sender = ?1  and receiver = ?2 and extra = ?3 and action in ('"
			+ Constants.MessageAction.ACTION_801 + "','" + Constants.MessageAction.ACTION_802 + "')")

	void deleteCommentRemind(String sender, String receiver, String commentId);

}
