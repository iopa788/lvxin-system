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

import com.farsunset.lvxin.model.Bottle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BottleRepository extends BaseRepository<Bottle, String> {
    private static BottleRepository manager = new BottleRepository();

    public static List<Bottle> queryBottleList() {
        List<Bottle> list = new ArrayList<Bottle>();
        try {
            list = manager.databaseDao.queryBuilder().orderBy("timestamp", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean hasExist(String gid) {

        return manager.innerQueryById(gid) != null;
    }

    public static boolean isNotExist(String gid) {

        return !hasExist(gid);
    }

    public static void clear() {
        manager.clearAll();
    }

    public static void deleteById(String   gid) {
        manager.innerDeleteById(gid);
    }

    public static void add(Bottle bottle) {
        manager.innerSave(bottle);
    }
}
