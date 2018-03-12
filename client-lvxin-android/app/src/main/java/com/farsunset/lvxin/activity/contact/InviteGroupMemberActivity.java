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

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;

import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.message.builder.Action105Builder;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class InviteGroupMemberActivity extends ContactSelectorActivity implements HttpRequestListener {

    private Group group;
    private List<String> memberList;

    @Override
    public void initComponents() {

        group = (Group) this.getIntent().getSerializableExtra("group");
        memberList = GroupMemberRepository.queryMemberAccountList(group.groupId);
        super.initComponents();
    }

    @Override
    public void onLoadContactList(List<Friend> list) {
        for (int i = 0; i < list.size(); i++) {
            if (memberList.contains(list.get(i).account)) {
                list.remove(i);
                i--;
            }
        }
        super.onLoadContactList(list);
    }

    @Override
    public void onConfirmMenuClicked() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_INVITE_URL, BaseResult.class);
        requestBody.addParameter("groupId", group.groupId);
        requestBody.addParameter("account", TextUtils.join(",", getAccountList().toArray()));
        requestBody.addParameter("content", new Action105Builder().buildJsonString(Global.getCurrentUser(), group));

        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        hideProgressDialog();
        if (result.isSuccess()) {
            showToastView(R.string.tip_group_invite_complete);
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }


    private ArrayList<String> getAccountList() {
        ArrayList<String> list = new ArrayList<>(adapter.getSelectedList().size());
        for (Friend friend : adapter.getSelectedList()) {
            list.add(friend.account);
        }
        return list;
    }

    @Override
    public void onContactSelected(MessageSource source) {
        super.onContactSelected(source);
        button.setText(getString(R.string.label_group_invite_count, adapter.getSelectedList().size()));
    }

    @Override
    public void onContactCanceled(MessageSource source) {
        super.onContactCanceled(source);
        button.setText(getString(R.string.label_group_invite_count, adapter.getSelectedList().size()));
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_group_invite;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = super.onCreateOptionsMenu(menu);
        button.setText(getString(R.string.label_group_invite_count, 0));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        return flag;
    }

}
