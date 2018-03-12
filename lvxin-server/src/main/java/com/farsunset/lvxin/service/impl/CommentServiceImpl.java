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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.repository.CommentRepository;
import com.farsunset.lvxin.service.CommentService;
import com.farsunset.lvxin.util.UUIDTools;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void add(Comment comment) {

		comment.setGid(UUIDTools.getUUID());
		comment.setTimestamp(String.valueOf(System.currentTimeMillis()));
		this.commentRepository.save(comment);

	}

	@Override
	public Page queryByPage(Comment comment, Page page) {
		int count = this.commentRepository.queryAmount(comment);
		page.setCount(Integer.valueOf(count));
		if (page.getCount() == 0) {
			return page;
		}
		return this.commentRepository.queryByPage(comment, page);
	}

	@Override
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}

}
