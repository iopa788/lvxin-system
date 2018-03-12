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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.GroupListViewAdapter;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.component.GlobalEmptyView;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.pro.R;

public class GroupListActivity extends BaseActivity {
    protected GlobalEmptyView emptyView;
    protected RecyclerView recyclerView;
    protected GroupListViewAdapter adapter;

    @Override
    public void initComponents() {


        recyclerView = (RecyclerView) findViewById(R.id.groupList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupListViewAdapter();
        recyclerView.setAdapter(adapter);

        emptyView = (GlobalEmptyView) findViewById(R.id.emptyView);
        emptyView.setTips(R.string.tips_empty_group);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_grouplist;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_mygroups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        Button button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CreateGroupActivity.class));
            }
        });
        button.setText(R.string.common_create);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onResume() {
        super.onResume();
        String account = Global.getCurrentAccount();
        adapter.clearAll();
        adapter.addAll(GroupRepository.queryCreatedList(account));
        adapter.addAll(GroupRepository.queryJoinList(account));
        emptyView.toggle(adapter);

    }


}
