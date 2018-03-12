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
package com.farsunset.lvxin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 评论实体
 */
@Entity
@Table(name = "t_lvxin_comment")
public class Comment implements Serializable {

	private transient static final long serialVersionUID = 1L;
	public transient static final String ACTION_0 = "0";// 回复文章
	public transient static final String ACTION_1 = "1";// 回复评论
	public transient static final String ACTION_2 = "2";// 点赞
	public transient static final String ACTION_FEED_BACK = "99";// 回复评论
	@Id
	@Column(name = "gid", length = 32)
	private String gid;
	@Column(name = "articleId", length = 32)
	private String articleId;
	@Column(name = "account", length = 32)
	private String account;
	@Column(name = "content", length = 320)
	private String content;
	@Column(name = "type", length = 2)
	private String type;
	@Column(name = "timestamp", length = 13)
	private long timestamp;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
