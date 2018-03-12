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


import com.farsunset.lvxin.model.Organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationRepository extends BaseRepository<Organization, String> {
    public final static String DATABASE_NAME = "base.db";
    private static OrganizationRepository manager = new OrganizationRepository();

    /**
     * 组织为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static  List<Organization> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public  static List<Organization> queryList(String code) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("parentCode", code).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static  List<Organization> queryRootList() {

        try {
            return manager.databaseDao.queryBuilder().where().isNull("parentCode").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static  String queryOrgName(String code) {
        if (code == null) {
            return null;
        }
        Organization target = manager.innerQueryById(code);
        return target == null ? null : target.name;
    }

    public static  void saveAll(final List<Organization> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
    }

    public static Organization queryOne(String code) {
       return manager.innerQueryById(code);
    }
}
