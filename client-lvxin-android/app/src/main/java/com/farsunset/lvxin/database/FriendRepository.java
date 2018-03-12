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


import android.support.v4.util.ArrayMap;

import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.model.Friend;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendRepository extends BaseRepository<Friend, String> {
    public final static String DATABASE_NAME = "base.db";
    private static FriendRepository manager = new FriendRepository();
    private static ArrayMap<String, Friend> friendCache = new ArrayMap<>();

    /**
     * 通讯录为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static List<Friend> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").or().like("account", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Friend> queryFriendList() {
        try {
            return manager.databaseDao.queryBuilder().where().ne("account", Global.getCurrentAccount()).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Friend> queryFriendList(String code) {
        try {
            return (manager.databaseDao.queryBuilder().where().eq("orgCode", code).query());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Friend> queryRootFriendList() {
        try {
            return manager.databaseDao.queryBuilder().where().isNull("orgCode").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Friend queryFriend(String account) {
        Friend friend = friendCache.get(account);
        if (friend == null) {
            friend = manager.innerQueryById(account);
            friendCache.put(account, friend);
        }
        return friend;
    }

    public static String queryFriendName(String account) {

        Friend friend = queryFriend(account);
        return friend == null ? "Unknow" : friend.name;
    }

    public static boolean isFriend(String account) {

        return queryFriend(account) != null;
    }

    public static boolean isNotExisted(String sender, String receiver) {

        if (Global.getCurrentAccount().equals(sender)) {
            return queryFriend(receiver) == null;
        }
        return queryFriend(sender) == null;
    }

    public static void saveAll(final List<Friend> list) {
        manager.clearAll();
        friendCache.clear();
        manager.innerSaveAll(list);
    }

    public static void modifyOnlineStatus(String account, String online) {

        String sql = "update " + Friend.TABLE_NAME + " set online=? where account=?";
        manager.innerExecuteSQL(sql, new String[]{online, account});
    }

    public static void modifyNameAndMotto(String account, String name, String motto) {

        String sql = "update " + Friend.TABLE_NAME + " set name=?,motto = ?  where account=?";
        manager.innerExecuteSQL(sql, new String[]{name, motto, account});

        Friend friend = friendCache.get(account);
        if (friend != null) {
            friend.name = name;
            friend.motto = motto;
        }
    }

    public static long count() {
        try {
            return manager.databaseDao.countOf();
        } catch (SQLException e) {
            return 0L;
        }
    }
}
