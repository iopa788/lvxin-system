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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		article.setTimestamp(System.currentTimeMillis());
		this.articleRepository.save(article);

		broadcastPublishMomentMessage(article);
	}

	@Override
	public Page<Article> queryByPage(Article article, Pageable pageable) {
		Specification<Article> specification = new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(article.getAccount())) {
					predicatesList.add(builder.equal(root.get("account").as(String.class), article.getAccount()));
				}
				if (!StringUtils.isEmpty(article.getType())) {
					predicatesList.add(builder.equal(root.get("type").as(String.class), article.getType()));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
				query.orderBy(builder.desc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};
		return articleRepository.findAll(specification, pageable);
	}

	@Override
	public void delete(String articleId) {

		commentRepository.deleteByArticleId(articleId);

		Article article = articleRepository.findOne(articleId);
		if (article == null) {
			return;
		}

		this.articleRepository.delete(articleId);

		broadcastDeleteMomentMessage(article);

		// 删除掉广播出去的该条内容发布的消息
		messageRepository.deleteByContentAndAction(articleId, Constants.MessageAction.ACTION_800);

		deleteFile(article);
	}

	@Override
	public Page<Article> queryByPage(String account, Pageable pageable) {
		Specification<Article> specification = new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				query.where(builder.equal(root.get("account").as(String.class), account));
				query.orderBy(builder.desc(root.get("timestamp").as(Long.class)));
				return query.getRestriction();
			}
		};
		return articleRepository.findAll(specification, pageable);
	}

	@Override
	public Page<Article> queryRelevantByPage(String account, Pageable pageable) {

		Page<Article> page = articleRepository.queryRelevantByPage(account, pageable);
		for (Article article : page.getContent()) {
			article.setCommentList(commentRepository.queryList(article.getGid()));
		}
		return page;
	}

	@Override
	public Article queryById(String gid) {
		Article model = articleRepository.findOne(gid);
		if (model == null) {
			return null;
		}
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
		List<Article> list = articleRepository.queryList(account);
		articleRepository.deleteByAccount(account);
		commentRepository.deleteByAccount(account);
		for (Article article : list) {
			deleteFile(article);
		}

	}

	/**
	 * 删除相应的图片和视频文件
	 * 
	 * @param article
	 */
	private void deleteFile(Article article) {

		if (StringUtils.isNotEmpty(article.getThumbnail())) {
			List<SNSImage> snsImageList = new Gson().fromJson(article.getThumbnail(), new TypeToken<List<SNSImage>>() {
			}.getType());
			for (SNSImage image : snsImageList) {
				fileManagerService.delete(image.image);
				fileManagerService.delete(image.thumbnail);
			}
		}
		if (Objects.equals(article.getType(), Article.FORMAT_VIDEO)) {
			SNSVideo video = new Gson().fromJson(article.getContent(), MomentVideo.class).video;
			fileManagerService.delete(video.video);
			fileManagerService.delete(video.thumbnail);
		}

	}

}
