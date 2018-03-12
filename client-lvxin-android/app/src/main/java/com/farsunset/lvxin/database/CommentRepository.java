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

import com.farsunset.lvxin.model.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository extends BaseRepository<Comment, String> {

    private static CommentRepository manager =  new CommentRepository();

    public static void saveAll(List<Comment> list) {

        if (list == null || list.isEmpty()) {
            return;
        }
        deleteByArticleId(list.get(0).articleId);
        manager.innerSaveAll(list);
    }

    public static void deleteById(String id) {
        manager.innerDeleteById(id);
    }

    public static void deleteByArticleId(String articleId) {
        manager.innerExecuteSQL("delete from t_lvxin_comment where articleId=?", new String[]{articleId});
    }

    public static Comment query(String articleId, String account, String type) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("articleId", articleId).and().eq("account", account).and().eq("type", type).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Comment> queryCommentList(String articleId) {

        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", true).where().eq("articleId", articleId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Comment>();

    }
    public static void delete(Comment comment) {
        manager.innerDelete(comment);
    }
    public static void add(Comment comment) {
        manager.innerSave(comment);
    }
}
