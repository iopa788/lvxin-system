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
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.chat.MMWebViewActivity;
import com.farsunset.lvxin.activity.chat.PubAccountChatActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.database.PublicMenuRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.PubTextMessage;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.PubMenuListResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

public class PubAccountDetailActivity extends BaseActivity implements HttpRequestListener {

    private PublicAccount publicAccount;
    private User self;

    @Override
    public void initComponents() {

        publicAccount = (PublicAccount) this.getIntent().getSerializableExtra(PublicAccount.NAME);
        self = Global.getCurrentUser();
        WebImageView icon = ((WebImageView) findViewById(R.id.icon));
        icon.setPopuShowAble();
        ((TextView) findViewById(R.id.pubaccount)).setText(getString(R.string.label_pubaccount, publicAccount.account));
        ((TextView) findViewById(R.id.name)).setText(publicAccount.name);
        ((TextView) findViewById(R.id.summary)).setText(publicAccount.description);
        icon.load(FileURLBuilder.getPubAccountLogoUrl(publicAccount.account), publicAccount.getDefaultIconRID(), 999);
        findViewById(R.id.item_homepage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribeButton:
                performSubscribeRequest();
                break;
            case R.id.enterButton:
                Intent intent1 = new Intent(this, PubAccountChatActivity.class);
                intent1.putExtra(PublicAccount.NAME, publicAccount);
                startActivity(intent1);
                break;
            case R.id.item_homepage:
                if (!TextUtils.isEmpty(publicAccount.link)) {
                    Intent intent = new Intent(this, MMWebViewActivity.class);
                    intent.setData(Uri.parse(publicAccount.link));
                    startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        if (result.isSuccess() && url.equals(URLConstant.PUBACCOUNT_MENU_URL)) {
            hideProgressDialog();
            if (!TextUtils.isEmpty(publicAccount.greet)) {
                com.farsunset.cim.sdk.android.model.Message msg = new com.farsunset.cim.sdk.android.model.Message();
                msg.setTimestamp(System.currentTimeMillis());
                msg.setMid(StringUtils.getUUID());
                msg.setAction(Constant.MessageAction.ACTION_201);
                msg.setSender(publicAccount.account);
                msg.setReceiver(self.account);
                msg.setFormat(Constant.MessageFormat.FORMAT_TEXT);
                PubTextMessage textMsg = new PubTextMessage();
                textMsg.content = publicAccount.greet;
                msg.setContent(new Gson().toJson(textMsg));

                Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
                intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), msg);
                intent.putExtra(Constant.NEED_RECEIPT, false);
                LvxinApplication.sendGlobalBroadcast(intent);

            }


            Intent intent1 = new Intent(this, PubAccountChatActivity.class);
            intent1.putExtra(PublicAccount.NAME, publicAccount);

            startActivity(intent1);

            PublicAccountRepository.add(publicAccount);
            PublicMenuRepository.savePublicMenus(((PubMenuListResult) result).dataList);

            finish();
        }

        if (result.isSuccess() && url.equals(URLConstant.PUBACCOUNT_SUBSCRIBE_URL)) {
            performGetMenuListRequest();
        }
        if (result.isSuccess() && url.equals(URLConstant.PUBACCOUNT_UNSUBSCRIBE_URL)) {

            MessageRepository.deleteByActions(publicAccount.account, new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201});
            PublicAccountRepository.delete(publicAccount.account);
            PublicMenuRepository.deleteByAccount(publicAccount.account);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(publicAccount));
            LvxinApplication.sendLocalBroadcast(intent);

            hideProgressDialog();
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_pubaccount_detail;
    }


    @Override
    public int getToolbarTitle() {

        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        PublicAccount target = PublicAccountRepository.queryPublicAccount(publicAccount.account);
        if (target == null) {
            findViewById(R.id.subscribeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.subscribeButton).setOnClickListener(this);
            findViewById(R.id.enterButton).setVisibility(View.GONE);
            setTitle(publicAccount.name);
        } else {
            findViewById(R.id.subscribeButton).setVisibility(View.GONE);
            findViewById(R.id.enterButton).setOnClickListener(this);
            findViewById(R.id.enterButton).setVisibility(View.VISIBLE);
            getMenuInflater().inflate(R.menu.pubaccount_detailed, menu);
            setTitle(target.name);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bar_menu_unsubscribe) {
            performUnsubscribeRequest();
        }

        return super.onOptionsItemSelected(item);

    }

    private void performUnsubscribeRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.PUBACCOUNT_UNSUBSCRIBE_URL, BaseResult.class);
        requestBody.addParameter("publicAccount", publicAccount.account);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performSubscribeRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.PUBACCOUNT_SUBSCRIBE_URL, BaseResult.class);
        requestBody.addParameter("publicAccount", publicAccount.account);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performGetMenuListRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.PUBACCOUNT_MENU_URL, PubMenuListResult.class);
        requestBody.addParameter("account", publicAccount.account);
        HttpRequestLauncher.execute(requestBody, this);
    }

}
