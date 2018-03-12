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

import com.farsunset.lvxin.model.MomentRule;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class MomentRuleRepository extends BaseRepository<MomentRule, String> {
    private static MomentRuleRepository manager = new MomentRuleRepository();

    public static void add(MomentRule model) {
        manager.createOrUpdate(model);
    }
    public static void saveAll(List<MomentRule> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
    }

    public static MomentRule query(String account, String otherAccount, String type) {
        try {
            List<MomentRule> list = manager.databaseDao.queryBuilder().where()
                    .eq("account", account)
                    .and().eq("otherAccount", otherAccount).and()
                    .eq("type", type).query();

            return list == null || list.isEmpty() ? null : list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void remove(MomentRule model) {
        try {
            DeleteBuilder builder = manager.databaseDao.deleteBuilder();
            builder.where().eq("account", model.account);
            builder.where().eq("otherAccount", model.otherAccount);
            builder.where().eq("type", model.type);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
