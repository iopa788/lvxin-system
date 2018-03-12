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

import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.model.User;

@Repository
public interface MomentRuleRepository extends JpaRepository<MomentRule, String> {

	@Query("select m.account from MomentRule as m where otherAccount =?1 and type = " + MomentRule.TYPE_1)
	List<String> queryLimitingMeList(String account);

	@Query("select m.otherAccount from MomentRule as m where account =?1 and type = " + MomentRule.TYPE_0)
	List<String> queryMeLimitingList(String account);

	@Query(nativeQuery = true, value = "select account from " + User.TABLE_NAME
			+ " WHERE account not in (SELECT otherAccount FROM " + MomentRule.TABLE_NAME
			+ " WHERE account = ?1 AND type = " + MomentRule.TYPE_0 + " UNION SELECT account FROM "
			+ MomentRule.TABLE_NAME + " WHERE otherAccount = ?1 AND type = " + MomentRule.TYPE_1
			+ "  ) AND account != ?1")
	List<String> queryFilteredList(String account);

	@Query("from MomentRule as m where account =?1")
	List<MomentRule> queryList(String account);

	@Query("select count(*) > 0  from MomentRule as m where account =?1 and otherAccount =?2 and type = "
			+ MomentRule.TYPE_0)
	boolean isBeLimiting(String account, String otherAccount);

	@Transactional
	@Modifying
	@Query("delete MomentRule where account =?1 and otherAccount=?2")
	void delete(String account, String otherAccount);
}
