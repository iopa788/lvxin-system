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

import com.farsunset.lvxin.model.Subscriber;
import com.farsunset.lvxin.model.User;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {

	@Transactional
	@Modifying
	@Query("delete Subscriber where account= ?1 and publicAccount= ?2")
	void delete(String account, String publicAccount);

	@Query("select u  from User as u,Subscriber as s where u.account = s.account and s.publicAccount = ?1 order by s.timestamp desc")
	List<User> getSubscriberList(String publicAccount);

	@Query("select account from Subscriber where publicAccount = ?1")
	List<String> getSubscriberAccounts(String publicAccount);

}
