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
package com.farsunset.lvxin.activity.contact;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.PubAccountLookViewAdapter;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.component.GlobalEmptyView;
import com.farsunset.lvxin.component.LoadMoreRecyclerView;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.listener.OnLoadRecyclerViewListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.PublicAccountListResult;
import com.farsunset.lvxin.pro.R;

public class LookPubAccountActivity extends BaseActivity implements OnItemClickedListener, HttpRequestListener, OnLoadRecyclerViewListener {

    private PubAccountLookViewAdapter adapter;
    private GlobalEmptyView emptyView;
    private int currentPage = 1;
    private LoadMoreRecyclerView recyclerView;

    @Override
    public void initComponents() {

        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PubAccountLookViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadEventListener(this);
        recyclerView.setFooterView(adapter.getFooterView());
        adapter.setOnItemClickedListener(this);
        emptyView = (GlobalEmptyView) findViewById(R.id.emptyView);

        performLookListRequest();
    }

    private void performLookListRequest() {
        showProgressDialog(getString(R.string.tips_list_loading));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.PUBACCOUNT_LOOK_URL, PublicAccountListResult.class);
        requestBody.addParameter("currentPage", String.valueOf(currentPage));
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_loadmore_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_look_pubaccount;
    }

    @Override
    public void onItemClicked(Object obj, View view) {

        Intent intent = new Intent(this, PubAccountDetailActivity.class);
        intent.putExtra(PublicAccount.NAME, (PublicAccount) obj);
        this.startActivity(intent);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult data, String url) {

        PublicAccountListResult result = (PublicAccountListResult) data;
        if (result.isNotEmpty()) {
            adapter.addAll(result.dataList);
        }
        if (!result.isNotEmpty() && currentPage > 1) {
            currentPage--;
        }
        recyclerView.showMoreComplete(result.page);
        switchEmptyView();

        hideProgressDialog();
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    @Override
    public void onGetNextPage() {
        currentPage++;
        performLookListRequest();
    }

    @Override
    public void onListViewStartScroll() {

    }
}
