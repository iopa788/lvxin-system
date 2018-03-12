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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.adapter.NearbyListViewAdapter;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.NearbyPullRefreshListView;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnListViewRefreshListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.FriendListResult;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;

public class NearbyUserListActivity extends BaseActivity implements OnClickListener, OnListViewRefreshListener, HttpRequestListener, OnItemClickListener {

    private NearbyListViewAdapter adapter;
    private ArrayList<Friend> list = new ArrayList<Friend>();
    private NearbyPullRefreshListView userListView;
    private User self;


    @Override
    public void initComponents() {


        userListView = (NearbyPullRefreshListView) findViewById(R.id.userListView);
        userListView.setOnRefreshListener(this);

        adapter = new NearbyListViewAdapter(this, list);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(this);

        self = Global.getCurrentUser();


        userListView.doRefresh();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {


        Friend member = list.get(index - 1);
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(Friend.class.getName(), member);
        this.startActivity(intent);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        if (((FriendListResult) result).isNotEmpty()) {
            findViewById(R.id.nonearbyuserimage).setVisibility(View.GONE);
        } else {
            findViewById(R.id.nonearbyuserimage).setVisibility(View.VISIBLE);
        }

        list.clear();
        list.addAll(((FriendListResult) result).dataList);
        adapter.notifyDataSetChanged();
        userListView.refreshComplete(result.page);
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        userListView.refreshComplete(null);
    }


    private void performGetListRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.USER_NEARBY_URL, FriendListResult.class);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_trend_nearby;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_function_nearby;
    }

    @Override
    public void onGetNextPage() {

    }

    @Override
    public void onGetFirstPage() {
        performGetListRequest();
    }
}
