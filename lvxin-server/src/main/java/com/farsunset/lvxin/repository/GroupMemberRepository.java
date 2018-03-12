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

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.User;

@Repository
public class GroupMemberRepository extends HibernateBaseDao<GroupMember> {

	public List<GroupMember> getMemberList(Long groupId) {

		String sql = "from GroupMember where groupId = :groupId order by host desc";
		Query<GroupMember> query = currentSession().createQuery(sql, GroupMember.class);
		query.setParameter("groupId", groupId);
		return query.getResultList();
	}

	public List<GroupMember> getMemberList(Long groupId, String founder) {

		String sql = "from GroupMember where groupId = :groupId and account != :founder  order by host desc";
		Query<GroupMember> query = currentSession().createQuery(sql, GroupMember.class);
		query.setParameter("groupId", groupId);
		query.setParameter("founder", founder);
		return query.getResultList();
	}

	public List<User> getUserList(Long groupId) {

		String sql = "select u from User as u,GroupMember as m where u.account = m.account and m.groupId = :groupId order by m.host desc";
		Query<User> query = currentSession().createQuery(sql, User.class);
		query.setParameter("groupId", groupId);
		return query.getResultList();
	}

	public int getCount(Long groupId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupMember.class);
		criteria.add(Restrictions.eq("groupId", groupId));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	public void remove(GroupMember groupMember) {
		Session session = currentSession();
		String sql = "delete GroupMember where account=? and groupId=?";
		Query<?> query = session.createQuery(sql);
		query.setParameter(0, groupMember.getAccount());
		query.setParameter(1, groupMember.getGroupId());
		query.executeUpdate();

	}

	public void clean(Long groupId) {
		Session session = currentSession();
		String sql = "delete GroupMember where  groupId=?";
		Query<?> query = session.createQuery(sql);
		query.setParameter(0, groupId);
		query.executeUpdate();

	}

	public List<String> getMemberAccountList(Long groupId) {
		String sql = "select m.account from  GroupMember as m where   m.groupId = :groupId";
		Query<String> query = currentSession().createQuery(sql, String.class);
		query.setParameter("groupId", groupId);
		return query.getResultList();
	}

	public void remove(List<String> list, Long groupId) {
		Session session = currentSession();
		String sql = "delete GroupMember where  groupId=? and account in (:accounts)";
		Query<?> query = session.createQuery(sql);
		query.setParameter(0, groupId);
		query.setParameterList("accounts", list);
		query.executeUpdate();

	}

	public boolean isExistThisMember(GroupMember groupMember) {

		DetachedCriteria criteria = DetachedCriteria.forClass(GroupMember.class);
		criteria.add(Restrictions.eq("account", groupMember.getAccount()));
		criteria.add(Restrictions.eq("groupId", groupMember.getGroupId()));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString()) > 0;
	}

}
