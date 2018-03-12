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

import com.farsunset.lvxin.model.PublicMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicMenuRepository extends BaseRepository<PublicMenu, String> {

    private static PublicMenuRepository manager = new PublicMenuRepository();

    public static List<PublicMenu> queryPublicMenuList(String account) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("account", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<PublicMenu>();
    }

    public static PublicMenu queryById(String gid) {
        return manager.innerQueryById(gid);
    }

    public static void savePublicMenus(List<PublicMenu> list) {
        if (list != null && !list.isEmpty()) {
            deleteByAccount(list.get(0).account);
            manager.innerSaveAll(list);
        }

    }

    public static void deleteByAccount(String account) {

        String sql = "delete from " + PublicMenu.TABLE_NAME + " where account = ?";
        manager.innerExecuteSQL(sql, new String[]{account});

    }
}
