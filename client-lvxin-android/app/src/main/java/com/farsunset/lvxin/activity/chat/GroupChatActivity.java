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

import com.farsunset.lvxin.activity.contact.GroupDetailActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;

public class GroupChatActivity extends FriendChatActivity {

    private boolean isDeleted = false;

    public String getMessageAction(){
        return Constant.MessageAction.ACTION_1;
    }
    @Override
    public String[] getIncludedMsgTypes() {
        return new String[]{Constant.MessageAction.ACTION_1, Constant.MessageAction.ACTION_3};
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        Message msg = MessageUtil.transform(message);
        if (Constant.MessageAction.ACTION_3.equals(msg.action) && msg.sender.equals(super.mMessageSource.getId())) {
            mAdapter.addMessage(msg);
            mChatListView.scrollToBottom();
            MessageRepository.updateStatus(msg.mid, Message.STATUS_READ);
        }
    }

    @Override
    public MessageSource getMessageSource() {
        String groupId = getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);
        return GroupRepository.queryById(groupId);
    }

    @Override
    public int getMenuIcon() {
        return R.drawable.icon_menu_group;
    }

    @Override
    public void onToolbarMenuClicked() {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra("group", getMessageSource());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        String groupId = getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);
        if (GroupRepository.queryById(groupId) == null) {
            isDeleted = true;
            finish();
        }
        //判断用户是否屏蔽该群消息
        if (Global.getIsIgnoredGroupMessage(mMessageSource.getId())) {
            setTitleDrawableRight(R.drawable.ic_notifications_off);
        } else {
            clearTitleDrawableRight();
        }
    }

    @Override
    public void sendRecentRefreshBroadcast(){
        if (!isDeleted) {
           super.sendRecentRefreshBroadcast();
        }
    }

}
