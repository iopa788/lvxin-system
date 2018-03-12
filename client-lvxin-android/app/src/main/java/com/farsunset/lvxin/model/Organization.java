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


/**
 * 组织表
 */
@DatabaseTable(tableName = Organization.TABLE_NAME)
public class Organization extends MessageSource implements Serializable {

    public static final String TABLE_NAME = "t_lvxin_organization";
    private static final long serialVersionUID = 4733464888738356502L;
    @DatabaseField(id = true)
    public String code;

    @DatabaseField
    public String name;

    @DatabaseField
    public String parentCode;
    @DatabaseField
    public int sort;

    @Override
    public String getSourceType() {
        return null;
    }

    @Override
    public String getWebIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getMessageAction() {
        return new String[0];
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public int getDefaultIconRID() {
        return 0;
    }
}
