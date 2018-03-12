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

import com.farsunset.lvxin.model.Group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends BaseRepository<Group, String> {

    private static GroupRepository manager = new GroupRepository();

    public static List<Group> queryCreatedList(String account) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("founder", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Group>();
    }

    public static List<Group> queryJoinList(String account) {

        try {
            return manager.databaseDao.queryBuilder().where().ne("founder", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Group>();

    }

    public static List<Group> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").or().like("groupId", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void saveAll(final List<Group> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
        for (Group g : list) {
            GroupMemberRepository.saveAll(g.memberList);
        }

    }

    public static void add(Group g) {
        manager.innerSave(g);
    }

    public static void deleteById(String groupId) {
        manager.innerDeleteById(groupId);
    }

    public static void update(Group group) {
        manager.innerUpdate(group);
    }


    public static Group queryById(String groupId) {
        return manager.innerQueryById(groupId);
    }
}
