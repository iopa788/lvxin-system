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
package com.farsunset.lvxin.component;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.ContentMenuWindow;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.listener.OnMessageDeleteListener;
import com.farsunset.lvxin.listener.OnMessageSendListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.SendChatMessageRequester;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.MessageUtil;

import java.util.Objects;

public abstract class BaseToMessageView extends RelativeLayout implements OnMessageSendListener, HttpRequestListener, OnLongClickListener, OnMenuClickedListener, OnClickListener {
    protected ProgressBar sendProgressbar;
    protected WebImageView icon;
    protected EmoticonTextView textView;
    protected ChatWebImageView imageView;
    protected ChatVoiceView voiceView;
    protected ChatFileView fileView;
    protected ChatMapView mapView;
    protected ChatVideoView videoView;
    protected Message message;
    protected MessageSource others;
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;
    private View icon_resend;
    private TextView readMark;

    public BaseToMessageView(Context context) {
        super(context);
    }


    public BaseToMessageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public abstract View getContentView();

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        readMark = (TextView) findViewById(R.id.read_status);

        icon_resend = findViewById(R.id.icon_resend);
        icon_resend.setOnClickListener(this);
        sendProgressbar = (ProgressBar) findViewById(R.id.sendProgressbar);
        icon = (WebImageView) findViewById(R.id.logo);

        View container = findViewById(R.id.container);
        if (container instanceof EmoticonTextView) {
            textView = (EmoticonTextView) container;
        }
        if (container instanceof ChatWebImageView) {
            imageView = (ChatWebImageView) container;
        }
        if (container instanceof ChatVoiceView) {
            voiceView = (ChatVoiceView) container;
        }
        if (container instanceof ChatFileView) {
            fileView = (ChatFileView) container;
        }
        if (container instanceof ChatMapView) {
            mapView = (ChatMapView) container;
        }
        if (container instanceof ChatVideoView) {
            videoView = (ChatVideoView) container;
        }
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
    }


    public final void displayMessage(Message message, User self, MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);
        readMark.setVisibility(View.INVISIBLE);
        icon.load(FileURLBuilder.getUserIconUrl(self.account), R.drawable.icon_def_head);
        getContentView().setOnLongClickListener(this);
        getContentView().setOnClickListener(null);
        displayMessage();

        statusHandler();
    }


    protected abstract void displayMessage();

    protected void statusHandler() {

        if (Constant.MessageStatus.STATUS_SENDING.equals(message.status)) {
            showSendProgressbar();
        } else {
            sendProgressbar.setVisibility(View.INVISIBLE);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.status)) {
            icon_resend.setVisibility(View.VISIBLE);
            icon_resend.setOnClickListener(this);
        } else {
            icon_resend.setVisibility(View.INVISIBLE);
        }

        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.status)) {

            sendMessage();
        }

        if (Constant.MessageStatus.STATUS_OTHERS_READ.equals(message.status) && message.isNeedShowReadStatus()) {
            showReadMark();
        }
        if (Constant.MessageStatus.STATUS_SEND.equals(message.status) && message.isNeedShowReadStatus()) {
            showSendMark();
        }
    }

    public void showReadMark() {
        message.status = Constant.MessageStatus.STATUS_OTHERS_READ;
        if (ClientConfig.getMessageStatusVisable() && Objects.equals(message.action,Constant.MessageAction.ACTION_0)) {
            readMark.setVisibility(View.VISIBLE);
            readMark.setText(R.string.tip_has_read);
            readMark.setBackgroundResource(R.drawable.bg_status_already_read);
        }
    }

    public void showSendMark() {
        if (ClientConfig.getMessageStatusVisable() && message.isNeedShowReadStatus()) {
            readMark.setVisibility(View.VISIBLE);
            readMark.setText(R.string.tip_has_sent);
            readMark.setBackgroundResource(R.drawable.bg_status_already_sent);
        }
    }


    private void showSendProgressbar() {
        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            sendProgressbar.setVisibility(View.VISIBLE);
        }
    }

    protected void sendMessage() {

        Message message = (Message) this.getTag();
        showSendProgressbar();
        icon_resend.setVisibility(View.INVISIBLE);
        message.status = Constant.MessageStatus.STATUS_SENDING;
        MessageRepository.updateStatus(message.mid, Constant.MessageStatus.STATUS_SENDING);

        //发送状态，通知相关界面更新UI
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        ChatItem chat = new ChatItem(message, others);
        intent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(intent);
        SendChatMessageRequester.getSendRequester(message).send();

    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        icon_resend.setVisibility(View.INVISIBLE);
        sendProgressbar.setVisibility(View.INVISIBLE);
        showSendMark();
        message.status = msg.status;
    }

    @Override
    public void onMessageSendFailure(Message msg) {
        icon_resend.setVisibility(View.VISIBLE);
        sendProgressbar.setVisibility(View.INVISIBLE);
        message.status = msg.status;

        onMessageSendFailure();
    }

    public void onMessageSendFailure() {
    }

    @Override
    public boolean onLongClick(View arg0) {

        Message message = (Message) this.getTag();
        boolean canRevoke = Constant.MessageAction.ACTION_0.equals(message.action);
        optionsDialog.buildChatRecordMenuGroup(Constant.MessageFormat.FORMAT_TEXT.equals(message.format), canRevoke);
        optionsDialog.show(arg0);
        return true;
    }

    @Override
    public void onMenuItemClicked(int id) {

        Message message = (Message) this.getTag();
        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {

            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_revoke) {
            performRevokeRequest();
        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            if (textView != null) {
                Message target = MessageUtil.clone(message);
                target.content = textView.getText().toString();
                intent.putExtra(Message.NAME, target);
            } else {
                intent.putExtra(Message.NAME, message);
            }
            getContext().startActivity(intent);
        }
    }


    private void performRevokeRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_REVOKE_URL, BaseResult.class);
        requestBody.addParameter("mid", message.mid);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_resend) {
            sendMessage();
        }
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        ((BaseActivity) getContext()).hideProgressDialog();
        if (result.isSuccess() && URLConstant.MESSAGE_REVOKE_URL.equals(url)) {
            onMessageDeleteListener.onDelete((Message) this.getTag());
        }
        if (Objects.equals(result.code,Constant.ReturnCode.CODE_403) && URLConstant.MESSAGE_REVOKE_URL.equals(url)) {
            ((BaseActivity) getContext()).showToastView(R.string.tip_message_revoke_failed);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        ((BaseActivity) getContext()).hideProgressDialog();
    }

}
