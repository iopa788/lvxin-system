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

import com.farsunset.lvxin.model.proto.UserProto;

@Entity
@Table(name = User.TABLE_NAME)
public class User implements Serializable {

	private transient static final long serialVersionUID = 4733464888738356502L;

	public transient static final String TABLE_NAME = "t_lvxin_user";

	public transient static final String OFF_LINE = "0";

	public transient static final String ON_LINE = "1";

	public transient static final String STATE_NORMAL = "0";

	public transient static final String STATE_DISABLE = "1";

	private transient static final String EMPTY = "";

	@Id
	@Column(name = "account", length = 32)
	private String account;

	@Column(name = "password", length = 64)
    private String password;

	@Column(name = "name", length = 16)
	private String name;

	@Column(name = "telephone", length = 20)
	private String telephone;

	@Column(name = "email", length = 50)
	private String email;

	@Column(name = "orgCode", length = 32)
	private String orgCode;

	@Column(name = "gender", length = 1)
	private String gender;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "motto", length = 200)
	private String motto;

	@Column(name = "inline", length = 1)
	private String online = OFF_LINE;

	@Column(name = "feature", length = 32)
	private String feature;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "location", length = 100)
	private String location;

	@Column(name = "state", length = 1)
	private String state;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getAccount() {
		return account;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isDisabled() {
		return STATE_DISABLE.equals(state);
	}

	@Override
	public String toString() {

		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("account:").append("'").append(account == null ? "" : account).append("'");
		buffer.append(",").append("name:").append("'").append(name == null ? "" : name).append("'");
		buffer.append(",").append("telephone:").append("'").append(telephone == null ? "" : telephone).append("'");
		buffer.append(",").append("orgCode:").append("'").append(orgCode == null ? "" : orgCode).append("'");
		buffer.append(",").append("gender:").append("'").append(gender == null ? "" : gender).append("'");
		buffer.append(",").append("email:").append("'").append(email).append("'");
		buffer.append("}");
		return buffer.toString();
	}

	public byte[] getProtobufBody() {
		UserProto.Model.Builder builder = UserProto.Model.newBuilder();

		builder.setAccount(account == null ? EMPTY : account);
		builder.setPassword(password == null ? EMPTY : password);
		builder.setName(name == null ? EMPTY : name);
		builder.setTelephone(telephone == null ? EMPTY : telephone);
		builder.setOrgCode(orgCode == null ? EMPTY : orgCode);
		builder.setEmail(email == null ? EMPTY : email);
		builder.setGrade(grade == null ? 0 : grade);
		builder.setGender(gender == null ? EMPTY : gender);
		builder.setFeature(feature == null ? EMPTY : feature);
		builder.setMotto(motto == null ? EMPTY : motto);
		builder.setOnline(online == null ? EMPTY : online);
		builder.setLongitude(longitude == null ? 0 : longitude);
		builder.setLatitude(latitude == null ? 0 : latitude);
		builder.setLocation(location == null ? EMPTY : location);
		builder.setState(state == null ? EMPTY : state);

		return builder.build().toByteArray();
	}
}
