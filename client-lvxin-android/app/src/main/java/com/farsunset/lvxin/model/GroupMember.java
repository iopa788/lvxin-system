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
package com.farsunset.lvxin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
@DatabaseTable(tableName = GroupMember.TABLE_NAME)
public class GroupMember implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_groupMember";
    public static final long serialVersionUID = 4733464888738356502L;
    public final static String RULE_FOUNDER = "1";
    public final static String RULE_NORMAL = "0";
    @DatabaseField(id = true)
    public String gid;

    @DatabaseField
    public String groupId;

    @DatabaseField
    public String account;

    @DatabaseField
    public String host;

    public String name;

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GroupMember) {
            GroupMember member = (GroupMember) o;
            return member.groupId.equals(groupId) && member.account.equals(account);
        }
        return false;
    }

}
