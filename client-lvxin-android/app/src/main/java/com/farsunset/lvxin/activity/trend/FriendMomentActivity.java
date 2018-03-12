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
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.adapter.SelfMomentListViewAdapter;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.component.LoadMoreRecyclerView;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.listener.OnLoadRecyclerViewListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MomentListResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class FriendMomentActivity extends BaseActivity implements HttpRequestListener, OnLoadRecyclerViewListener, OnItemClickedListener {

    private SelfMomentListViewAdapter adapter;
    private LoadMoreRecyclerView circleListView;
    private Friend friend;
    private int currentPage = 1;

    @Override
    public void initComponents() {


        friend = (Friend) getIntent().getSerializableExtra(Friend.class.getName());
        setTitle(friend.getTitle());
        circleListView = (LoadMoreRecyclerView) findViewById(R.id.circleListView);
        circleListView.setOnLoadEventListener(this);
        adapter = new SelfMomentListViewAdapter(this);
        circleListView.setAdapter(adapter);
        circleListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage(friend.account, currentPage));
        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(friend.account));
        adapter.getHeaderView().setOnIconClickedListener(this);

        circleListView.showProgressBar();
        performLoadMomentRequest();
    }

    @Override
    public void onClick(View v) {

    }


    private void performLoadMomentRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_LIST_URL, MomentListResult.class);
        requestBody.addParameter("currentPage", String.valueOf(currentPage));
        requestBody.addParameter("otherAccount", friend.account);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onGetNextPage() {
        currentPage++;
        performLoadMomentRequest();
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            circleListView.hideHintView();
        } else {
            findViewById(R.id.emptyView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onHttpRequestSucceed(BaseResult data, String url) {
        MomentListResult result = (MomentListResult) data;
        if (result.isSuccess()) {
            if (result.isNotEmpty() && currentPage == 1 && !adapter.listEquals(result.dataList)) {
                adapter.replaceFirstPage(result.dataList);
                MomentRepository.saveAll(result.dataList);
            }

            if (result.isNotEmpty() && currentPage > 1) {
                adapter.addAll(result.dataList);
            }

            if (!result.isNotEmpty() && currentPage > 1) {
                currentPage--;
            }
        }
        circleListView.showMoreComplete(result.page);
        switchEmptyView();
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        circleListView.showMoreComplete(null);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_moment_listview;
    }


    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @Override
    public void onListViewStartScroll() {

    }

    @Override
    public void onItemClicked(Object obj, View view) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        startActivity(intent);
    }
}
