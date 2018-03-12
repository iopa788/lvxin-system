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
package com.farsunset.lvxin.activity.trend;


import com.farsunset.lvxin.activity.chat.FriendChatActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.BottleRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.BottleMessage;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;
import com.google.gson.Gson;

public class BottleChatActivity extends FriendChatActivity implements OnDialogButtonClickListener {
    private Bottle mBottle;
    private CustomDialog mCustomDialog;

    @Override
    public void initComponents() {
        mBottle = (Bottle) getIntent().getSerializableExtra(Bottle.class.getSimpleName());
        mCustomDialog = new CustomDialog(this);
        mCustomDialog.setTitle(R.string.title_delete_bottle);
        mCustomDialog.setOnDialogButtonClickListener(this);
        mCustomDialog.setMessage(getString(R.string.tip_delete_bottle));
        super.initComponents();
    }

    public String getMessageAction(){
        return Constant.MessageAction.ACTION_700;
    }

    @Override
    public String[] getIncludedMsgTypes() {
        return new String[]{Constant.MessageAction.ACTION_700};
    }

    @Override
    public int getMenuIcon() {
        return R.drawable.icon_menu_delete;
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        boolean isBootleMessage = Constant.MessageAction.ACTION_700.equals(message.getAction());
        boolean isCurrentBottle = mBottle.gid.equals(message.getSender());

        if (isBootleMessage && isCurrentBottle) {
            Message msg = MessageUtil.transform(message);
            mAdapter.addMessage(msg);
            mChatListView.scrollToBottom();
            MessageRepository.updateStatus(msg.mid, Message.STATUS_READ);
        }
    }


    @Override
    public MessageSource getMessageSource() {
        return mBottle;
    }


    @Override
    public void saveAndDisplayMessage(String context, String format) {
        BottleMessage bottleMessage = new BottleMessage();
        bottleMessage.bottleId = mBottle.gid;
        bottleMessage.sender = mBottle.sender;
        super.saveAndDisplayMessage(context, new Gson().toJson(bottleMessage), format);
    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        MessageRepository.updateReceiver(msg.mid, mBottle.gid);
    }

    @Override
    public void loadChatRecord(String otherId) {
        super.loadChatRecord(mBottle.gid);
    }

    @Override
    public void onToolbarMenuClicked() {
        mCustomDialog.show();
    }

    @Override
    public void onLeftButtonClicked() {
        mCustomDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        mCustomDialog.dismiss();
        MessageRepository.deleteByAction(mBottle.gid, Constant.MessageAction.ACTION_700);
        BottleRepository.deleteById(mBottle.gid);
        performDeleteBottleRequest();

        finish();
    }

    /**
     * 漂流瓶消息并不需要做任何处理，因为不在最近消息列表显示
     */
    @Override
    public void sendRecentRefreshBroadcast(){
    }
    private void performDeleteBottleRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.BOTTLE_DELETE_URL, BaseResult.class);
        requestBody.addParameter("gid", mBottle.gid);
        requestBody.addParameter("receiver", mBottle.receiver.equals(mSelf.account) ? mBottle.sender : mBottle.receiver);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

}
