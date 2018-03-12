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
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;


/**
 * 公众账号
 */
@DatabaseTable(tableName = "t_lvxin_publicaccount")

public class PublicAccount extends MessageSource implements Serializable {


    public static final long serialVersionUID = 1L;

    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201};

    @DatabaseField(id = true)
    public String account;

    @DatabaseField
    public String description;


    @DatabaseField
    public String name;


    @DatabaseField
    public String power;

    @DatabaseField
    public String link;

    @DatabaseField
    public String greet;

    @DatabaseField
    public String apiUrl;

    public List<PublicMenu> menuList;

    @Override
    public String getSourceType() {

        return SOURCE_PUBACCOUNT;
    }

    @Override
    public String getWebIcon() {

        return FileURLBuilder.getPubAccountLogoUrl(account);
    }

    @Override
    public String getTitle() {

        return name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getId() {

        return account;
    }

    @Override
    public int getDefaultIconRID() {

        return R.drawable.icon_pubaccount;
    }

    @Override
    public String[] getMessageAction() {

        return MESSAGE_ACTION;
    }

    @Override
    public int getTitleColor() {
        return R.color.text_blue;
    }
}
