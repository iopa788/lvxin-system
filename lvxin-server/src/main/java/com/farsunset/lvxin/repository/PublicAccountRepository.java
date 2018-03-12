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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.web.jstl.Page;

@Repository
public class PublicAccountRepository extends HibernateBaseDao<PublicAccount> {

	public PublicAccount queryByAccount(String publicAccount) {

		String sql = "from PublicAccount  where account = :account";
		Query<PublicAccount> query = currentSession().createQuery(sql, PublicAccount.class);
		query.setParameter("account", publicAccount);

		List<PublicAccount> list = query.getResultList();
		return list == null || list.isEmpty() ? null : list.get(0);

	}

	public Page queryPublicAccountList(PublicAccount pAccount, Page page) {

		DetachedCriteria criteria = mapingParam(pAccount);
		page.setDataList(getHibernateTemplate().findByCriteria(criteria, page.getFirstResult(), page.size));

		return page;
	}

	public int queryPublicAccountAmount(PublicAccount model) {
		DetachedCriteria criteria = mapingParam(model);
		criteria.setProjection(Projections.rowCount());
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return Integer.valueOf(list.get(0).toString());
	}

	private DetachedCriteria mapingParam(PublicAccount model) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PublicAccount.class);
		if (StringUtils.isNotEmpty(model.getAccount())) {
			criteria.add(Restrictions.like("account", "%" + model.getAccount() + "%", MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(model.getName())) {
			criteria.add(Restrictions.like("name", "%" + model.getName() + "%", MatchMode.ANYWHERE));
		}

		return criteria;
	}

	public List<PublicAccount> queryListByUserAccount(String account) {

		String sql = "select a  from PublicAccount as a,Subscriber as s where a.account = s.publicAccount and s.account = :account";
		Query<PublicAccount> query = currentSession().createQuery(sql, PublicAccount.class);
		query.setParameter("account", account);

		return query.getResultList();
	}

}
