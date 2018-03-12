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
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.FileViewerActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileUtils;
import com.google.gson.Gson;

public class ChatFileView extends FrameLayout implements OnClickListener, OnTransmitProgressListener {

    private CircleProgressView uploadProgressView;
    private Message message;
    private TextView fileSize;
    private TextView fileName;
    private ImageView icon;

    public ChatFileView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        fileName = ((TextView) findViewById(R.id.fileName));
        fileSize = ((TextView) findViewById(R.id.fileSize));
        icon = ((ImageView) findViewById(R.id.fileIcon));
    }

    public void setSendProgressView(CircleProgressView circleProgressView) {
        this.uploadProgressView = circleProgressView;
    }

    public void initView(Message message) {

        this.message = message;
        ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);

        setTag(chatFile.key);

        fileSize.setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        fileName.setText(chatFile.name);
        icon.setImageResource(FileUtils.getFileIcon(chatFile.name));

    }


    @Override
    public void onClick(View v) {

        if (message.status.equals(Constant.MessageStatus.STATUS_SENDING)
                || message.status.equals(Constant.MessageStatus.STATUS_SEND_FAILURE)) {
            return;
        }
        Intent intent = new Intent(getContext(), FileViewerActivity.class);
        intent.putExtra("mid", message.mid);
        getContext().startActivity(intent);
    }

    @Override
    public void onProgress(float progress) {
        uploadProgressView.setProgress((int) progress);
        if (progress >= 100) {
            uploadProgressView.setVisibility(GONE);
            return;
        }
        if (progress > 0) {
            uploadProgressView.setVisibility(VISIBLE);
        }
    }
}
