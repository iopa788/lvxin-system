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

import com.farsunset.lvxin.model.GroupMember;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberRepository extends BaseRepository<GroupMember, String> {

    private static GroupMemberRepository manager = new GroupMemberRepository();

    public static List<GroupMember> queryMemberList(String groupId) {
        List<GroupMember> list = new ArrayList<>();
        try {
            list.addAll(manager.databaseDao.queryBuilder().orderBy("host", false).where().eq("groupId", groupId).query());
            for (GroupMember member : list) {
                member.name = FriendRepository.queryFriendName(member.account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static List<String> queryMemberAccountList(String groupId) {

        List<String> list = new ArrayList<String>();
        List<GroupMember> data = queryMemberList(groupId);
        for (GroupMember m : data) {
            list.add(m.account);
        }
        return list;
    }

    public static void saveAll(List<GroupMember> list) {

        if (list == null || list.isEmpty()) {
            return;
        }

        deleteByGID(list.get(0).groupId);
        manager.innerSaveAll(list);
    }

    public static void saveMember(GroupMember g) {
        manager.createOrUpdate(g);
    }

    public static void deleteByGID(String groupId) {
        manager.innerExecuteSQL("delete from " + GroupMember.TABLE_NAME + " where groupId=?", new String[]{groupId});
    }

    public static void delete(String groupId, String account) {
        manager.innerExecuteSQL("delete from " + GroupMember.TABLE_NAME + " where groupId=? and account=?", new String[]{groupId, account});
    }

    public static void delete(String groupId, List<String> list) {
        try {
            DeleteBuilder builder = manager.databaseDao.deleteBuilder();
            builder.where().eq("groupId", groupId).and().in("account", list);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
