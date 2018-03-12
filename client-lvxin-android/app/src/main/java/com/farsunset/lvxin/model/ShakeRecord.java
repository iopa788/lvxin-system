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

import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_shakerecord")

public class ShakeRecord extends MessageSource implements Serializable {

    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public String account;


    @DatabaseField
    public String name;

    @DatabaseField
    public String gender;


    @DatabaseField
    public Double longitude;

    @DatabaseField
    public Double latitude;

    public static ShakeRecord toShakeRecord(Friend f) {
        ShakeRecord s = new ShakeRecord();
        s.account = f.account;
        s.name = f.name;
        s.gender = f.gender;
        s.latitude = f.latitude;
        s.longitude = f.longitude;
        return s;
    }

    @Override
    public String getWebIcon() {


        return FileURLBuilder.getUserIconUrl(account);
    }

    @Override
    public String getTitle() {

        return name;
    }

    @Override
    public int getDefaultIconRID() {
        return R.drawable.icon_def_head;
    }

    @Override
    public String getId() {
        return account;
    }

    @Override
    public String getSourceType() {

        return SOURCE_USER;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShakeRecord) {

            return ((ShakeRecord) o).account.equals(account);
        }
        return false;
    }

    @Override
    public String[] getMessageAction() {
        return new String[]{};
    }

    public Friend toFriend() {
        Friend f = new Friend();
        f.account = account;
        f.name = name;
        f.gender = gender;
        f.latitude = latitude;
        f.longitude = longitude;
        return f;
    }
}
