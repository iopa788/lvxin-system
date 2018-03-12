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

import android.support.v4.app.NotificationCompat;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Friend.TABLE_NAME)
public class Friend extends MessageSource implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_user";

    private static final long serialVersionUID = 1L;
    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_0};
    @DatabaseField(id = true)
    public String account;
    @DatabaseField
    public String online = User.OFF_LINE;
    @DatabaseField
    public String name;
    @DatabaseField
    public String motto;
    @DatabaseField
    public String gender;
    @DatabaseField
    public String telephone;
    @DatabaseField
    public String email;
    @DatabaseField
    public String orgCode;
    @DatabaseField
    public Double longitude;
    @DatabaseField
    public Double latitude;
    @DatabaseField
    public String location;
    public char fristChar;

    public Friend() {
    }

    public Friend(String accont) {
        this.account = accont;
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
    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_HIGH;
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION;
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_green;
    }

    @Override
    public Friend clone() {
        Friend friend = new Friend();
        friend.account = account;
        friend.email = email;
        friend.name = name;
        friend.gender = gender;
        friend.latitude = latitude;
        friend.longitude = longitude;
        friend.location = location;
        friend.orgCode = orgCode;
        friend.telephone = telephone;
        friend.motto = motto;
        friend.fristChar = fristChar;
        return friend;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Friend) {
            return ((Friend) o).account.equals(account);
        }
        return false;
    }
}
