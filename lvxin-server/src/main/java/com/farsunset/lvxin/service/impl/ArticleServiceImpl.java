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
package com.farsunset.lvxin.service.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.farsunset.lvxin.cim.push.MomentMessagePusher;
import com.farsunset.lvxin.message.bean.MomentVideo;
import com.farsunset.lvxin.message.bean.SNSImage;
import com.farsunset.lvxin.message.bean.SNSVideo;
import com.farsunset.lvxin.model.Article;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.repository.ArticleRepository;
import com.farsunset.lvxin.repository.CommentRepository;
import com.farsunset.lvxin.repository.MessageRepository;
import com.farsunset.lvxin.service.ArticleService;
import com.farsunset.lvxin.util.Constants;
import com.farsunset.lvxin.util.UUIDTools;
import com.farsunset.lvxin.web.jstl.Page;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private FileManagerServiceImpl fileManagerService;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MomentMessagePusher momentMessagePusher;

	@Override
	public void add(Article article) {

		article.setGid(UUIDTools.getUUID());
		article.setTimestamp(String.valueOf(System.currentTimeMillis()));
		this.articleRepository.save(article);

		broadcastPublishMomentMessage(article);
	}

	@Override
	public Page queryByPage(Article article, Page page) {
		page.setCount(articleRepository.queryAmount(article));
		if (page.getCount() == 0) {
			return page;
		}
		return this.articleRepository.queryByPage(article, page);
	}

	@Override
	public void delete(String articleId) {

		commentRepository.deleteByArticleId(articleId);
		Article article = articleRepository.get(articleId);
		this.articleRepository.delete(article);

		broadcastDeleteMomentMessage(article);

		// 删除掉广播出去的该条内容发布的消息
		messageRepository.delete(article.getAccount(),articleId , Constants.MessageAction.ACTION_800);

		deleteFile(article);
	}

	@Override
	public Page queryByPage(String account, Page page) {
		page.setCount(articleRepository.queryAmount(account));
		if (page.getCount() == 0) {
			return page;
		}
		this.articleRepository.queryByPage(account, page);

		for (Object o : page.getDataList()) {
			Article article = (Article) o;
			article.setCommentList(commentRepository.queryList(article.getGid()));
		}
		return page;
	}

	@Override
	public Page queryRelevantByPage(String account, Page page) {
		page.setCount((int) articleRepository.queryRelevantAmount(account));
		if (page.getCount() == 0) {
			return page;
		}
		this.articleRepository.queryRelevantByPage(account, page);
		for (Object o : page.getDataList()) {
			Article article = (Article) o;
			article.setCommentList(commentRepository.queryList(article.getGid()));
		}
		return page;
	}

	@Override
	public Article queryById(String gid) {
		Article model = articleRepository.get(gid);
		model.setCommentList(commentRepository.queryList(model.getGid()));
		return model;
	}

	private void broadcastPublishMomentMessage(Article article) {
		Message message = new Message();
		message.setAction(Constants.MessageAction.ACTION_800);
		message.setSender(article.getAccount());
		message.setContent(article.getGid());
		momentMessagePusher.push(message);

	}

	private void broadcastDeleteMomentMessage(Article article) {
		Message message = new Message();
		message.setAction(Constants.MessageAction.ACTION_803);
		message.setContent(article.getGid());
		message.setSender(article.getAccount());
		momentMessagePusher.push(message);

	}

	@Override
	public void deleteBatch(String account) {
		List<Article> list = articleRepository.queryByAccount(account);
		articleRepository.deleteByAccount(account);
		commentRepository.deleteByAccount(account);
		for (Article article : list) {
			deleteFile(article);
		}

	}

	/**
	 * 删除相应的图片文件
	 * 
	 * @param article
	 */
	private void deleteFile(Article article) {
		if(StringUtils.isNotEmpty(article.getThumbnail())) {
			List<SNSImage> snsImageList = JSON.parseObject(article.getThumbnail(), new TypeReference<List<SNSImage>>() {}.getType());
			for (SNSImage image : snsImageList) {
				fileManagerService.delete(image.image);
				fileManagerService.delete(image.thumbnail);
			}
		}
		if (Objects.equals(article.getType(), Article.FORMAT_VIDEO)) {
	       SNSVideo video = JSON.parseObject(article.getContent(), MomentVideo.class).video;
	       fileManagerService.delete(video.video);
		   fileManagerService.delete(video.thumbnail);
		}
	}

}
