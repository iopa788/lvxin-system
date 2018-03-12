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
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.adapter.DriftBottleListAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.CustomSwipeRefreshLayout;
import com.farsunset.lvxin.component.PaddingDecoration;
import com.farsunset.lvxin.database.BottleRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.BottleResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;

public class DriftBottleActivity extends CIMMonitorActivity implements HttpRequestListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickedListener {

    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private DriftBottleListAdapter adapter;
    private User self;
    private TextView badge;
    private Bottle bottle;
    @Override
    public void initComponents() {
        self = Global.getCurrentUser();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 15;
        recyclerView.addItemDecoration(new PaddingDecoration(padding));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DriftBottleListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnBottleSelectedListener(this);
        swipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_drift_bottle;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_function_bottle;
    }

    @Override
    public void onHttpRequestSucceed(BaseResult data, String url) {
        swipeRefreshLayout.onRefreshCompleted();
        hideProgressDialog();
        if (URLConstant.BOTTLE_GET_URL.equals(url) && data.isSuccess() && adapter.getItemCount() == 1) {
            BottleResult result = (BottleResult) data;
            Bottle target = result.data;
            adapter.add(target);
        }
        if (URLConstant.BOTTLE_RECEIVED_URL.equals(url) && data.isSuccess()) {
            Intent intent = new Intent(this, BottleChatActivity.class);
            intent.putExtra(Bottle.class.getSimpleName(),bottle);
            startActivity(intent);
            adapter.remove(bottle);

            saveBottleAndMessage(bottle);
        }
        if (URLConstant.BOTTLE_RECEIVED_URL.equals(url) && !data.isSuccess()) {
            showToastView(R.string.tips_bottle_dismissed);
            adapter.remove(bottle);
        }
    }

    private void saveBottleAndMessage(Bottle target){
        BottleRepository.add(target);
        Message msg = new Message();
        msg.format = Constant.MessageFormat.FORMAT_TEXT;
        msg.mid = StringUtils.getUUID();
        msg.receiver = self.account;
        msg.sender = target.gid;
        msg.action = Constant.MessageAction.ACTION_700;
        msg.timestamp = target.timestamp;
        msg.status = Message.STATUS_READ;
        msg.content = target.content;
        MessageRepository.add(msg);
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
        swipeRefreshLayout.onRefreshCompleted();
    }


    private void performGetBottleRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.BOTTLE_GET_URL, BottleResult.class);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performReceivedRequest() {
        showProgressDialog(getString(R.string.tip_loading,getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.BOTTLE_RECEIVED_URL, BaseResult.class);
        requestBody.addParameter("gid",bottle.gid);
        HttpRequestLauncher.execute(requestBody, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driftbottle, menu);
        View view = menu.findItem(R.id.menu_record).getActionView().findViewById(R.id.menu_record);
        view.setOnClickListener(this);
        badge = (TextView) view.findViewById(R.id.badge);
        showNewMsgLabe();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        performGetBottleRequest();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_record) {
            this.startActivity(new Intent(this, BottleRecordListActivity.class));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (badge != null) {
            showNewMsgLabe();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_publish) {
            this.startActivity(new Intent(this, BottlePublishActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        bottle = (Bottle) obj;
        performReceivedRequest();
    }


    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message msg) {

        if (Constant.MessageAction.ACTION_700.equals(msg.getAction())) {
            showNewMsgLabe();
        }
    }

    private void showNewMsgLabe() {
        long count = MessageRepository.countNewByType(Constant.MessageAction.ACTION_700);
        if (count > 0) {
            badge.setVisibility(View.VISIBLE);
            badge.setText(String.valueOf(count));
        } else {
            badge.setText(null);
            badge.setVisibility(View.GONE);
        }
    }


}
