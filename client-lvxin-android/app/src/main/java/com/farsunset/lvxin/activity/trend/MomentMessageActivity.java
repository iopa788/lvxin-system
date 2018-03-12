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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.MomentMessageAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MomentResult;
import com.farsunset.lvxin.pro.R;

public class MomentMessageActivity extends BaseActivity implements HttpRequestListener, OnItemClickedListener {


    @Override
    public void initComponents() {

        MomentMessageAdapter adapter = new MomentMessageAdapter();
        RecyclerView messageListView = ((RecyclerView) findViewById(R.id.recyclerView));
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setAdapter(adapter);
        adapter.addAll(MessageRepository.queryMoments());

        adapter.setOnItemClickedListener(this);
        MessageRepository.batchReadMessage(new String[]{Constant.MessageAction.ACTION_801, Constant.MessageAction.ACTION_802});

        findViewById(R.id.emptyView).setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        hideProgressDialog();
        if (result.isSuccess() && ((MomentResult) result).data == null) {
            showToastView(R.string.tip_article_deleted);
            return;
        }


        MomentRepository.add(((MomentResult) result).data);
        Intent intent = new Intent(this, MomentDetailedActivity.class);
        intent.putExtra(Moment.class.getName(), ((MomentResult) result).data);
        startActivity(intent);
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_moment_message;
    }


    private void performGetMomentRequest(String gid) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_DETAILED_URL, MomentResult.class);
        requestBody.addParameter("gid", gid);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        Comment comment = (Comment) obj;
        Moment article = MomentRepository.queryById(comment.articleId);
        if (article != null) {
            Intent intent = new Intent(this, MomentDetailedActivity.class);
            intent.putExtra(Moment.class.getName(), MomentRepository.queryById(comment.articleId));
            startActivity(intent);
        } else {
            performGetMomentRequest(comment.articleId);
        }
    }
}
