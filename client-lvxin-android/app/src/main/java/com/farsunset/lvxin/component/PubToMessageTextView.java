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

import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.ContentMenuWindow;
import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.listener.OnMessageDeleteListener;
import com.farsunset.lvxin.listener.OnMessageSendListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.network.PublicAccountMenuHander;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class PubToMessageTextView extends RelativeLayout implements OnMessageSendListener, OnLongClickListener, OnMenuClickedListener, OnClickListener {
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;
    private View icon_resend;
    private ProgressBar sendProgressbar;
    private WebImageView icon;
    private EmoticonTextView textView;
    private Message message;
    private MessageSource others;


    public PubToMessageTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        icon_resend = findViewById(R.id.icon_resend);
        icon_resend.setOnClickListener(this);
        sendProgressbar = (ProgressBar) findViewById(R.id.sendProgressbar);
        icon = (WebImageView) findViewById(R.id.icon);
        textView = (EmoticonTextView) findViewById(R.id.container);
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
        sendProgressbar.setVisibility(View.GONE);

        int padding = textView.getPaddingLeft() + textView.getPaddingRight();
        textView.setMaxWidth(padding + Global.getChatTextMaxWidth());
    }


    public final void displayMessage(Message message,MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);
        icon.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.drawable.icon_def_head);
        textView.setFaceSize(Constant.EMOTION_FACE_SIZE);
        textView.setText(message.content);
        textView.setOnLongClickListener(this);
        statusHandler();
    }


    private void statusHandler() {
        if (Constant.MessageStatus.STATUS_SENDING.equals(message.status)) {
            sendProgressbar.setVisibility(View.VISIBLE);
        } else {
            sendProgressbar.setVisibility(View.GONE);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.status)) {
            icon_resend.setVisibility(View.VISIBLE);
            icon_resend.setOnClickListener(this);
        } else {
            icon_resend.setVisibility(View.GONE);
        }

        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.status)) {

            sendProgressbar.setVisibility(View.VISIBLE);
            sendMessage();
        }
    }


    private void sendMessage() {

        MessageRepository.add(message);
        icon_resend.setVisibility(View.GONE);
        message.status = Constant.MessageStatus.STATUS_SENDING;
        MessageRepository.updateStatus(message.mid, Constant.MessageStatus.STATUS_SENDING);


        //发送状态，通知相关界面更新UI
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        ChatItem chat = new ChatItem(message, others);
        intent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(intent);

        PublicAccountMenuHander.execute((PublicAccount) others, message);
    }

    @Override
    public boolean onLongClick(View arg0) {

        Message message = (Message) this.getTag();
        boolean isTextMessage = Constant.MessageFormat.FORMAT_TEXT.equals(message.format);
        optionsDialog.buildChatRecordMenuGroup(isTextMessage, false);
        optionsDialog.show(arg0);
        return true;
    }

    @Override
    public void onMenuItemClicked(int id) {

        Message message = (Message) this.getTag();
        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, message.content));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {

            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, message);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_resend) {
            MessageRepository.deleteById(message.mid);
            sendMessage();
        }
    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        icon_resend.setVisibility(View.GONE);
        sendProgressbar.setVisibility(View.GONE);
        message.status = msg.status;
    }

    @Override
    public void onMessageSendFailure(Message msg) {
        icon_resend.setVisibility(View.VISIBLE);
        sendProgressbar.setVisibility(View.GONE);
        message.status = msg.status;
    }
}
