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
package com.farsunset.lvxin.activity.trend;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.activity.util.VideoRecorderActivity;
import com.farsunset.lvxin.adapter.MomentListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.CommentListView;
import com.farsunset.lvxin.component.CustomSwipeRefreshLayout;
import com.farsunset.lvxin.component.LoadMoreRecyclerView;
import com.farsunset.lvxin.component.SimpleInputPanelView;
import com.farsunset.lvxin.component.SnsItemMomentView;
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.listener.OnListViewRefreshListener;
import com.farsunset.lvxin.listener.OnLoadRecyclerViewListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.CommentObject;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommentResult;
import com.farsunset.lvxin.network.result.MomentListResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class SNSMomentActivity extends CIMMonitorActivity implements OnListViewRefreshListener, OnLoadRecyclerViewListener, HttpRequestListener, OnCommentSelectedListener, OnInputPanelEventListener, OnItemClickedListener {
    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private MomentListViewAdapter adapter;
    private LoadMoreRecyclerView circleListView;
    private User self;
    private int currentPage = 1;
    private SimpleInputPanelView inputPanelView;
    private InnerMomentReceiver mInnerMomentReceiver;

    private CommentListView mCommentListView;
    private int mFullHeight;
    private HttpRequestBody commentBody = new HttpRequestBody(URLConstant.COMMENT_PUBLISH_URL, CommentResult.class);

    @Override
    public void initComponents() {
        self = Global.getCurrentUser();
        swipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setLimited(true);
        circleListView = (LoadMoreRecyclerView) findViewById(R.id.circleListView);
        circleListView.setOnLoadEventListener(this);

        inputPanelView = (SimpleInputPanelView) findViewById(R.id.inputPanelView);
        inputPanelView.setOnInputPanelEventListener(this);

        adapter = new MomentListViewAdapter(this);
        adapter.setOnCommentSelectedListener(this);
        circleListView.setAdapter(adapter);
        circleListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage());

        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(self.account));
        adapter.getHeaderView().showMessageRemind(MessageRepository.queryNewMoments(3));
        adapter.getHeaderView().setOnIconClickedListener(this);
        mInnerMomentReceiver = new InnerMomentReceiver();
        LvxinApplication.registerLocalReceiver(mInnerMomentReceiver, mInnerMomentReceiver.getIntentFilter());

        com.farsunset.lvxin.model.Message newMomentMsg = MessageRepository.queryNewMomentMessage();
        if (adapter.getItemCount() < Constant.MOMENT_PAGE_SIZE || newMomentMsg != null) {
            swipeRefreshLayout.startRefreshing();
        }
        MessageRepository.deleteByAction(Constant.MessageAction.ACTION_800);

        mFullHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    }


    private void performMomentListRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_RELEVANTLIST_URL, MomentListResult.class);
        requestBody.addParameter("currentPage", String.valueOf(currentPage));
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onGetNextPage() {
        currentPage++;
        performMomentListRequest();
    }

    @Override
    public void onGetFirstPage() {
        currentPage = 1;
        performMomentListRequest();
    }

    @Override
    public void onListViewStartScroll() {
        if (inputPanelView.getVisibility() == View.VISIBLE) {
            inputPanelView.hide();
            cleanCommentInfo();
        }


    }

    public void onListViewScrollNow() {

    }


    @Override
    public void onHttpRequestSucceed(BaseResult data, String url) {

        if (URLConstant.ARTICLE_RELEVANTLIST_URL.equals(url)) {
            MomentListResult result = (MomentListResult) data;

            swipeRefreshLayout.onRefreshCompleted();

            if (result.isNotEmpty() && currentPage == 1 && !adapter.listEquals(result.dataList)) {
                adapter.replaceFirstPage(result.dataList);
                MomentRepository.saveAll(result.dataList);
            }

            if (result.isNotEmpty() && currentPage > 1) {
                adapter.addAll(result.dataList);
            }

            if (result.isEmpty()) {
                currentPage = currentPage > 1 ? currentPage-- : currentPage;
            }

            circleListView.showMoreComplete(result.page);

        }

        if (URLConstant.COMMENT_PUBLISH_URL.equals(url)) {
            CommentResult result = (CommentResult) data;
            CommentRepository.add(result.data);

            mCommentListView.addComment(result.data);

            cleanCommentInfo();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        circleListView.showMoreComplete(null);
        swipeRefreshLayout.onRefreshCompleted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MomentPublishActivity.REQUEST_CODE) {
            Moment article = (Moment) data.getSerializableExtra(Moment.class.getName());
            adapter.add(article);
            circleListView.scrollToPosition(0);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == VideoRecorderActivity.REQUEST_CODE) {
            data.setClass(this, MomentPublishActivity.class);
            this.startActivityForResult(data, MomentPublishActivity.REQUEST_CODE);
        }
    }

    @Override
    public void onCommentSelected(CommentListView currentView, String articleId, String commentId, String authorAccount, String account, String type) {


        commentBody.addParameter("articleId", articleId);
        commentBody.addParameter("authorAccount", authorAccount);
        commentBody.addParameter("replyAccount", account);
        commentBody.addParameter("account", self.account);
        commentBody.addParameter("type", type);
        commentBody.addParameter("commentId", commentId);
        if (!authorAccount.equals(self.account)) {
            String name = FriendRepository.queryFriendName(account == null ? authorAccount : account);
            inputPanelView.setHint(getString(R.string.hint_comment, name));
        }
        mCommentListView = currentView;

        int inputPanelHeight = inputPanelView.getFullHeight();
        if (commentId == null) {
            circleListView.smoothScrollBy(0, mCommentListView.getEndYOnScrenn() - (mFullHeight - inputPanelHeight));
        } else {
            circleListView.smoothScrollBy(0, inputPanelHeight - (mFullHeight - mCommentListView.getLastTouchY()));
        }

        inputPanelView.show();

    }

    private void performAddCommentRequest(String content) {
        CommentObject body = new CommentObject();
        body.content = content;
        String type = commentBody.getParameterValue("type");
        if (Comment.TYPE_1.equals(type)) {
            body.commentId = String.valueOf(commentBody.getParameterValue("commentId"));
            body.replyAccount = commentBody.getParameterValue("replyAccount");
            commentBody.removeParameter("commentId");
        }
        commentBody.addParameter("content", new Gson().toJson(body));
        HttpRequestLauncher.execute(commentBody, this);
    }


    @Override
    public void onSendButtonClicked(String content) {

        performAddCommentRequest(content);

        inputPanelView.hide();
    }

    public void cleanCommentInfo() {

        commentBody.clearParameter();
        inputPanelView.setHint(null);
        inputPanelView.setContent(null);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_circle;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_function_moment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moment_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_publish) {
            Intent intent = new Intent(this, MomentPublishActivity.class);
            this.startActivityForResult(intent, MomentPublishActivity.REQUEST_CODE);
        }
        if (item.getItemId() == R.id.menu_camera) {
            Intent intent = new Intent(this, VideoRecorderActivity.class);
            this.startActivityForResult(intent, VideoRecorderActivity.REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        LvxinApplication.unregisterLocalReceiver(mInnerMomentReceiver);
        super.onDestroy();
    }


    @Override
    public void onMessageReceived(Message message) {
        if (message.getAction().equals(Constant.MessageAction.ACTION_801) || message.getAction().equals(Constant.MessageAction.ACTION_802)) {
            Comment comment = new Gson().fromJson(message.getContent(), Comment.class);
            String articleId = comment.articleId;
            SnsItemMomentView momentView = (SnsItemMomentView) circleListView.findViewWithTag(articleId);
            if (momentView != null) {
                if (comment.type.equals(Comment.TYPE_2)) {
                    momentView.addPraise(comment);
                } else {
                    momentView.addComment(comment);
                }
            }
            adapter.getHeaderView().showMessageRemind(MessageRepository.queryNewMoments(3));
        }

    }

    @Override
    public void onItemClicked(Object obj, View view) {
        startActivity(new Intent(this, SelfMomentActivity.class));
    }

    public class InnerMomentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Moment article = (Moment) intent.getSerializableExtra(Moment.class.getName());

            if (intent.getAction().equals(Constant.Action.ACTION_DELETE_MOMENT)) {
                adapter.remove(article);
            }
            if (intent.getAction().equals(Constant.Action.ACTION_REFRESH_MOMENT)) {
                adapter.update(article);
            }
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_DELETE_MOMENT);
            filter.addAction(Constant.Action.ACTION_REFRESH_MOMENT);
            return filter;
        }
    }

}
