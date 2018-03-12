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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import com.farsunset.lvxin.model.SNSShake;

@SuppressWarnings("unchecked")
@Repository
public class SNSShakeRepository extends HibernateBaseDao<SNSShake>

{

	public SNSShake queryNewShake(String account, long timestamp) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SNSShake.class);
		criteria.add(Restrictions.ne("account", account));
		criteria.add(Restrictions.gt("timestamp", timestamp));
		criteria.addOrder(Order.desc("timestamp"));
		List<SNSShake> list = (List<SNSShake>) getHibernateTemplate().findByCriteria(criteria, 0, 1);
		return (list == null || list.isEmpty()) ? null : list.get(0);
	}

	public void deleteObsoleted() {
		Session session = currentSession();
		long timestamp = System.currentTimeMillis() - 60 * 1000L;// 1小时之前的记录;
		String sql = "delete   SNSShake   where timestamp <=:timestamp";
		Query<?> query = session.createQuery(sql);
		query.setParameter("timestamp", timestamp);
		query.executeUpdate();
	}

}
