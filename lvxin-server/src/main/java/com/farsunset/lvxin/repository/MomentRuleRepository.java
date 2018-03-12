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


import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.model.User;

@Repository
public class MomentRuleRepository extends HibernateBaseDao<MomentRule> {

	public List<String> queryLimitingMeList(String account) {

		String sql = "select m.account from MomentRule as m where otherAccount =:otherAccount and type = :type";
		Query<String> query = currentSession().createQuery(sql, String.class);
		query.setParameter("type", MomentRule.TYPE_1);
		query.setParameter("otherAccount", account);
		return query.getResultList();
	}

	public List<String> queryMeLimitingList(String account) {
		String sql = "select m.otherAccount from MomentRule as m where account =:account and type = :type";
		Query<String> query = currentSession().createQuery(sql, String.class);
		query.setParameter("type", MomentRule.TYPE_0);
		query.setParameter("account", account);
		return query.getResultList();
	}

	public List<String> queryFilteredList(String account){
		String sql = "select account from "+User.TABLE_NAME+" WHERE account not in (SELECT otherAccount FROM "+MomentRule.TABLE_NAME+" WHERE account = :account AND type = "+MomentRule.TYPE_0+" UNION SELECT account FROM "+MomentRule.TABLE_NAME+" WHERE otherAccount = :account AND type = "+MomentRule.TYPE_1+"  ) AND account != :account";
        Query<String> query = currentSession().createNativeQuery(sql,String.class);
        query.setParameter("account", account);
		return query.getResultList();
	}
	
	
	public List<MomentRule> queryList(String account) {

		String sql = "from MomentRule as m where account =:account";
		Query<MomentRule> query = currentSession().createQuery(sql, MomentRule.class);
		query.setParameter("account", account);
		return query.getResultList();

	}

	public boolean isBeLimiting(String account, String otherAccount) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MomentRule.class);
		criteria.add(Restrictions.eq("account", otherAccount));
		criteria.add(Restrictions.eq("otherAccount", account));
		criteria.add(Restrictions.eq("type", MomentRule.TYPE_0));
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString()) > 0;
	}

	public void delete(MomentRule model) {
		Session session = currentSession();
		String sql = "delete MomentRule where account =:account and otherAccount=:otherAccount ";
		Query<?> query = session.createQuery(sql);
		query.setParameter("account", model.getAccount());
		query.setParameter("otherAccount", model.getOtherAccount());
		query.executeUpdate();
	}
}
