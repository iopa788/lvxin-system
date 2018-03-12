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
 * 公众账号菜单
 */
@DatabaseTable(tableName = PublicMenu.TABLE_NAME)
public class PublicMenu implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_publicmenu";

    public final static String ACTION_ROOT = "0";
    public final static String ACTION_WEB = "2";
    public final static String ACTION_API = "1";
    public final static String FORMAT_TEXT = "3";
    public static final long serialVersionUID = 1L;


    @DatabaseField(id = true)
    public String gid;


    @DatabaseField
    public String fid;

    @DatabaseField
    public String account;

    @DatabaseField
    public String name;


    @DatabaseField
    public String code;

    @DatabaseField
    public String link;

    /*
    0:一级菜单
    1:调用接口
    2:网页地址
    3:回复文字,回复菜单的 content字段内容
    */
    @DatabaseField
    public String type;


    @DatabaseField
    public String content;

    @DatabaseField
    public int sort;

    public boolean hasSubMenu() {
        return ACTION_ROOT.equals(type);
    }

    public boolean isRootMenu() {
        return fid == null;
    }

    public boolean isApiMenu() {
        return ACTION_API.equals(type);
    }

    public boolean isWebMenu() {
        return ACTION_WEB.equals(type);
    }

    public boolean isTextMenu() {
        return FORMAT_TEXT.equals(type);
    }
}
