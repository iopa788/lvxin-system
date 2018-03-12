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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.adapter.ChatRecordListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.GlobalVoicePlayer;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.BaseToMessageView;
import com.farsunset.lvxin.component.ChatRecordListView;
import com.farsunset.lvxin.component.CustomInputPanelView;
import com.farsunset.lvxin.component.CustomSwipeRefreshLayout;
import com.farsunset.lvxin.component.RecordingButton;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnMessageSendListener;
import com.farsunset.lvxin.listener.OnTouchDownListenter;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.util.List;


public class FriendChatActivity extends CIMMonitorActivity implements OnInputPanelEventListener, OnTouchDownListenter, SwipeRefreshLayout.OnRefreshListener, RecordingButton.OnRecordCompletedListener, OnMessageSendListener {
    protected User mSelf;
    protected ChatRecordListViewAdapter mAdapter;
    protected ChatRecordListView mChatListView;
    protected CustomInputPanelView mCustomInputPanelView;
    protected RecordingButton mVoiceButton;
    protected CustomSwipeRefreshLayout mSwipeRefreshLayout;
    protected MessageSource mMessageSource;
    private int mCurrentPage = 1;

    @Override
    public void initComponents() {

        mMessageSource = getMessageSource();
        if (mMessageSource == null) {
            MessageRepository.deleteBySenderOrReceiver(getIntent().getStringExtra(Constant.CHAT_OTHRES_ID));
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(null, new Friend(getIntent().getStringExtra(Constant.CHAT_OTHRES_ID))));
            LvxinApplication.sendLocalBroadcast(intent);
            showToastView(R.string.tips_chat_object_notexist);
            finish();
            return;
        }

        setTitle(mMessageSource.getName());

