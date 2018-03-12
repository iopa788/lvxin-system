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

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.message.builder.Action102Builder;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.SendMessageRequester;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;

/**
 * 请求入群
 */
public class JoinGroupRequestActivity extends BaseActivity implements HttpRequestListener {


    private Group group;
    private Message message = new Message();

    @Override
    public void initComponents() {

        group = (Group) this.getIntent().getExtras().getSerializable("group");
    }

    @Override
    public void onClick(View v) {
        sendAllyRequest();
    }


    public void sendAllyRequest() {
        //验证请求消息
        String token = ((EditText) findViewById(R.id.token)).getText().toString();
        User source = Global.getCurrentUser();
        message.action = Constant.MessageAction.ACTION_102;
        //接收者为 群创建者account
        message.receiver = group.founder;
        message.content = new Action102Builder().buildJsonString(source, group, token);

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));

        SendMessageRequester.send(message, this);

    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();
        if (result.isSuccess()) {
            showToastView(R.string.tip_send_request_complete);
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_ally_request;
    }


    @Override
    public int getToolbarTitle() {

        return R.string.title_contacts_verify;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        Button button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }
}
