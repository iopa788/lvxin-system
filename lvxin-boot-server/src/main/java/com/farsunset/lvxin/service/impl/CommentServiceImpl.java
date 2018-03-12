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
package com.farsunset.lvxin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.repository.CommentRepository;
import com.farsunset.lvxin.service.CommentService;
import com.farsunset.lvxin.util.UUIDTools;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void add(Comment comment) {

		comment.setGid(UUIDTools.getUUID());
		comment.setTimestamp(System.currentTimeMillis());
		this.commentRepository.save(comment);

	}

	@Override
	public Page<Comment> queryByPage(Comment comment, Pageable pageable) {
		Specification<Comment> specification = new Specification<Comment>() {

			@Override
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(comment.getArticleId())) {
					predicatesList.add(builder.equal(root.get("articleId").as(String.class), comment.getArticleId()));
				}
				if (!StringUtils.isEmpty(comment.getType())) {
					predicatesList.add(builder.equal(root.get("type").as(String.class), comment.getType()));
				}
				if (!StringUtils.isEmpty(comment.getAccount())) {
					predicatesList.add(builder.equal(root.get("account").as(String.class), comment.getAccount()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};
		return this.commentRepository.findAll(specification, pageable);
	}

	@Override
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}

}
