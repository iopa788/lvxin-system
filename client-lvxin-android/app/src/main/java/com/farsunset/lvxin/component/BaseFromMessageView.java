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
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.ContentMenuWindow;
import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.listener.OnMessageDeleteListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.MessageReadHandler;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;

public abstract class BaseFromMessageView extends RelativeLayout implements OnMenuClickedListener, OnLongClickListener, OnClickListener {
    protected Message message;
    protected WebImageView icon;
    protected EmoticonTextView textView;
    protected ChatWebImageView imageView;
    protected ChatVoiceView voiceView;
    protected ChatFileView fileView;
    protected ChatMapView mapView;
    protected ChatVideoView videoView;
    protected ContentMenuWindow optionsDialog;
    protected ViewStub fromNameStub;
    protected TextView fromName;

    private OnMessageDeleteListener onMessageDeleteListener;

    public BaseFromMessageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public abstract View getContentView();

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        fromNameStub =  findViewById(R.id.fromNameStub);
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
        getContentView().setOnLongClickListener(this);
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
        icon.setOnClickListener(this);
    }

    private TextView getFromNameTextView(){
        if (fromName==null)
        {
            fromName = (TextView) fromNameStub.inflate();
        }
        return fromName;
    }
    public final void displayMessage(Message message, MessageSource others) {

        icon.setTag(R.id.target, others);
        this.message = message;

        if (message.action.equals(Constant.MessageAction.ACTION_3)) {

            Friend friend = FriendRepository.queryFriend(message.title);
            icon.setTag(R.id.target, friend);
            icon.load(friend.getWebIcon(), others.getDefaultIconRID());
            getFromNameTextView().setVisibility(VISIBLE);
            getFromNameTextView().setText(friend.getName());
        } else {
            icon.load(others.getWebIcon(), others.getDefaultIconRID());
        }

        setTag(message);
        getContentView().setOnClickListener(null);
        displayMessage();

        statusHandler();

    }


    private void statusHandler() {
        //阅读新消息的时候，像对方发送阅读状态
        if (Message.STATUS_NOT_READ.equals(message.status)) {

            MessageReadHandler.sendReadStatus(message);
            message.status = Message.STATUS_READ;
            MessageRepository.updateStatus(message.mid, Message.STATUS_READ);
        }

    }

    @Override
    public Object getTag() {
        return icon.getTag(R.id.CHAT_RECORD_TARGET);
    }

    @Override
    public void setTag(Object obj) {
        icon.setTag(R.id.CHAT_RECORD_TARGET, obj);
    }


    protected abstract void displayMessage();


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logo && icon.getTag(R.id.target) instanceof Friend) {
            Intent intent = new Intent(getContext(), UserDetailActivity.class);
            Friend friend = (Friend) icon.getTag(R.id.target);
            intent.putExtra(Friend.class.getName(), friend);

            if (message.action.equals(Constant.MessageAction.ACTION_3)) {
                intent.putExtra(Friend.class.getName(), FriendRepository.queryFriend(friend.account));
            }
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onMenuItemClicked(int id) {

        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {
            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, message);
            if (textView != null) {
                Message target = MessageUtil.clone(message);
                target.content = textView.getText().toString();
                intent.putExtra(Message.NAME, target);
            }
            getContext().startActivity(intent);
        }
    }


    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public boolean onLongClick(View arg0) {
        optionsDialog.buildChatRecordMenuGroup(Constant.MessageFormat.FORMAT_TEXT.equals(message.format), false);
        optionsDialog.show(arg0);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(Resources.getSystem().getDisplayMetrics().widthPixels, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
