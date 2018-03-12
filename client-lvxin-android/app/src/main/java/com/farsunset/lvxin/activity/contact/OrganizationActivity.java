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
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.OrganizationListAdapter;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.OrganizationRepository;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizationActivity extends BaseActivity implements OnItemClickedListener {

    private OrganizationListAdapter adapter;
    private List<MessageSource> dataList = new ArrayList<MessageSource>();
    private Organization current;

    @Override
    public void initComponents() {

        RecyclerView listView = (RecyclerView) findViewById(R.id.recyclerView);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.backbutton).setOnClickListener(this);
        adapter = new OrganizationListAdapter(this);
        listView.setAdapter(adapter);
        dataList.addAll(OrganizationRepository.queryRootList());
        dataList.addAll(FriendRepository.queryRootFriendList());
        adapter.addAll(dataList);
    }


    @Override
    public void onClick(View v) {
        goBack(current);
    }

    public void goBack(Organization org) {
        if (current != null) {
            if (org.parentCode == null) {
                setTitle(R.string.label_org_list);
                dataList.clear();
                dataList.addAll(OrganizationRepository.queryRootList());
                dataList.addAll(FriendRepository.queryRootFriendList());
                adapter.addAll(dataList);
                current = null;
                return;
            }
            current = OrganizationRepository.queryOne(org.parentCode);
            if (current != null) {
                setTitle(current.getName());
                dataList.clear();
                dataList.addAll(OrganizationRepository.queryList(current.code));
                dataList.addAll(FriendRepository.queryFriendList(current.code));
                adapter.addAll(dataList);
            }
        }
    }

    public void gotoEnter(Organization org) {
        setTitle(org.getName());
        dataList.clear();
        dataList.addAll(OrganizationRepository.queryList(org.code));
        dataList.addAll(FriendRepository.queryFriendList(org.code));
        adapter.addAll(dataList);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_organization;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_org_list;
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Organization) {
            current = (Organization) obj;
            gotoEnter((Organization) obj);

        } else {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra(Friend.class.getName(), (Friend) obj);
            startActivity(intent);
        }
    }
}
