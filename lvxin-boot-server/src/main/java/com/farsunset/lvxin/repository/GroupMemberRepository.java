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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String> {

	@Query("from GroupMember where groupId = ?1 order by host desc")
	List<GroupMember> queryMemberList(Long groupId);

	@Query("from GroupMember where groupId = ?1 and account != ?2  order by host desc")
	List<GroupMember> queryMemberList(Long groupId, String founder);

	@Query("select u from User as u,GroupMember as m where  u.account = m.account and groupId = ?1 order by host desc")
	List<User> queryMemberUserList(Long groupId);

	int countByGroupId(Long groupId);

	@Transactional
	@Modifying
	@Query("delete GroupMember where account=?1 and groupId=?2")
	void remove(String account, Long groupId);

	@Transactional
	@Modifying
	@Query("delete GroupMember where  groupId=?1")
	void clean(Long groupId);

	@Query("select m.account from  GroupMember as m where   m.groupId = ?1")
	List<String> getAccountList(Long groupId);

	@Transactional
	@Modifying
	@Query("delete GroupMember where  groupId=?1 and account in (?2)")
	void remove(Long groupId, List<String> list);

}
