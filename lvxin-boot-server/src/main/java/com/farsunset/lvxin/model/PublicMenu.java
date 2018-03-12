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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公众账号菜单
 */
@Entity
@Table(name = "t_lvxin_publicmenu")
public class PublicMenu implements Serializable {

	public transient final static int ACTION_ROOT = 0;
	public transient final static int ACTION_WEB = 1;
	public transient final static int ACTION_API = 2;
	public transient final static int ACTION_TEXT = 3;
	private transient static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gid", length = 32)
	private String gid;

	@Column(name = "fid", length = 32)
	private String fid;
	@Column(name = "account", length = 32)
	private String account;
	@Column(name = "name", length = 20)
	private String name;

	@Column(name = "code", length = 32)
	private String code;
	@Column(name = "link", length = 640)
	private String link;

	@Column(name = "type", length = 2)
	private int type;

	@Column(name = "content", length = 1024)
	private String content;
	@Column(name = "sort")
	private int sort;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PublicMenu) {
			PublicMenu target = (PublicMenu) o;
			return Objects.equals(target.gid, gid);
		}
		return false;
	}

}
