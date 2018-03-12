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

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.GroupMemberAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.GroupMemberListResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class GroupDetailActivity extends BaseActivity implements HttpRequestListener, OnDialogButtonClickListener, OnCheckedChangeListener {

    private Group group;

    private CustomDialog customDialog;
    private User self;
    private GroupMemberAdapter memberAdapter;

    @Override
    public void initComponents() {

        findViewById(R.id.lookMemberBar).setOnClickListener(this);
        group = (Group) this.getIntent().getSerializableExtra("group");
        self = Global.getCurrentUser();
        ((WebImageView) findViewById(R.id.icon)).load(FileURLBuilder.getGroupIconUrl(group.groupId), R.drawable.logo_group_normal);
        ((TextView) findViewById(R.id.groupId)).setText(group.groupId);
        ((TextView) findViewById(R.id.name)).setText(group.name);
        ((TextView) findViewById(R.id.summary)).setText(group.summary);
        ((TextView) findViewById(R.id.founderAccount)).setText(group.founder);
        ((WebImageView) findViewById(R.id.founderIcon)).load(FileURLBuilder.getUserIconUrl(group.founder), R.drawable.icon_def_head);
        ((Switch) findViewById(R.id.ignoreMessageCheckbox)).setChecked(Global.getIsIgnoredGroupMessage(group.groupId));
        ((Switch) findViewById(R.id.ignoreMessageCheckbox)).setOnCheckedChangeListener(this);
        if (GroupRepository.queryById(group.groupId) == null) {
            findViewById(R.id.joinButton).setOnClickListener(this);
            findViewById(R.id.joinButton).setVisibility(View.VISIBLE);
            findViewById(R.id.ignoreMessageBar).setVisibility(View.GONE);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        memberAdapter = new GroupMemberAdapter();
        recyclerView.setAdapter(memberAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (GroupRepository.queryById(group.groupId) != null) {
            getMenuInflater().inflate(R.menu.group_detailed, menu);

            customDialog = new CustomDialog(this);
            customDialog.setOnDialogButtonClickListener(this);

            if (group.founder.equals(self.account)) {
                customDialog.setTitle(R.string.common_dissolve);
                customDialog.setMessage((R.string.tip_dissolve_group));
                menu.findItem(R.id.bar_menu_delete).setTitle(R.string.common_dissolve);
                findViewById(R.id.item_to_update).setOnClickListener(this);
            } else {
                menu.findItem(R.id.bar_menu_invite).setVisible(false);
                menu.findItem(R.id.bar_menu_update).setVisible(false);
                customDialog.setTitle(R.string.common_quit);
                customDialog.setMessage((R.string.tip_quit_group));
                menu.findItem(R.id.bar_menu_delete).setTitle(R.string.common_quit);
            }


        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bar_menu_delete:
                customDialog.show();
                break;
            case R.id.bar_menu_update:
                Intent inviteIntent = new Intent(this, UpdateGroupActivity.class);
                inviteIntent.putExtra(Group.NAME, group);
                startActivityForResult(inviteIntent, UpdateGroupActivity.REQUEST_CODE);
                break;
            case R.id.bar_menu_invite:
                Intent updateIntent = new Intent(this, InviteGroupMemberActivity.class);
                updateIntent.putExtra("group", group);
                startActivity(updateIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        memberAdapter.addAll(GroupMemberRepository.queryMemberList(group.groupId));
        if (memberAdapter.getItemCount() == 0) {
            performGetMembersRequest();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lookMemberBar:
                Intent memberIntent = new Intent(this, GroupMemberListActivity.class);
                memberIntent.putExtra("group", group);
                startActivity(memberIntent);
                break;
            case R.id.item_to_update:
                Intent inviteIntent = new Intent(this, UpdateGroupActivity.class);
                inviteIntent.putExtra(Group.NAME, group);
                startActivityForResult(inviteIntent, UpdateGroupActivity.REQUEST_CODE);
                break;

            case R.id.joinButton:
                Intent intent = new Intent(this, JoinGroupRequestActivity.class);
                intent.putExtra("group", group);
                startActivity(intent);
                break;


        }
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        if (group.founder.equals(self.account)) {
            performDisdandRequest();
        } else {
            performQuitGroupRequest();
        }
    }

    private void performDisdandRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_dissolve)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_DISBAND_URL, BaseResult.class);
        requestBody.addParameter("groupId", group.groupId);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performQuitGroupRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_quit)));

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_QUIT_URL, BaseResult.class);
        requestBody.addParameter("groupId", group.groupId);
        HttpRequestLauncher.execute(requestBody, this);


    }

    private void performGetMembersRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_LIST_URL, GroupMemberListResult.class);
        requestBody.addParameter("groupId", group.groupId);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();
        if (result.isSuccess() && (url.equals(URLConstant.GROUP_DISBAND_URL) || url.equals(URLConstant.GROUPMEMBER_QUIT_URL))) {
            if (url.equals(URLConstant.GROUP_DISBAND_URL)) {
                showToastView(R.string.tip_group_dissolve_complete);
            } else {
                showToastView(R.string.tip_group_quit_complete);
            }
            GroupRepository.deleteById(group.groupId);
            GroupMemberRepository.delete(group.groupId, self.account);
            MessageRepository.deleteBySenderOrReceiver(group.groupId);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(group));
            LvxinApplication.sendLocalBroadcast(intent);
            finish();
        }

        if (result.isSuccess() && url.equals(URLConstant.GROUPMEMBER_LIST_URL)) {
            final GroupMemberListResult data = (GroupMemberListResult) result;
            GroupMemberRepository.saveAll(data.dataList);
            memberAdapter.addAll(GroupMemberRepository.queryMemberList(group.groupId));
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
        if (R.id.ignoreMessageCheckbox == compoundbutton.getId()) {
            Global.saveIgnoredGroupMessage(group.groupId, flag);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_group_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == UpdateGroupActivity.REQUEST_CODE) {
            group = GroupRepository.queryById(group.groupId);
            ((WebImageView) findViewById(R.id.icon)).load(FileURLBuilder.getGroupIconUrl(group.groupId), R.drawable.logo_group_normal);
            ((TextView) findViewById(R.id.name)).setText(group.name);
            ((TextView) findViewById(R.id.summary)).setText(group.summary);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
            intent.putExtra(ChatItem.NAME, new ChatItem(group));
            LvxinApplication.sendLocalBroadcast(intent);
        }
    }


}
