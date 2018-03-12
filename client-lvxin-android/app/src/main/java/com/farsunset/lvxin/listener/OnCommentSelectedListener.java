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
package com.farsunset.lvxin.listener;

import com.farsunset.lvxin.component.CommentListView;

public interface OnCommentSelectedListener {


    /**
     * @param commentListView 被评论的文章的评论列表view
     * @param commentId       被回复的评论的ID，如果 回复的是文章则为null
     * @param authorAccount   文章作者账号
     * @param account         回复者账号
     * @param type            0 回复文章，1回复评论
     */
    void onCommentSelected(CommentListView commentListView, String articleId, String commentId, String authorAccount, String account, String type);
}
