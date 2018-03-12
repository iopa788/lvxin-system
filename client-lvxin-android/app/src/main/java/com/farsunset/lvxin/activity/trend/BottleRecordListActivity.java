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

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.adapter.BottleRecordListAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.component.GlobalEmptyView;
import com.farsunset.lvxin.database.BottleRepository;
import com.farsunset.lvxin.pro.R;


public class BottleRecordListActivity extends CIMMonitorActivity {
    private BottleRecordListAdapter adapter;
    private GlobalEmptyView emptyView;

    @Override
    public void initComponents() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BottleRecordListAdapter();
        recyclerView.setAdapter(adapter);

        emptyView = (GlobalEmptyView) findViewById(R.id.emptyView);
        emptyView.setTips(R.string.tips_bottle_bucket_empty);
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.addAll(BottleRepository.queryBottleList());
        if (adapter.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message msg) {

        if (Constant.MessageAction.ACTION_700.equals(msg.getAction()) || Constant.MessageAction.ACTION_701.equals(msg.getAction())) {
            adapter.addAll(BottleRepository.queryBottleList());
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_bottle_record;
    }

}
