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

import com.farsunset.lvxin.app.Constant;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 通知内容实体
 */
@DatabaseTable(tableName = Message.TABLE_NAME)

public class Message implements Serializable {

    public static final String TABLE_NAME = "t_lvxin_message";

    public static final long serialVersionUID = 1845362556725768545L;

    public static final String NAME = Message.class.getSimpleName();
    public static final String STATUS_NOT_READ = "0"; //自己还未阅读
    public static final String STATUS_READ = "1";//自己已经阅读
    public static final String STATUS_READ_OF_VOICE = "2";//自己已经阅读
    @DatabaseField(id = true)
    public String mid;
    @DatabaseField
    public String sender;
    @DatabaseField
    public String receiver;
    @DatabaseField
    public String action;
    @DatabaseField
    public String format;
    @DatabaseField
    public String title;
    @DatabaseField
    public String content;
    @DatabaseField
    public String extra;
    @DatabaseField
    public String status;
    @DatabaseField
    public long timestamp;
    //0: 未处理
    @DatabaseField
    public String handleStatus;
    String[] isNeedSoundTypes = new String[]{Constant.MessageAction.ACTION_0,
            Constant.MessageAction.ACTION_2,
            Constant.MessageAction.ACTION_3,
            Constant.MessageAction.ACTION_102,
            Constant.MessageAction.ACTION_103,
            Constant.MessageAction.ACTION_104,
            Constant.MessageAction.ACTION_105,
            Constant.MessageAction.ACTION_106,
            Constant.MessageAction.ACTION_107
    };

    //是否为动作消息，无需记录，无需显示
    public boolean isActionMessage() {
        return action.startsWith("9");
    }

    public boolean isNeedSound() {
        return Arrays.asList(isNeedSoundTypes).contains(action);
    }

    //是否为动作消息，无需记录，无需显示
    public boolean isNoNeedShow() {

        return action.startsWith("9") || action.startsWith("8");
    }

    //是否在对话列表显示阅读状态
    public boolean isNeedShowReadStatus() {
        return action.equals(Constant.MessageAction.ACTION_0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            return ((Message) o).mid != null && ((Message) o).mid.equals(mid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

}
