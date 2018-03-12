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

import com.farsunset.lvxin.model.PublicMenu;

@Repository
public interface PublicMenuRepository extends JpaRepository<PublicMenu, String> {

	@Transactional
	@Modifying
	@Query("delete from PublicMenu  where  gid  = ?1 or fid = ?1 ")
	void deleteById(String gid);

	@Query("from PublicMenu where account =?1 order by sort")
	List<PublicMenu> getPublicMenuList(String account);

	@Query("from PublicMenu where account = ?1 and code =?2")
	PublicMenu getSingleMenu(String account, String code);

	@Query("select count(*) > 0 from PublicMenu where account = ?1 and code =?2")
	boolean hasExistThisCode(String account, String code);

	@Query("from PublicMenu where fid =?1 order by sort")
	List<PublicMenu> queryChildList(String fid);

	@Query("from PublicMenu where account =?1 and fid = null order by sort")
	List<PublicMenu> queryRootList(String account);

	@Transactional
	@Modifying
	@Query("update PublicMenu set sort = ?2   where  gid  = ?1")
	void updateSort(String gid, int sort);

	@Query("select count(*)  from PublicMenu where  fid =?1")
	int countChild(String fid);

	@Query("select count(*)  from PublicMenu where account = ?1 and type = " + PublicMenu.ACTION_ROOT)
	int countRoot(String account);
}
