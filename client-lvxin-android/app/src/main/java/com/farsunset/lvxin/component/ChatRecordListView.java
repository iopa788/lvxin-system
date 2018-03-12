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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.listener.OnMessageSendListener;
import com.farsunset.lvxin.listener.OnTouchDownListenter;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;

import java.util.Objects;

public class ChatRecordListView extends RecyclerView {
    private OnTouchDownListenter onTouchDownListenter;
    private MessageSendReceiver messageSendReceiver;
    private OnMessageSendListener onMessageSendListener;

    public ChatRecordListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setItemAnimator(new DefaultItemAnimator());
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 18;
        addItemDecoration(new PaddingDecoration(padding));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN && onTouchDownListenter != null) {
            onTouchDownListenter.onTouchDown();
        }
        return super.onTouchEvent(event);
    }


    public void scrollToBottom() {
        scrollToPosition(getAdapter().getItemCount() - 1);
    }

    public void setOnMessageSendListener(OnMessageSendListener onMessageSendListener) {
        this.onMessageSendListener = onMessageSendListener;
    }

    public void setOnTouchDownListenter(OnTouchDownListenter onTouchDownListenter) {
        this.onTouchDownListenter = onTouchDownListenter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LvxinApplication.unregisterLocalReceiver(messageSendReceiver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        messageSendReceiver = new MessageSendReceiver();
        LvxinApplication.registerLocalReceiver(messageSendReceiver, messageSendReceiver.getIntentFilter());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h || (oldh < h && oldh > 0)) {
            scrollToBottom();
        }
    }

    public class MessageSendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE)) {
                ChatItem target = (ChatItem) intent.getSerializableExtra(ChatItem.NAME);

                View view = findViewWithTag(target.message);

                if (Constant.MessageStatus.STATUS_SEND.equals(target.message.status)) {
                    if (onMessageSendListener != null) {
                        onMessageSendListener.onMessageSendSuccess(target.message);
                    }
                    if (view instanceof OnMessageSendListener) {
                        ((OnMessageSendListener) view).onMessageSendSuccess(target.message);
                    }
                }
                if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(target.message.status)) {
                    if (onMessageSendListener != null) {
                        onMessageSendListener.onMessageSendFailure(target.message);
                    }
                    if (view instanceof OnMessageSendListener) {
                        ((OnMessageSendListener) view).onMessageSendFailure(target.message);
                    }
                }
            }

            if (intent.getAction().equals(Constant.Action.ACTION_UPLOAD_PROGRESS)) {
                String key = intent.getStringExtra("objectKey");
                View view = findViewWithTag(key);
                if (view instanceof OnTransmitProgressListener) {
                    ((OnTransmitProgressListener) view).onProgress(intent.getFloatExtra("progress", 0f));
                }
            }
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
            filter.addAction(Constant.Action.ACTION_UPLOAD_PROGRESS);
            return filter;
        }
    }
}
