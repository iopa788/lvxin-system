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

import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.util.BackgroundThreadHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicAccountRepository extends BaseRepository<PublicAccount, String> {

    private static PublicAccountRepository manager = new PublicAccountRepository();

    public static List<PublicAccount> queryPublicAccountList() {

        return manager.innerQueryAll();
    }

    public static List<PublicAccount> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").or().like("account", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static PublicAccount queryPublicAccount(String account) {

        return manager.innerQueryById(account);
    }

    public static void saveAll(final List<PublicAccount> list) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.clearAll();
                manager.innerSaveAll(list);
                for (PublicAccount pub : list) {
                    PublicMenuRepository.savePublicMenus(pub.menuList);
                }
            }
        });

    }

    public static void add(PublicAccount u) {
        manager.innerSave(u);
    }

    public static void delete(String gid) {
        manager.innerDeleteById(gid);
    }

}
