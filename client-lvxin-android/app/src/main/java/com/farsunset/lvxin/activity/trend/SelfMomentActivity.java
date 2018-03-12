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


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.SelfMomentListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.LoadMoreRecyclerView;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnLoadRecyclerViewListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MomentListResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class SelfMomentActivity extends BaseActivity implements HttpRequestListener, OnLoadRecyclerViewListener {


    private SelfMomentListViewAdapter adapter;
    private LoadMoreRecyclerView circleListView;
    private User self;
    private int currentPage = 1;
    private InnerMomentReceiver mInnerMomentReceiver;

    @Override
    public void initComponents() {


        self = Global.getCurrentUser();

        circleListView = (LoadMoreRecyclerView) findViewById(R.id.circleListView);
        circleListView.setOnLoadEventListener(this);
        adapter = new SelfMomentListViewAdapter(this);
        circleListView.setAdapter(adapter);
        circleListView.setFooterView(adapter.getFooterView());
        adapter.addAll(MomentRepository.queryFirstPage(self.account, currentPage));
        adapter.getHeaderView().displayIcon(FileURLBuilder.getUserIconUrl(self.account));

        circleListView.showProgressBar();
        performMomentListRequest();

        mInnerMomentReceiver = new InnerMomentReceiver();
        LvxinApplication.registerLocalReceiver(mInnerMomentReceiver, mInnerMomentReceiver.getIntentFilter());
    }


    private void performMomentListRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_MY_LIST_URL, MomentListResult.class);
        requestBody.addParameter("currentPage", String.valueOf(currentPage));
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LvxinApplication.unregisterLocalReceiver(mInnerMomentReceiver);
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
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

            switchEmptyView();

            circleListView.showMoreComplete(result.page);

        }

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
        return R.string.label_moment_me;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_notify);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            this.startActivity(new Intent(this, MomentMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetNextPage() {
        currentPage++;
        performMomentListRequest();
    }

    @Override
    public void onListViewStartScroll() {

    }

    public class InnerMomentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Moment article = (Moment) intent.getSerializableExtra(Moment.class.getName());
            adapter.remove(article);
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_DELETE_MOMENT);
            return filter;
        }
    }
}
