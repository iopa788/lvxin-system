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
 * 组织表
 */
@Entity
@Table(name = Organization.TABLE_NAME)
public class Organization implements Serializable {

	public transient static final String TABLE_NAME = "t_lvxin_organization";
	private transient static final long serialVersionUID = 4733464888738356502L;
	@Id
	@Column(name = "code", length = 32)
	private String code;

	@Column(name = "name", length = 32)
	private String name;

	@Column(name = "parentCode", length = 32)
	private String parentCode;

	@Column(name = "sort", length = 2)
	private int sort;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("code:").append("'").append(code == null ? "" : code).append("'");
		buffer.append(",").append("name:").append("'").append(name == null ? "" : name).append("'");
		buffer.append(",").append("parentCode:").append("'").append(parentCode == null ? "" : parentCode).append("'");
		buffer.append(",").append("sort:").append("'").append(sort).append("'");
		buffer.append("}");
		return buffer.toString();
	}

}
