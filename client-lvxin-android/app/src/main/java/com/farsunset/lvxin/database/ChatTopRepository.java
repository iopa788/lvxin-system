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
package com.farsunset.lvxin.database;

import com.farsunset.lvxin.model.ChatTop;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatTopRepository extends BaseRepository<ChatTop, String> {

    private static ChatTopRepository manager = new ChatTopRepository();

    public static List<ChatTop> queryList() {
        try {
            return manager.databaseDao.queryBuilder().orderBy("sort", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<ChatTop>();
    }

    public static void updateSort(ChatTop top){
        top.sort = System.currentTimeMillis();
        manager.innerUpdate(top);
    }
    public static ChatTop setTop(Class<? extends MessageSource> sourceName, String sender) {
        ChatTop top = new ChatTop();
        top.sender = sender;
        top.sourceName = sourceName.getName();
        top.sort = System.currentTimeMillis();
        top.gid = StringUtils.getUUID();
        manager.innerSave(top);
        return top;
    }

    public static void delete(Class<? extends MessageSource> sourceName, String sender) {
        manager.innerExecuteSQL("delete from t_lvxin_chattop where sourceName = ? and sender = ?", new String[]{sourceName.getName(), sender});
    }
}
