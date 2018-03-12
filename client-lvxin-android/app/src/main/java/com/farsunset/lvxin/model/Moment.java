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
package com.farsunset.lvxin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "t_lvxin_moment")
public class Moment implements Serializable {

    public final static String FORMAT_TEXT_IMAGE = "0";//图文
    public final static String FORMAT_LINK = "1";//网址连接
    public final static String FORMAT_VIDEO = "2";//视频
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public String gid;

    @DatabaseField
    public String account;
    @DatabaseField
    public String content;
    @DatabaseField
    public String thumbnail;
    @DatabaseField
    public String link;
    @DatabaseField
    public String type;
    @DatabaseField
    public long timestamp;

    private List<Comment> commentList = new ArrayList<Comment>();

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> list) {
        commentList.clear();
        commentList.addAll(list);
    }


    public int getAllCount() {
        return commentList.size();
    }

    public List<Comment> getTextList() {
        List<Comment> textList = new ArrayList<Comment>();
        for (Comment comment : commentList) {
            if (!comment.type.equals(Comment.TYPE_2)) {
                textList.add(comment);
            }
        }
        return textList;
    }

    public List<Comment> getPraiseList() {
        List<Comment> praiseList = new ArrayList<Comment>();
        for (Comment comment : commentList) {
            if (comment.type.equals(Comment.TYPE_2)) {
                praiseList.add(comment);
            }
        }

        return praiseList;
    }

    public int getTextCount() {
        int count = 0;
        for (Comment comment : commentList) {
            if (!comment.type.equals(Comment.TYPE_2)) {
                count++;
            }
        }
        return count;
    }

    public int getPraiseCount() {
        int count = 0;
        for (Comment comment : commentList) {
            if (comment.type.equals(Comment.TYPE_2)) {
                count++;
            }
        }
        return count;
    }

    public void add(Comment comment) {
        commentList.add(comment);
    }

    public void remove(Comment comment) {
        commentList.remove(comment);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Moment) {
            Moment target = (Moment) o;
            return gid.equals(target.gid) && target.commentList.equals(commentList);
        }
        return false;
    }

}
