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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.farsunset.lvxin.model.proto.PubAccountProto;

/**
 * 公众账号
 */
@Entity
@Table(name = "t_lvxin_publicaccount")
public class PublicAccount implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final String EMPTY = "";

	@Id
	@Column(name = "account", length = 32)
	private String account;

	@Column(name = "description", length = 320)
	private String description;

	@Column(name = "name", length = 20)
	private String name;
	 
	@Column(name = "link", length = 640)
	private String link;

	@Column(name = "greet", length = 320)
	private String greet;

	@Column(name = "apiUrl", length = 100)
	private String apiUrl;

	@Column(name = "timestamp", length = 13)
	private String timestamp;

	@Column(name = "status", length = 2)
	private String status;

	@Transient
	private Set<PublicMenu> menuList;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getGreet() {
		return greet;
	}

	public void setGreet(String greet) {
		this.greet = greet;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public Set<PublicMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(Set<PublicMenu> menuList) {
		this.menuList = menuList;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	@JSONField(serialize=false)
	public byte[] getProtobufBody() {
		PubAccountProto.Model.Builder builder = PubAccountProto.Model.newBuilder();
		 
		builder.setAccount(account==null?EMPTY:account);
		builder.setName(name==null?EMPTY:name);
		builder.setDescription(description==null?EMPTY:description);
		builder.setApiUrl(apiUrl==null?EMPTY:apiUrl);
		builder.setLink(link==null?EMPTY:link);
		builder.setGreet(greet==null?EMPTY:greet);
		builder.setStatus(status==null?EMPTY:status);
		 
		return builder.build().toByteArray();
	}
}
