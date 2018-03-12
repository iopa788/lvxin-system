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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.farsunset.lvxin.model.proto.GroupProto;

/**
 * 群组
 */
@Entity
@Table(name = "t_lvxin_group")
public class Group implements Serializable {

	private transient static final long serialVersionUID = 4733464888738356502L;
	private transient static final String EMPTY = "";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "groupId")
	private Long groupId;

	@Column(name = "name", length = 16)
	private String name;

	@Column(name = "summary", length = 200)
	private String summary;

	@Column(name = "category", length = 16)
	private String category;

	@Column(name = "founder", length = 32)
	private String founder;

	@Transient
	private Set<GroupMember> memberList;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public Set<GroupMember> getMemberList() {
		return memberList;
	}

	public void setMemberList(Set<GroupMember> memberList) {
		this.memberList = memberList;
	}

	public byte[] getProtobufBody() {
		GroupProto.Model.Builder builder = GroupProto.Model.newBuilder();

		builder.setGroupId(groupId);
		builder.setName(name == null ? EMPTY : name);
		builder.setFounder(founder == null ? EMPTY : founder);
		builder.setCategory(category == null ? EMPTY : category);
		builder.setSummary(summary == null ? EMPTY : summary);

		return builder.build().toByteArray();
	}
}
