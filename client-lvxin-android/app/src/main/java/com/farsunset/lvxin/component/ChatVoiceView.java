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
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.app.GlobalMediaPlayer;
import com.farsunset.lvxin.app.GlobalVoicePlayer;
import com.farsunset.lvxin.app.GlobalVoicePlayer.OnPlayListener;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.OSSFileDownloadListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.CloudFileDownloader;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.File;


public class ChatVoiceView extends FrameLayout implements OnClickListener, OnPlayListener, OSSFileDownloadListener {
    private Message message;
    private File voiceFile;
    private int mMaxWidth;
    private TextView lengthTextView;
    private ChatVoice chatVoice;

    public ChatVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        float scale = context.getResources().getDisplayMetrics().density;
        mMaxWidth = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - (160 * scale + 0.5f));
    }


    public void display(Message msg, boolean self) {
        this.message = msg;
        chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
        loadVoiceFile();
        if (getChildCount() == 0) {
            LayoutInflater.from(getContext()).inflate(self ? R.layout.layout_chat_voice_item_self : R.layout.layout_chat_voice_item_other, this);
            lengthTextView = ((TextView) this.findViewById(R.id.lengthTextView));
        }

        lengthTextView.setText(chatVoice.length + "\"");
        int normalWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_voice_width);
        int realWidth = normalWidth + (int) ((mMaxWidth - normalWidth) * (chatVoice.length / 60f));
        this.getLayoutParams().width = Math.min(realWidth, mMaxWidth);
    }


    /**
     * 设置download url，开始下载
     */
    private void loadVoiceFile() {

        if (chatVoice.key == null) {
            return;
        }
        this.setTag(chatVoice.key);
        voiceFile = new File(LvxinApplication.CACHE_DIR_VOICE, chatVoice.key);
        if (!voiceFile.exists()) {
            voiceFile = null;
            CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_FILES, chatVoice.key, LvxinApplication.CACHE_DIR_VOICE, this);
        }
    }

    @Override
    public void onClick(View v) {
        if (voiceFile != null) {
            findViewById(R.id.waveformImage).setVisibility(View.GONE);
            findViewById(R.id.waveformAnim).setVisibility(View.VISIBLE);
            ((AnimationDrawable) ((ImageView) findViewById(R.id.waveformAnim)).getDrawable()).start();
            GlobalVoicePlayer.getPlayer().start(voiceFile, this);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        GlobalMediaPlayer.play(R.raw.play_completed);
        onVoiceStop();
    }

    @Override
    public void onVoiceStop() {
        findViewById(R.id.waveformImage).setVisibility(View.VISIBLE);
        findViewById(R.id.waveformAnim).setVisibility(View.GONE);
        ((AnimationDrawable) ((ImageView) findViewById(R.id.waveformAnim)).getDrawable()).stop();

        if (getParent() instanceof FromMessageVoiceView) {
            message.status = Message.STATUS_READ_OF_VOICE;
            MessageRepository.updateStatus(message.mid, Message.STATUS_READ_OF_VOICE);
            ((FromMessageVoiceView) getParent()).hideReadDot();
        }
    }


    @Override
    public void onDownloadCompleted(File file, String currentKey) {
        ChatVoiceView chatVoiceView = this;
        Object listView = this.getParent().getParent();
        if (listView != null) {
            Object target = ((View) listView).findViewWithTag(currentKey);
            if (target instanceof ChatVoiceView) {
                chatVoiceView = (ChatVoiceView) target;
            }
        }
        chatVoiceView.voiceFile = file;
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {
    }

    @Override
    public void onDownloadProgress(String key, float progress) {
    }

}
