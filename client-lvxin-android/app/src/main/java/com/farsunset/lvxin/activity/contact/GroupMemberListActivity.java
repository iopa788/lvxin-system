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

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.GroupMemberListViewAdapter;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.GroupMemberListResult;
import com.farsunset.lvxin.pro.R;

public class GroupMemberListActivity extends BaseActivity implements OnDialogButtonClickListener, HttpRequestListener {

    protected GroupMemberListViewAdapter adapter;
    private Group group;
    private User self;
    private CustomDialog customDialog;
    private Button button;
    private MenuItem mClearMenu;


    @Override
    public void initComponents() {
        group = (Group) this.getIntent().getSerializableExtra("group");

        RecyclerView memberListView = (RecyclerView) findViewById(R.id.recyclerView);
        memberListView.setLayoutManager(new LinearLayoutManager(this));
        memberListView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GroupMemberListViewAdapter(this, group.founder);
        memberListView.setAdapter(adapter);
        adapter.addAll(GroupMemberRepository.queryMemberList(group.groupId));
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.common_hint);
        customDialog.setMessage((R.string.tip_kickout_group));

        self = Global.getCurrentUser();

        performLoadMembersRequest();
    }

    private void performLoadMembersRequest() {
        if (adapter.getItemCount() == 0) {
            HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_LIST_URL, GroupMemberListResult.class);
            requestBody.addParameter("groupId", group.groupId);
            HttpRequestLauncher.execute(requestBody, this);
        }
    }

    private void performRemoveMembersRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_GETOUT_URL, BaseResult.class);
        requestBody.addParameter("account", adapter.getSelected().account);
        requestBody.addParameter("groupId", group.groupId);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (group.founder.equals(self.account)) {
            getMenuInflater().inflate(R.menu.menu_mgr_member, menu);
            mClearMenu = menu.findItem(R.id.menu_clear);
            button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
            button.setOnClickListener(this);
            button.setText(R.string.common_mangment);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

        if (adapter.getMangmentMode()) {
            adapter.setMangmentMode(false);
            button.setText(R.string.common_mangment);
        } else {
            adapter.setMangmentMode(true);
            button.setText(R.string.common_cancel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear && adapter.getSelected() != null) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        hideProgressDialog();
        if (result.isSuccess()) {
            if (URLConstant.GROUPMEMBER_LIST_URL.equals(url)) {
                final GroupMemberListResult data = (GroupMemberListResult) result;
                adapter.addAll(data.dataList);
                GroupMemberRepository.saveAll(data.dataList);
            }
            if (URLConstant.GROUPMEMBER_GETOUT_URL.equals(url)) {
                GroupMember member = adapter.getSelected();
                adapter.remove(member);
                button.setText(R.string.common_cancel);
                button.setBackgroundResource(R.drawable.toolbar_button);
                GroupMemberRepository.delete(group.groupId, member.account);
                showToastView(R.string.tip_group_kickout_complete);
            }
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();
        performRemoveMembersRequest();

    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_group_members;
    }


    public void onMemberSelected() {
        mClearMenu.setVisible(true);
        button.setVisibility(View.GONE);
    }


}
