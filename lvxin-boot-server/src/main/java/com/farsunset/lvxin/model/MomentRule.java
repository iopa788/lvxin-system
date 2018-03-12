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
@Table(name = MomentRule.TABLE_NAME)
public class MomentRule implements Serializable {
	public transient static final String TABLE_NAME = "t_lvxin_momentrule";
	public transient static final int TYPE_0 = 0;// 不让对方看到
	public transient static final int TYPE_1 = 1;// 不看对方的内容
	private transient static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gid", length = 32)
	private String gid;
	@Column(name = "account", length = 32)
	private String account;
	@Column(name = "otherAccount", length = 32)
	private String otherAccount;

	@Column(name = "type", length = 1)
	private int type;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOtherAccount() {
		return otherAccount;
	}

	public void setOtherAccount(String otherAccount) {
		this.otherAccount = otherAccount;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