        mSelf = Global.getCurrentUser();
        mChatListView = (ChatRecordListView) findViewById(R.id.chat_list);
        mCustomInputPanelView = (CustomInputPanelView) findViewById(R.id.inputPanel);
        mCustomInputPanelView.setOnInputPanelEventListener(this);
        mVoiceButton = (RecordingButton) findViewById(R.id.voiceButton);
        mVoiceButton.setOnRecordCompletedListener(this);
        mSelf = Global.getCurrentUser();
        mAdapter = new ChatRecordListViewAdapter(mMessageSource);
        mChatListView.setAdapter(mAdapter);
        mChatListView.setOnTouchDownListenter(this);
        mChatListView.setOnMessageSendListener(this);
        loadChatRecord(mMessageSource.getId());

        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mCustomInputPanelView.setInputText(Global.getLastChatDraft(mMessageSource));

    }


    public String[] getIncludedMsgTypes() {
        return new String[]{Constant.MessageAction.ACTION_0};
    }

    public void loadChatRecord(String otherId) {
        List<Message> data = MessageRepository.queryMessage(otherId, getIncludedMsgTypes(), mCurrentPage);
        if (data != null && !data.isEmpty()) {
            mAdapter.addAllMessage(data);
            if (mCurrentPage == 1) {
                mChatListView.scrollToBottom();
            } else {
                mChatListView.scrollToPosition(data.size() - 1);
            }

        } else {
            mCurrentPage = mCurrentPage == 1 ? mCurrentPage : mCurrentPage - 1;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if (Constant.MessageAction.ACTION_0.equals(msg.action) && msg.sender.equals(mMessageSource.getId())) {
            mAdapter.addMessage(MessageUtil.transform(message));
            mChatListView.scrollToBottom();
            if (mAdapter.getItemCount() == 1 && mCurrentPage == 0) {
                mCurrentPage = 1;
            }
        }

        //收到好友阅读某条消息
        if (mMessageSource.getId().equals(msg.sender) && msg.action.equals(Constant.MessageAction.ACTION_108)) {
            //阅读的消息ID为 消息的content字段
            msg.mid = message.getContent();
            BaseToMessageView view = (BaseToMessageView) mChatListView.findViewWithTag(msg);
            if (view != null) {
                view.showReadMark();
            }
        }
    }

    @Override
    public void onRefresh() {

        ++mCurrentPage;
        loadChatRecord(mMessageSource.getId());
        mSwipeRefreshLayout.onRefreshCompleted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // 视频
            SNSVideo video = (SNSVideo) data.getSerializableExtra(SNSVideo.class.getName());
            saveAndDisplayMessage(new Gson().toJson(video), Constant.MessageFormat.FORMAT_VIDEO);
        }

        // 图片
        if (resultCode == RESULT_OK && requestCode == 2) {
            SNSImage chatImage = new SNSImage();
            chatImage.image = data.getData().toString();
            saveAndDisplayMessage(new Gson().toJson(chatImage), Constant.MessageFormat.FORMAT_IMAGE);
        }
        // 文件
        if (resultCode == RESULT_OK && requestCode == 3) {
            ChatFile file = (ChatFile) data.getSerializableExtra(ChatFile.class.getName());
            saveAndDisplayMessage(new Gson().toJson(file), Constant.MessageFormat.FORMAT_FILE);
        }

        // 地图
        if (resultCode == RESULT_OK && requestCode == 4) {
            ChatMap map = (ChatMap) data.getSerializableExtra(ChatMap.class.getName());
            saveAndDisplayMessage(new Gson().toJson(map), Constant.MessageFormat.FORMAT_MAP);
        }


    }

    public String getMessageAction(){
        return Constant.MessageAction.ACTION_0;
    }
    public MessageSource getMessageSource() {
        String account = getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);
        return FriendRepository.queryFriend(account);
    }

    @Override
    public void onSendButtonClicked(String content) {
        saveAndDisplayMessage(content, Constant.MessageFormat.FORMAT_TEXT);
        mCustomInputPanelView.clearText();
    }

    @Override
    public void onClick(View v) {
    }

    public void saveAndDisplayMessage(String context, String format) {
        saveAndDisplayMessage(context, null, format);
    }

    public void saveAndDisplayMessage(String context, String extra, String format) {
        Message message = new Message();
        message.mid = StringUtils.getUUID();
        message.content = context;
        message.sender = mSelf.account;
        message.receiver = mMessageSource.getId();
        message.format = format;
        message.extra = extra;
        message.action = getMessageAction();
        message.timestamp = System.currentTimeMillis();
        message.status = Constant.MessageStatus.STATUS_NO_SEND;

        mAdapter.addMessage(message);

        //发送的消息存储数据库
        saveMessage(message);

        mChatListView.scrollToBottom();

        if (Constant.MessageFormat.FORMAT_IMAGE.equals(format)
                || Constant.MessageFormat.FORMAT_FILE.equals(format)
                || Constant.MessageFormat.FORMAT_MAP.equals(format)
                || Constant.MessageFormat.FORMAT_VIDEO.equals(format)) {
            mCustomInputPanelView.resetInputPanel();
        }

        if (mAdapter.getItemCount() == 1 && mCurrentPage == 0) {
            mCurrentPage = 1;
        }
    }


    public void saveMessage(Message message) {
        //发送的消息存储数据库
        MessageRepository.add(message);
    }

    /**
     * 当删除最后一个消息，或者有草稿时刷新最近消息列表
     */
    public void sendRecentRefreshBroadcast(){
        String draft = mCustomInputPanelView.getInputText();
        Global.saveChatDraft(mMessageSource, draft);
        if (mAdapter.getItemCount() == 0 && TextUtils.isEmpty(draft)) {
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(mMessageSource));
            intent.setAction(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            LvxinApplication.sendLocalBroadcast(intent);
            return;
        }

        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(mAdapter.getLastMessage(),mMessageSource));
        if (!TextUtils.isEmpty(draft)) {
            Message message = new Message();
            message.action = getMessageAction();
            message.mid = StringUtils.getUUID();
            message.timestamp = System.currentTimeMillis();
            intent.putExtra(ChatItem.NAME, new ChatItem(message, mMessageSource));
        }
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalVoicePlayer.getPlayer().stop();
        sendRecentRefreshBroadcast();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_chatting;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(getMenuIcon());
        return super.onCreateOptionsMenu(menu);
    }


    public int getMenuIcon() {
        return R.drawable.icon_menu_user;

    }


    public void onToolbarMenuClicked() {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(Friend.class.getName(), mMessageSource);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            onToolbarMenuClicked();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTouchDown() {
        mCustomInputPanelView.resetInputPanel();
    }

    @Override
    public void onRecordCompleted(ChatVoice voice) {
        saveAndDisplayMessage(new Gson().toJson(voice), Constant.MessageFormat.FORMAT_VOICE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CustomInputPanelView.PERMISSION_RADIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCustomInputPanelView.onRadioButtonClicked();
            } else {
                showToastView(R.string.tip_permission_audio_rejected);
            }
        }
        if (requestCode == CustomInputPanelView.PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCustomInputPanelView.onMapButtonClicked();
            } else {
                showToastView(R.string.tip_permission_location_rejected);
            }
        }

    }

    @Override
    public void onMessageSendSuccess(Message msg) {

    }

    @Override
    public void onMessageSendFailure(Message msg) {

    }
}
