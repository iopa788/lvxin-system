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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 朋友圈文章实体
 */
@Entity
@Table(name = "t_lvxin_article")
public class Article implements Serializable {

	public final static String FORMAT_TEXT_IMAGE = "0";//图文
	public final static String FORMAT_LINK = "1";//网址连接
	public final static String FORMAT_VIDEO = "2";//视频
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "gid", length = 32)
	private String gid;

	@Column(name = "account", length = 32, nullable = false)
	private String account;

	@Column(name = "content", length = 10000, nullable = false)
	private String content;

	@Column(name = "thumbnail", length = 2000)
	private String thumbnail;

	@Column(name = "link", length = 640)
	private String link;

	@Column(name = "type", length = 2)
	private String type;

	@Column(name = "timestamp", length = 13)
	private String timestamp;

	@Transient
	private List<Comment> commentList;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
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

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
