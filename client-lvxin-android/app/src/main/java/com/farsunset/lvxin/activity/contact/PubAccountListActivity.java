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
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.chat.PubAccountChatActivity;
import com.farsunset.lvxin.adapter.PubAccountListViewAdapter;
import com.farsunset.lvxin.component.GlobalEmptyView;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.pro.R;

public class PubAccountListActivity extends BaseActivity implements OnItemClickedListener {

    private PubAccountListViewAdapter adapter;
    private GlobalEmptyView emptyView;

    @Override
    public void initComponents() {


        RecyclerView listview = (RecyclerView) findViewById(R.id.recyclerView);
        listview.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PubAccountListViewAdapter();
        listview.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);

        emptyView = (GlobalEmptyView) findViewById(R.id.emptyView);

        emptyView.setTips(R.string.tips_no_pubaccount);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_contacts_public;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.addAll(PublicAccountRepository.queryPublicAccountList());
        emptyView.toggle(adapter);
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        Intent intent = new Intent(this, PubAccountChatActivity.class);
        intent.putExtra(PublicAccount.NAME, (PublicAccount) obj);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_look);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            startActivity(new Intent(this, LookPubAccountActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
