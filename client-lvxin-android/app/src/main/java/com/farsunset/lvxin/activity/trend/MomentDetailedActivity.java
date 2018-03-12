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


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.adapter.CustomCommentListAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.MomentDetailsView;
import com.farsunset.lvxin.component.SimpleInputPanelView;
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.CommentObject;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommentResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class MomentDetailedActivity extends CIMMonitorActivity implements OnItemClickListener, OnInputPanelEventListener, HttpRequestListener, OnDialogButtonClickListener, OnItemClickedListener {

    private CustomCommentListAdapter adapter;
    private ListView listView;
    private Moment article;
    private SimpleInputPanelView inputPanelView;
    private User self;
    private CustomDialog customDialog;

    private Comment target;
    private String type = Comment.TYPE_0;

    @Override
    public void initComponents() {

        self = Global.getCurrentUser();
        article = (Moment) this.getIntent().getSerializableExtra(Moment.class.getName());
        if (article.getAllCount() == 0) {
            article.setCommentList(CommentRepository.queryCommentList(article.gid));
        }


        adapter = new CustomCommentListAdapter(article);
        listView = (ListView) findViewById(R.id.listView);
        inputPanelView = (SimpleInputPanelView) findViewById(R.id.inputPanelView);
        inputPanelView.setOnInputPanelEventListener(this);

        MomentDetailsView headerView = (MomentDetailsView) getHeaderView();
        headerView.displayMoment(article);
        headerView.setOnItemClickedListener(this);
        listView.addHeaderView(headerView);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    public View getHeaderView() {
        if (article.type.equals(Moment.FORMAT_LINK)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_link, null, false);
        }
        if (article.type.equals(Moment.FORMAT_VIDEO)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_video, null, false);
        }
        if (article.type.equals(Moment.FORMAT_TEXT_IMAGE) && StringUtils.isEmpty(article.thumbnail)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_text, null, false);
        }

        int photoSize = new JsonParser().parse(article.thumbnail).getAsJsonArray().size();
        if (photoSize == 1) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_one_photo, null, false);
        }

        return LayoutInflater.from(this).inflate(R.layout.moment_details_multi_photo, null, false);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {

        Comment comment = (Comment) arg1.getTag(R.id.target);
        if (comment == null || comment.account.equals(self.account)) {
            resetApiParams();
            return;
        }

        target = comment;
        type = Comment.TYPE_1;
        String name = FriendRepository.queryFriendName(comment.account);
        inputPanelView.setHint(getString(R.string.hint_comment, name));
        inputPanelView.show();
    }


    @Override
    public void onSendButtonClicked(String content) {

        performAddCommentRequest(content);
        inputPanelView.resetInputPanel();
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        if (result.isSuccess() && url.equals(URLConstant.COMMENT_PUBLISH_URL)) {
            CommentRepository.add(((CommentResult) result).data);
            article.add(((CommentResult) result).data);
            adapter.notifyDataSetChanged();
            listView.setSelection(listView.getBottom());
            resetApiParams();
        }

        if (result.isSuccess() && url.equals(URLConstant.ARTICLE_DELETE_URL)) {
            MomentRepository.deleteById(article.gid);
            Intent intent = new Intent(Constant.Action.ACTION_DELETE_MOMENT);
            intent.putExtra(Moment.class.getName(), article);
            LvxinApplication.sendLocalBroadcast(intent);
            finish();
        }


    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
    }


    public void resetApiParams() {
        type = Comment.TYPE_0;
        target = null;
        inputPanelView.setContent(null);
        inputPanelView.setHint(null);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_detailed;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (self.account.equals(article.account)) {
            getMenuInflater().inflate(R.menu.single_icon, menu);
            menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_delete);

            customDialog = new CustomDialog(this);
            customDialog.setOnDialogButtonClickListener(this);
            customDialog.setTitle(R.string.common_delete);
            customDialog.setMessage((R.string.tip_delete_article));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();

        performDeleteMomentRequest();


    }

    private void performDeleteMomentRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_DELETE_URL, BaseResult.class);
        requestBody.addParameter("gid", article.gid);
        HttpRequestLauncher.execute(requestBody, this);
    }


    private void performAddCommentRequest(String content) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_PUBLISH_URL, CommentResult.class);
        CommentObject body = new CommentObject();

        body.content = content;
        if (Comment.TYPE_1.equals(type)) {
            body.commentId = String.valueOf(target.gid);
            body.replyAccount = target.account;
            requestBody.addParameter("replyAccount", target.account);
        }
        requestBody.addParameter("content", new Gson().toJson(body));
        requestBody.addParameter("type", type);
        requestBody.addParameter("articleId", article.gid);
        requestBody.addParameter("authorAccount", article.account);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(Constant.Action.ACTION_REFRESH_MOMENT);
        intent.putExtra(Moment.class.getName(), article);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (view.getId() == R.id.bar_comment) {
            inputPanelView.show();
        }
    }
}
