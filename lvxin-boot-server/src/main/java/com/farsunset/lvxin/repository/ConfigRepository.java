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
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {

	@Query("from Config where domain= ?1 ")
	List<Config> queryListByDomain(String domain);

	@Query("select domain from Config ")
	List<String> queryDomainList();

	@Query("from Config where domain= ?1 and key = ?2")
	Config querySingle(String domain, String key);

	@Query("select count(*) from Config where domain= ?2 and key = ?3 and gid !=?1")
	long countExist(String gid, String domain, String key);

	List<Config> findAll(Specification<Config> sp);

	Page<Config> findAll(Specification<Config> sp, Pageable pageable);

}
