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

@Entity
@Table(name = "t_lvxin_config")

/**
 * 系统配置
 */
public class Config implements Serializable {

	private transient static final long serialVersionUID = 1L;
	@Id
	@Column(name = "gid", length = 32)
	private String gid;

	@Column(name = "ikey", length = 32)
	private String key;

	@Column(name = "value", length = 2000)
	private String value;

	@Column(name = "domain", length = 32)
	private String domain;

	@Column(name = "description", length = 200)
	private String description;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("gid:").append("'").append(gid == null ? "" : gid).append("'");
		buffer.append(",").append("domain:").append("'").append(domain == null ? "" : domain).append("'");
		buffer.append(",").append("key:").append("'").append(key == null ? "" : key).append("'");
		buffer.append(",").append("value:").append("'").append(value == null ? "" : value.replaceAll("\n", ""))
				.append("'");
		buffer.append(",").append("description:").append("'")
				.append(description == null ? "" : description.replaceAll("\n", "")).append("'");
		buffer.append("}");
		return buffer.toString();
	}

}
