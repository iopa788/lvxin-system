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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.RencentContactsAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.dialog.SharedMessageDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnContactHandleListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.MessageForwardHandler;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.MessageForwardResult;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class MessageForwardActivity extends BaseActivity implements HttpRequestListener, OnContactHandleListener, TextWatcher, OnDialogButtonClickListener {

    private static final String[] INCLUDED_MESSAGE_TYPES = new String[]{Constant.MessageAction.ACTION_0, Constant.MessageAction.ACTION_1, Constant.MessageAction.ACTION_3,};
    protected RencentContactsAdapter adapter;
    protected User self;
    protected SharedMessageDialog sharedDialog;
    protected Message message;
    protected ArrayList<MessageSource> reciverList = new ArrayList<>();
    private ListView memberListView;
    private Button button;
    private LinearLayout memberIconPanel;
    private HorizontalScrollView horizontalScrollView;
    private List<MessageSource> tempList = new ArrayList<>();
    private View emptyView;
    private View headerView;

    @Override
    public void initComponents() {

        self = Global.getCurrentUser();


        message = getMessage();
        message.sender = self.account;

        memberListView = (ListView) findViewById(R.id.listView);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_recent_contacts_header, null);
        memberListView.addHeaderView(headerView, null, false);
        adapter = new RencentContactsAdapter(this);
        memberListView.setAdapter(adapter);
        findViewById(R.id.bar_create_chat).setOnClickListener(this);
        adapter.addAll(MessageRepository.getRecentContacts(INCLUDED_MESSAGE_TYPES));
        adapter.setOnContactHandleListener(this);
        memberIconPanel = (LinearLayout) findViewById(R.id.memberIconPanel);
        EditText keyword = (EditText) findViewById(R.id.keyword);
        keyword.addTextChangedListener(this);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        emptyView = findViewById(R.id.emptyView);
        emptyView.setOnClickListener(this);

        sharedDialog = new SharedMessageDialog(this);
        sharedDialog.setOnDialogButtonClickListener(this);
        sharedDialog.setMessage(message);


    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            if (button.getTag().equals(R.string.common_send)) {
                reciverList.clear();
                reciverList.addAll(adapter.getSelectedList());
                sharedDialog.show(adapter.getSelectedList());
            } else if (button.getTag().equals(R.string.common_multiselect)) {
                adapter.setMultiSelect();
                button.setTag(R.string.common_singleselect);
                button.setText(R.string.common_singleselect);
            } else {
                adapter.setSinglSelect();
                button.setTag(R.string.common_multiselect);
                button.setText(R.string.common_multiselect);
            }
        }
        if (v.getId() == R.id.icon) {
            View itemView = memberListView.findViewWithTag(v.getTag(R.id.target));
            if (itemView != null) {
                ((CheckBox) itemView.findViewById(R.id.checkbox)).setChecked(false);
            } else {
                adapter.getSelectedList().remove(v.getTag(R.id.target));
                onContactCanceled((MessageSource) v.getTag(R.id.target));
            }
        }
        if (v.getId() == R.id.bar_create_chat) {
            startActivityForResult(new Intent(this, ContactSelectorActivity.class), ContactSelectorActivity.RESULT_CODE);
        }
    }


    @Override
    public void onHttpRequestSucceed(BaseResult data, String url) {
        hideProgressDialog();
        if (data.isSuccess()) {
            MessageForwardResult result = (MessageForwardResult) data;
            int count = result.dataList.size();
            for (int i = 0; i < count; i++) {
                message.receiver = reciverList.get(i).getId();
                message.mid = result.dataList.get(i);
                message.action = reciverList.get(i).getSourceType();
                message.timestamp = System.currentTimeMillis();
                message.status = Constant.MessageStatus.STATUS_SEND;
                MessageRepository.add(message);

                ChatItem chatItem = new ChatItem(reciverList.get(i),message);
                Intent intent = new Intent(Constant.Action.ACTION_RECENT_APPEND_CHAT);
                intent.putExtra(ChatItem.NAME, chatItem);
                LvxinApplication.sendLocalBroadcast(intent);
            }
            showToastView(R.string.tip_message_forward_completed);
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {

        e.printStackTrace();
        hideProgressDialog();
    }

    public Message getMessage() {
        message = (Message) this.getIntent().getSerializableExtra(Message.NAME);
        return message;
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_message_forward;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.title_message_forward;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setText(R.string.common_multiselect);
        button.setTag(R.string.common_multiselect);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onContactClicked(MessageSource source) {

        reciverList.clear();
        reciverList.add(source);
        sharedDialog.show(source);
    }

    @Override
    public void onContactSelected(MessageSource source) {

        int side = (int) (this.getResources().getDisplayMetrics().density * 38);
        int padding = (int) (this.getResources().getDisplayMetrics().density * 5);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(side, side);
        if (memberIconPanel.getChildCount() > 0) {
            lp.setMargins(padding, 0, 0, 0);
        }
        WebImageView image = new WebImageView(this);
        image.setTag(R.id.target, source);
        image.setId(R.id.icon);
        image.setLayoutParams(lp);
        image.load(source.getWebIcon(), R.drawable.icon_def_head);
        image.setOnClickListener(this);
        memberIconPanel.addView(image);
        if (memberIconPanel.getChildCount() >= 5) {
            horizontalScrollView.getLayoutParams().width = 5 * (side + padding) + padding * 2;
        }
        horizontalScrollView.scrollTo(horizontalScrollView.getBottom(), 0);

        button.setTag(R.string.common_send);
        button.setText(R.string.common_send);
    }

    @Override
    public void onContactCanceled(MessageSource source) {

        int side = (int) (this.getResources().getDisplayMetrics().density * 38);
        int padding = (int) (this.getResources().getDisplayMetrics().density * 5);

        for (int i = 0; i < memberIconPanel.getChildCount(); i++) {
            if (memberIconPanel.getChildAt(i).getTag(R.id.target).equals(source)) {
                memberIconPanel.removeViewAt(i);
            }
        }

        if (memberIconPanel.getChildCount() >= 5) {
            horizontalScrollView.getLayoutParams().width = 5 * (side + padding);
        } else {
            horizontalScrollView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (adapter.getSelectedList().isEmpty()) {
            button.setTag(R.string.common_singleselect);
            button.setText(R.string.common_singleselect);
        } else {
            button.setTag(R.string.common_send);
            button.setText(R.string.common_send);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (!TextUtils.isEmpty(text.toString().trim())) {
            tempList.clear();
            tempList.addAll(FriendRepository.queryLike(text.toString()));
            if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
                tempList.addAll(PublicAccountRepository.queryLike(text.toString()));
            }

            tempList.addAll(GroupRepository.queryLike(text.toString()));
            if (tempList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                adapter.addAll(tempList, true);
                headerView.findViewById(R.id.bar_rencent_chat).setVisibility(View.GONE);
            }

        } else {
            headerView.findViewById(R.id.bar_rencent_chat).setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.addAll(MessageRepository.getRecentContacts(INCLUDED_MESSAGE_TYPES));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactSelectorActivity.RESULT_CODE && resultCode == RESULT_OK) {

            ArrayList<Friend> dataList = (ArrayList<Friend>) data.getSerializableExtra("data");
            dataList.removeAll(adapter.getSelectedList());
            adapter.getSelectedList().addAll(dataList);
            adapter.notifyDataSetChanged();
            for (Friend friend : dataList) {
                onContactSelected(friend);
            }
        }
    }

    @Override
    public void onLeftButtonClicked() {
        sharedDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        sharedDialog.dismiss();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_send)));
        MessageForwardHandler.forward(message, null, this, reciverList);
    }

}
