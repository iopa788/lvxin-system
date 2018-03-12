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

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.util.BackgroundThreadHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MomentRepository extends BaseRepository<Moment, String> {

    private static MomentRepository manager = new MomentRepository();

    public static void add(final Moment article) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.saveIfNotExists(article);
                CommentRepository.saveAll(article.getCommentList());
            }
        });
    }

    public static void saveAll(final List<Moment> list) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Moment article : list) {
                    manager.saveIfNotExists(article);
                    CommentRepository.saveAll(article.getCommentList());
                }
            }
        });

    }

    public static List<Moment> queryFirstPage() {
        try {
            List<Moment> list = manager.databaseDao.queryBuilder().offset(0L).limit(Constant.MOMENT_PAGE_SIZE).orderBy("timestamp", false).query();
            for (Moment article : list) {
                article.setCommentList(CommentRepository.queryCommentList(article.gid));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Moment>();
        }
    }

    public static List<Moment> queryFirstPage(String account, int pagenow) {

        long startRow = (long) (pagenow - 1) * Constant.MESSAGE_PAGE_SIZE;
        try {
            return manager.databaseDao.queryBuilder().offset(startRow).limit(Constant.MESSAGE_PAGE_SIZE)
                    .orderBy("timestamp", false)
                    .where().eq("account", account)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<Moment>();
    }


    public static void deleteById(String gid) {
        manager.innerDeleteById(gid);
        CommentRepository.deleteByArticleId(gid);
    }

    public static Moment queryById(String gid) {
        Moment article = manager.innerQueryById(gid);
        if (article == null) {
            return null;
        }
        article.setCommentList(CommentRepository.queryCommentList(article.gid));
        return article;
    }
}
