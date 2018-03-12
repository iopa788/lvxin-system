/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.farsunset.lvxin.bean;

import java.io.Serializable;

public class User implements Serializable {

    public transient static final long serialVersionUID = 4733464888738356502L;

    public transient static final String OFF_LINE = "0";

    public transient static final String ON_LINE = "1";

    public transient static final String GENDER_MAN = "1";

    public transient static final String GENDER_FEMALE = "0";

    public String account;
    public String password;
    public String name;
    public String gender;
    public String telephone;
    public String email;
    public String orgCode;
    public String motto;
    public Double longitude;
    public Double latitude;
    public String location;


    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User obj = (User) o;
            if (account != null && obj.account != null) {
                return account.equals(obj.account);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

}
