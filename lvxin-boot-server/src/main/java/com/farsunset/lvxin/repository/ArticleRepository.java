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
import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.model.MomentRule;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

	Page<Article> findAll(Specification<Article> sp, Pageable pageable);

	@Transactional
	@Modifying
	@Query("delete  from Article   where  account  = ?1")
	void deleteByAccount(String account);

	@Query("from Article   where   account=?1 ")
	List<Article> queryList(String account);

	@Query("select a  from Article as a where a.account not in (select account from MomentRule  where otherAccount = ?1 and account = a.account and type = "
			+ MomentRule.TYPE_0
			+ ") and a.account not in (select otherAccount from MomentRule  where account =?1 and otherAccount = a.account and type = "
			+ MomentRule.TYPE_1 + ") order by a.timestamp desc")
	Page<Article> queryRelevantByPage(String account, Pageable pageable);
}
