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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.activity.contact.RequestHandleActivity;
import com.farsunset.lvxin.adapter.SystemMessageListAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.component.TopBottomDecoration;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;

import java.util.List;

public class SystemMessageActivity extends CIMMonitorActivity implements OnDialogButtonClickListener {

    private RecyclerView messageListView;
    private SystemMessageListAdapter adapter;
    private CustomDialog customDialog;

    @Override
    public void initComponents() {


        messageListView = (RecyclerView) findViewById(R.id.recyclerView);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setItemAnimator(new DefaultItemAnimator());
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 5;
        messageListView.addItemDecoration(new TopBottomDecoration(padding));
        adapter = new SystemMessageListAdapter();
        messageListView.setAdapter(adapter);

        loadMessageRecord();

        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.common_delete);
        customDialog.setMessage(getString(R.string.tip_delete_system_message));
        customDialog.setButtonsText(getString(R.string.common_cancel), getString(R.string.common_confirm));

        MessageRepository.batchReadMessage(SystemMessage.MESSAGE_ACTION_ARRAY);

    }


    public void loadMessageRecord() {

        List<Message> datalist = MessageRepository.queryIncludedSystemMessageList(SystemMessage.MESSAGE_ACTION_ARRAY);

        if (datalist != null && !datalist.isEmpty()) {
            adapter.addAllMessage(datalist);
            messageListView.smoothScrollToPosition(0);
        }

    }

    public void onHandleButtonClick(Message message) {
        Intent intent = new Intent(this, RequestHandleActivity.class);
        intent.putExtra("message", message);
        this.startActivityForResult(intent, 6789);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 6789) {
            Message target = MessageRepository.queryById(data.getStringExtra("mid"));
            adapter.onItemChanged(target);
        }

    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if (msg.sender.equals(Constant.SYSTEM) && !msg.isNoNeedShow()) {
            adapter.addMessage(msg);
            messageListView.smoothScrollToPosition(0);
            MessageRepository.updateStatus(msg.mid, Message.STATUS_READ);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_sysmessage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_delete);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        MessageRepository.deleteIncludedSystemMessageList(SystemMessage.MESSAGE_ACTION_ARRAY);
        adapter.notifyDataSetChanged();
        customDialog.dismiss();
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(null, new SystemMessage(Constant.MessageAction.ACTION_2)));
        LvxinApplication.sendLocalBroadcast(intent);
        finish();
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(adapter.getLastMessage(), new SystemMessage(Constant.MessageAction.ACTION_2)));
        LvxinApplication.sendLocalBroadcast(intent);
    }

}
