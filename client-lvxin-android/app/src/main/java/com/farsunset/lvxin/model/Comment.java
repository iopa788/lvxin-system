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

@DatabaseTable(tableName = "t_lvxin_comment")
public class Comment implements Serializable {


    public static final String TYPE_0 = "0";//回复文章
    public static final String TYPE_1 = "1";//回复评论
    public static final String TYPE_2 = "2";//点赞
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public String gid;

    @DatabaseField
    public String articleId;

    @DatabaseField
    public String account;

    @DatabaseField
    public String content;

    @DatabaseField
    public String type = TYPE_0;

    @DatabaseField
    public String timestamp;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comment) {
            return ((Comment) o).gid.equals(gid);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
