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
package com.farsunset.lvxin.component;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.MapViewActivity;
import com.farsunset.lvxin.activity.trend.FriendMomentActivity;
import com.farsunset.lvxin.activity.trend.MomentDetailedActivity;
import com.farsunset.lvxin.activity.trend.SelfMomentActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.dialog.MomentRespondWindow;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.MapAddress;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommentResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.io.Serializable;

public class SnsItemMomentView extends RelativeLayout implements OnClickListener, OnItemClickedListener, HttpRequestListener, OnDialogButtonClickListener {
    protected Moment moment;
    protected TextView text;
    private WebImageView icon;
    private TextView name;
    private TextView time;
    private CommentListView commentListView;
    private User self;
    private OnCommentSelectedListener commentSelectedListener;
    private MomentRespondWindow respondWindow;
    private View commentButton;
    private TextView location;
    private TextView delete;
    private CustomDialog customDialog;

    public SnsItemMomentView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        delete = (TextView) findViewById(R.id.delete);
        name = (TextView) findViewById(R.id.name);
        text = (TextView) findViewById(R.id.text);
        icon = (WebImageView) findViewById(R.id.icon);
        time = (TextView) findViewById(R.id.time);
        location = (TextView) findViewById(R.id.location);
        commentListView = (CommentListView) findViewById(R.id.commentListView);
        icon.setOnClickListener(this);
        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(this);
        respondWindow = new MomentRespondWindow(getContext());
        respondWindow.setOnItemClickedListener(this);
    }

    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        this.moment = moment;
        this.self = self;
        this.commentSelectedListener = commentSelectedListener;

        this.setTag(moment.gid);

        icon.load(FileURLBuilder.getUserIconUrl(moment.account), R.drawable.icon_def_head);
        name.getPaint().setFakeBoldText(true);
        if (moment.account.equals(self.account)) {
            name.setText(self.name);
        } else {
            name.setText(FriendRepository.queryFriendName(moment.account));
        }
        time.setText(AppTools.getRecentTimeString(moment.timestamp));


        PublishObject object = new Gson().fromJson(moment.content, PublishObject.class);
        text.setText(object.content);
        text.setVisibility(StringUtils.isNotEmpty(object.content) ? View.VISIBLE : GONE);
        if (object.location != null) {
            location.setVisibility(VISIBLE);
            location.setText(object.location.name);
            location.setTag(object.location);
            location.setOnClickListener(this);
        } else {
            location.setVisibility(GONE);
        }

        delete.setVisibility(self.account.equals(moment.account) ? VISIBLE : GONE);
        delete.setOnClickListener(this);
        commentListView.setVisibility(moment.getAllCount() > 0 ? VISIBLE : GONE);
        commentListView.showCommentsAndPraises(moment);
        commentListView.setTag(R.id.target, moment);
        commentListView.setOnItemClickListener(this);
    }


    public void addPraise(Comment data) {
        commentListView.addPraise(data);
    }


    public void addComment(Comment data) {
        commentListView.addComment(data);
    }


    @Override
    public void onClick(View view) {
        if (view == icon) {
            Intent intent = new Intent();
            if (self.account.equals(moment.account)) {
                intent.setClass(getContext(), SelfMomentActivity.class);
            } else {
                intent.setClass(getContext(), FriendMomentActivity.class);
                intent.putExtra(Friend.class.getName(), FriendRepository.queryFriend(moment.account));
            }
            getContext().startActivity(intent);

            return;
        }
        if (view == commentButton) {
            respondWindow.showAtLocation(commentButton, getHasPraise() != null);
            return;
        }

        if (view == this) {
            Intent intent = new Intent(getContext(), MomentDetailedActivity.class);
            intent.putExtra(Moment.class.getName(), moment);
            getContext().startActivity(intent);
        }
        if (view == location) {
            Intent intent = new Intent(getContext(), MapViewActivity.class);
            intent.putExtra(MapAddress.class.getName(), (Serializable) location.getTag());
            getContext().startActivity(intent);
        }

        if (view == delete) {
            customDialog = new CustomDialog(getContext());
            customDialog.setOnDialogButtonClickListener(this);
            customDialog.setTitle(R.string.common_delete);
            customDialog.setMessage((R.string.tip_delete_article));
            customDialog.show();
        }
    }

    private Comment getHasPraise() {
        for (Comment comment : moment.getPraiseList()) {
            if (comment.account.equals(self.account)) {
                return comment;
            }
        }
        return null;
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Comment) {
            Comment comment = (Comment) obj;
            CommentListView commentListView = (CommentListView) view;
            commentSelectedListener.onCommentSelected(commentListView, moment.gid, comment.gid, moment.account, comment.account, Comment.TYPE_1);
        }
        if (view.getId() == R.id.bar_comment) {
            BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    commentSelectedListener.onCommentSelected(commentListView, moment.gid, null, moment.account, null, Comment.TYPE_0);
                }
            }, 50);
        }
        if (view.getId() == R.id.bar_praise) {
            Comment praise = getHasPraise();
            if (praise != null) {
                performCancelPraiseRequest(praise.gid);
            } else {
                performPraiseRequest();
            }
        }
    }

    private void performPraiseRequest() {

        respondWindow.disenablePariseMenu();
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_PRAISE_URL, CommentResult.class);
        requestBody.addParameter("articleId", moment.gid);
        requestBody.addParameter("authorAccount", moment.account);
        HttpRequestLauncher.execute(requestBody, this);
    }


    private void performCancelPraiseRequest(String gid) {
        respondWindow.disenablePariseMenu();
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_DELETE_URL, BaseResult.class);
        requestBody.addParameter("gid", gid);
        requestBody.addParameter("articleId", moment.gid);
        requestBody.addParameter("authorAccount", moment.account);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        respondWindow.enablePariseMenu();
        if (url.equals(URLConstant.COMMENT_PRAISE_URL) && result.isSuccess()) {
            Comment comment = ((CommentResult) result).data;
            moment.add(comment);
            commentListView.addPraise(comment);
            CommentRepository.add(comment);
        }
        if (url.equals(URLConstant.COMMENT_DELETE_URL) && result.isSuccess()) {

            Comment praise = getHasPraise();
            moment.remove(praise);
            commentListView.removePraise(praise);
            CommentRepository.delete(praise);
        }

        if (url.equals(URLConstant.ARTICLE_DELETE_URL) && result.isSuccess()) {

            MomentRepository.deleteById(moment.gid);
            Intent intent = new Intent(Constant.Action.ACTION_DELETE_MOMENT);
            intent.putExtra(Moment.class.getName(), moment);
            LvxinApplication.sendLocalBroadcast(intent);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        respondWindow.enablePariseMenu();
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        performDeleteMomentRequest();
    }

    private void performDeleteMomentRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_DELETE_URL, BaseResult.class);
        requestBody.addParameter("gid", moment.gid);
        HttpRequestLauncher.execute(requestBody, this);
    }

}
