/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.lvxin.annotation.PageNumber;
import com.farsunset.lvxin.annotation.TokenAccount;
import com.farsunset.lvxin.cim.push.DefaultMessagePusher;
import com.farsunset.lvxin.dto.BaseResult;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.service.CommentService;
import com.farsunset.lvxin.service.MessageService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.PageCompat;
import com.farsunset.lvxin.util.UUIDTools;
import com.google.gson.Gson;

@RestController
@RequestMapping("/cgi/comment")
public class APICommentController {

	@Autowired
	DefaultMessagePusher defaultMessagePusher;
	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/publish.api")
	public BaseResult publish(@TokenAccount String account, Comment comment, String replyAccount,
			String authorAccount) {
		BaseResult result = new BaseResult();

		comment.setAccount(account);
		commentService.add(comment);
		result.data = comment;

		broadcastCommentMessage(authorAccount, replyAccount, comment);

		return result;
	}

	@RequestMapping(value = "/praise.api")
	public BaseResult publish(@TokenAccount String account, String articleId, String authorAccount) {
		BaseResult result = new BaseResult();
		Comment comment = new Comment();
		comment.setAccount(account);
		comment.setArticleId(articleId);
		comment.setType(Comment.ACTION_2);
		commentService.add(comment);
		result.data = comment;

		broadcastPraiseMessage(authorAccount, comment);

		return result;
	}

	@RequestMapping(value = "/list.api")
	public BaseResult list(Comment comment, @PageNumber int currentPage) {

		Pageable pageable = new PageRequest(currentPage, Constants.PAGE_SIZE);
		Page<Comment> page = this.commentService.queryByPage(comment, pageable);

		return PageCompat.transform(page);
	}

	@RequestMapping(value = "/delete.api")
	public BaseResult delete(String authorAccount, @TokenAccount String account, String gid) {
		BaseResult result = new BaseResult();

		Comment comment = new Comment();
		comment.setGid(gid);
		comment.setAccount(account);

		commentService.delete(comment);
		messageService.deleteCommentRemind(account, authorAccount, gid);
		broadcastDeleteMessage(authorAccount, comment);
		return result;
	}

	/**
	 * 评论被删除或者被取消点赞，只通知文章作者
	 */
    private void broadcastDeleteMessage(String authorAccount, Comment comment) {
		boolean isNotSelf = !comment.getAccount().equals(authorAccount);
		// 如果自己在自己的文章里删除评论或者取消点赞 则不发通知
		if (isNotSelf) {
			Message message = new Message();
			message.setSender(comment.getAccount());
			message.setContent(comment.getGid());
			message.setAction(Constants.MessageAction.ACTION_804);
			message.setReceiver(authorAccount);
			defaultMessagePusher.push(message);
		}
	}

	/**
	 * 有新的赞时，发送提醒消息文章发布者
	 */
    private void broadcastPraiseMessage(String authorAccount, Comment comment) {

		boolean isNotSelf = !comment.getAccount().equals(authorAccount);
		// 如果自己给自己点赞 则不发通知
		if (isNotSelf) {
			Message praiseMessage = new Message();
			praiseMessage.setSender(comment.getAccount());
			praiseMessage.setContent(new Gson().toJson(comment));
			praiseMessage.setAction(Constants.MessageAction.ACTION_801);
			praiseMessage.setReceiver(authorAccount);
			praiseMessage.setExtra(comment.getGid());
			defaultMessagePusher.push(praiseMessage);
		}

	}

	/**
	 * 有新的评论时，发送提醒消息给 被回复者
	 */
    private void broadcastCommentMessage(String authorAccount, String replyAccount, Comment comment) {
		Message commentMessage = new Message();
		commentMessage.setSender(comment.getAccount());
		commentMessage.setContent(new Gson().toJson(comment));
		// 记录下该条评论的ID，用于后续通过评论ID删除这个提醒
		commentMessage.setExtra(comment.getGid());

		// 如果别人回复自己的文章，或者别人在你文章里评论别人的评论，则发送消息给文章作者
		boolean isAnswerNotAuthor = !comment.getAccount().equals(authorAccount);
		if (isAnswerNotAuthor) {
			commentMessage.setAction(Constants.MessageAction.ACTION_801);
			commentMessage.setReceiver(authorAccount);
			defaultMessagePusher.push(commentMessage);
		}

		// 被回复者是不是文章的作者
		boolean isReplyNotAuthor = !authorAccount.equals(replyAccount);
		// 是不是回复别人的评论
		boolean isReplyComment = comment.getType().equals(Comment.ACTION_1);

		if (isReplyComment && isReplyNotAuthor) {
			commentMessage.setMid(UUIDTools.getUUID());
			commentMessage.setReceiver(replyAccount);
			commentMessage.setAction(Constants.MessageAction.ACTION_802);
			defaultMessagePusher.push(commentMessage);
		}

	}

}
