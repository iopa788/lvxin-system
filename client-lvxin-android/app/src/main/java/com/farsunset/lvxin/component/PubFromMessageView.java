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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.RelativeLayout;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.ContentMenuWindow;
import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.listener.OnMessageDeleteListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;

public abstract class PubFromMessageView extends RelativeLayout implements OnMenuClickedListener, OnLongClickListener {
    protected MessageSource others;
    protected Message message;
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;


    public PubFromMessageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
    }

    public final void displayMessage(Message message,MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);

        if (Message.STATUS_NOT_READ.equals(message.status)) {
            message.status = Message.STATUS_READ;
            MessageRepository.updateStatus(message.mid, Message.STATUS_READ);
        }

        displayMessage();
    }


    protected abstract void displayMessage();


    @Override
    public void onMenuItemClicked(int id) {

        if (id == R.id.menu_delete) {
            onMessageDeleteListener.onDelete(message);
        }

    }

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public boolean onLongClick(View arg0) {
        boolean isTextMessage = Constant.MessageFormat.FORMAT_TEXT.equals(message.format);
        optionsDialog.buildChatRecordMenuGroup(isTextMessage, false);
        optionsDialog.show(arg0);

        return true;
    }
}
