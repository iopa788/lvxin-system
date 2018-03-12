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

import com.farsunset.lvxin.util.UUIDTools;

//漂流瓶
@Entity
@Table(name = "t_lvxin_bottle")
public class Bottle implements Serializable {

	private static final long serialVersionUID = 1845362556725768545L;

	public static final int STATUS_NOT_RECEIVED = 0;// 未接受
	public static final int STATUS_RECEIVED = 1;// 已接收

	public Bottle() {

		gid = UUIDTools.getUUID();
		timestamp = String.valueOf(System.currentTimeMillis());
		status = STATUS_NOT_RECEIVED;
	}

	public Bottle(Object obj) {
	}

	@Id
	@Column(name = "gid", length = 32)
	private String gid;

	@Column(name = "sender", length = 64)
	private String sender;

	@Column(name = "receiver", length = 64)
	private String receiver;

	@Column(name = "type", length = 1)
	private Integer type;

	@Column(name = "content", length = 2000)
	private String content;

	@Column(name = "region", length = 20)
	private String region;

	@Column(name = "length", length = 3)
	private String length;

	@Column(name = "status", length = 2)
	private Integer status;

	@Column(name = "timestamp", length = 13)
	private String timestamp;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
