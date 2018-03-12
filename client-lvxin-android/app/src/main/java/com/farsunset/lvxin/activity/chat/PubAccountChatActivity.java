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
package com.farsunset.lvxin.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.activity.contact.PubAccountDetailActivity;
import com.farsunset.lvxin.adapter.PubChatListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.ChatRecordListView;
import com.farsunset.lvxin.component.CustomSwipeRefreshLayout;
import com.farsunset.lvxin.component.PubAccountInputPanelView;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.database.PublicMenuRepository;
import com.farsunset.lvxin.dialog.PubAccountMenuWindow;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnTouchDownListenter;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.network.PublicAccountMenuHander;
import com.farsunset.lvxin.network.model.PubTextMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.util.List;
public class PubAccountChatActivity extends CIMMonitorActivity implements OnInputPanelEventListener, OnTouchDownListenter, SwipeRefreshLayout.OnRefreshListener, PubAccountMenuWindow.OnMenuClickListener {
    private final String[] INCLUDE_TYPES = new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201};
    public User self;
    public int currentPage = 1;
    public PubChatListViewAdapter adapter;
    public ChatRecordListView chatListView;
    private PublicAccount publicAccount;
    private PubAccountInputPanelView customInputPanelView;
    private View progressView;
    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private MenuInvokedReceiver menuInvokedReceiver;

    @Override
    public void initComponents() {

        publicAccount = (PublicAccount) this.getIntent().getSerializableExtra(PublicAccount.NAME);
        setTitle(publicAccount.name);
        self = Global.getCurrentUser();
        chatListView = (ChatRecordListView) findViewById(R.id.chat_list);
        progressView = findViewById(R.id.menu_progress_view);
        chatListView.setOnTouchDownListenter(this);
        customInputPanelView = (PubAccountInputPanelView) findViewById(R.id.inputPanel);
        customInputPanelView.buildMenus(PublicMenuRepository.queryPublicMenuList(publicAccount.account));
        customInputPanelView.setOnMenuClickListener(this);
        customInputPanelView.setOnInputPanelEventListener(this);
        self = Global.getCurrentUser();
        adapter = new PubChatListViewAdapter(publicAccount);
        chatListView.setAdapter(adapter);
        loadChatRecord();

        swipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        menuInvokedReceiver = new MenuInvokedReceiver();
        LvxinApplication.registerLocalReceiver(menuInvokedReceiver, menuInvokedReceiver.getIntentFilter());

    }


    @Override
    public void onResume() {
        super.onResume();
        publicAccount = PublicAccountRepository.queryPublicAccount(publicAccount.account);
        if (publicAccount == null) {
            this.finish();
        }
    }


    public void loadChatRecord() {
        List<Message> data = MessageRepository.queryMessage(publicAccount.account, INCLUDE_TYPES, currentPage);
        if (data != null && !data.isEmpty()) {
            adapter.addAllMessage(data);
            if (currentPage == 1) {
                chatListView.scrollToBottom();
            } else {
                chatListView.scrollToPosition(data.size() - 1);
            }


        } else {
            currentPage = currentPage == 1 ? currentPage : currentPage - 1;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if (Constant.MessageAction.ACTION_201.equals(msg.action) && msg.sender.equals(publicAccount.account)) {
            adapter.addMessage(MessageUtil.transform(message));
            chatListView.scrollToBottom();
            if (adapter.getItemCount() == 1 && currentPage == 0) {
                currentPage = 1;
            }
        }
    }
    @Override
    public void onDestroy() {
        LvxinApplication.unregisterLocalReceiver(menuInvokedReceiver);
        ChatItem chatItem = new ChatItem(publicAccount,adapter.getLastMessage());
        Intent intent = new Intent();
        intent.setAction(chatItem.message == null ? Constant.Action.ACTION_RECENT_DELETE_CHAT : Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
        super.onDestroy();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_pubaccount_chat;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_back;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(getMenuIcon());
        return super.onCreateOptionsMenu(menu);
    }


    public int getMenuIcon() {
        return R.drawable.icon_menu_pubaccount;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            Intent intent = new Intent();
            intent.setClass(this, PubAccountDetailActivity.class);
            intent.putExtra(PublicAccount.NAME, publicAccount);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMenuClicked(PublicMenu menu) {
        if (menu.isWebMenu()) {
            Intent intent = new Intent(this, MMWebViewActivity.class);
            intent.setData(Uri.parse(menu.link));
            startActivity(intent);
        }
        if (menu.isApiMenu()) {
            progressView.setVisibility(View.VISIBLE);
            PublicAccountMenuHander.execute(publicAccount, menu);
        }
        if (menu.isTextMenu()) {
            Message  lastMessage = new Message();
            lastMessage.content = menu.content;
            lastMessage.sender = publicAccount.account;
            lastMessage.receiver = self.account;
            lastMessage.format = Constant.MessageFormat.FORMAT_TEXT;
            lastMessage.action = Constant.MessageAction.ACTION_201;
            lastMessage.status = Message.STATUS_READ;
            lastMessage.mid = StringUtils.getUUID();
            lastMessage.timestamp = System.currentTimeMillis();
            PubTextMessage textMsg = new PubTextMessage();
            textMsg.content = menu.content;
            lastMessage.content = (new Gson().toJson(textMsg));
            MessageRepository.add(lastMessage);
            adapter.addMessage(lastMessage);
            chatListView.scrollToBottom();
        }
    }


    @Override
    public void onSendButtonClicked(String content) {
        Message message = new Message();
        message.mid = String.valueOf(System.currentTimeMillis());
        message.content = content;
        message.sender = self.account;
        message.receiver = publicAccount.account;
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        message.action = Constant.MessageAction.ACTION_200;
        message.timestamp = System.currentTimeMillis();
        message.status = Constant.MessageStatus.STATUS_NO_SEND;
        adapter.addMessage(message);
        chatListView.scrollToBottom();
        if (adapter.getItemCount() == 1 && currentPage == 0) {
            currentPage = 1;
        }
    }

    @Override
    public void onTouchDown() {
        customInputPanelView.resetInputPanel();
    }

    @Override
    public void onRefresh() {
        ++currentPage;
        loadChatRecord();
        swipeRefreshLayout.onRefreshCompleted();
    }


    public class MenuInvokedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressView.setVisibility(View.GONE);
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_PUB_MENU_INVOKED);
            return filter;
        }
    }
}
