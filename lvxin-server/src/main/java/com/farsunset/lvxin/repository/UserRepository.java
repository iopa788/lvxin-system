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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.exception.IllegalUserAccountException;
import com.farsunset.lvxin.model.User;
import com.farsunset.lvxin.web.jstl.Page;

@SuppressWarnings("unchecked")
@Repository
public class UserRepository extends HibernateBaseDao<User> {

	final String INSERT_SQL = "insert into " + User.TABLE_NAME
			+ "(account,name,gender,telephone,email,orgCode,password,state) values(?,?,?,?,?,?,?,?)";


	public Page queryUserList(User user, Page page) {

		DetachedCriteria criteria = mapingParam(user);
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));

		return page;
	}

	public int queryUserAmount(User model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}


	public int countOrganizationMember(String code) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("orgCode",code));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}
	
	private DetachedCriteria mapingParam(User model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		if (StringUtils.isNotEmpty(model.getAccount())) {
			criteria.add(Restrictions.like("account", model.getAccount(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getName())) {
			criteria.add(Restrictions.like("name", model.getName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getTelephone())) {
			criteria.add(Restrictions.like("telephone", model.getTelephone(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getEmail())) {
			criteria.add(Restrictions.like("email", model.getEmail(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getGender())) {
			criteria.add(Restrictions.eq("gender", model.getGender()));
		}
		if (StringUtils.isNotEmpty(model.getOrgCode())) {
			criteria.add(Restrictions.eq("orgCode", model.getOrgCode()));
		}
		if (StringUtils.isNotEmpty(model.getState())) {
			criteria.add(Restrictions.eq("state", model.getState()));
		}
		return criteria;
	}

	public List<String> getUserAccounts() {

		String sql = "select u.account from User as u ";
		return (List<String>) this.getHibernateTemplate().find(sql);
	}

	public List<User> queryAllUsers() {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);

		return (List<User>) getHibernateTemplate().findByCriteria(criteria);
	}

	public List<User> queryNearbyList(String account,String gender) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("online", User.ON_LINE));
		criteria.add(Restrictions.ne("account",account));
		if (StringUtils.isNotEmpty(gender)) {
			criteria.add(Restrictions.eq("gender", gender));
		}
		criteria.add(Restrictions.isNotNull("latitude"));
		criteria.add(Restrictions.isNotNull("longitude"));
		return (List<User>) getHibernateTemplate().findByCriteria(criteria);
	}

	public int saveBatch(final List<User> list) {
		final Session session = currentSession();
		return session.doReturningWork(new ReturningWork<Integer>() {
			@Override
			public Integer execute(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(INSERT_SQL);
				con.setAutoCommit(false);
				try {
					int index = 0;
					for (User user : list) {
						int i = 1;
						statement.setString(i++, user.getAccount());
						statement.setString(i++, user.getName());
						statement.setString(i++, user.getGender());
						statement.setString(i++, user.getTelephone());
						statement.setString(i++, user.getEmail());
						statement.setString(i++, user.getOrgCode());
						statement.setString(i++, user.getPassword());
						statement.setString(i++, User.STATE_NORMAL);
						statement.addBatch();
						if (index % 20 == 0) {
							statement.executeBatch();

						}
					}
					statement.executeBatch();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
					throw new IllegalUserAccountException();
				} finally {
					con.commit();
				}
				return list.size();
			}
		});
	}


}
