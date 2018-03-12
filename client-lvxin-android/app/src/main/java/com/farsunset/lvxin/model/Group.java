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
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "t_lvxin_group")
public class Group extends MessageSource implements Serializable {

    public static final long serialVersionUID = 4733464888738356502L;
    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_1, Constant.MessageAction.ACTION_3};

    @DatabaseField(id = true)
    public String groupId;

    @DatabaseField
    public String name;

    @DatabaseField
    public String summary;

    @DatabaseField
    public String category;

    @DatabaseField
    public String founder;

    public List<GroupMember> memberList;

    public Group() {
    }

    public Group(String groupId) {
        this.groupId = groupId;
    }

    public void addMember(GroupMember c) {
        if (memberList == null) {
            memberList = new ArrayList<GroupMember>();
        }
        memberList.add(c);
    }


    @Override
    public String getWebIcon() {

        return FileURLBuilder.getGroupIconUrl(groupId);
    }

    @Override
    public String getTitle() {

        return name;
    }

    @Override
    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_HIGH;
    }

    @Override
    public int getDefaultIconRID() {
        return R.drawable.logo_group_normal;
    }

    @Override
    public String getId() {

        return groupId;
    }

    public int getMemberCount() {
        return memberList == null ? 0 : memberList.size();
    }

    @Override
    public String getSourceType() {
        return SOURCE_GROUP;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION;
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }
}
