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
package com.farsunset.lvxin.network;

import android.content.Intent;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.bean.FileResource;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OSSFileUploadListener;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.SendMessageResult;
import com.google.gson.Gson;

import java.io.File;

public class SendChatMessageRequester implements HttpRequestListener, OSSFileUploadListener {
    private Message message;

    private SendChatMessageRequester() {
    }

    public static SendChatMessageRequester getSendRequester(Message message) {
        SendChatMessageRequester sendRequester = new SendChatMessageRequester();
        sendRequester.message = message;
        return sendRequester;
    }

    public void send() {
        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            SendMessageRequester.send(message, this);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_FILE)) {
            ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);
            File file = chatFile.getLocalFile();
            CloudFileUploader.asyncUpload(chatFile.key, file, this);
            chatFile.path = null;
            message.content = new Gson().toJson(chatFile);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_IMAGE)) {

            SNSImage chatImage = new Gson().fromJson(message.content, SNSImage.class);
            CloudFileUploader.asyncUpload(chatImage.image, new File(LvxinApplication.CACHE_DIR_IMAGE, chatImage.image), this);
            if (!chatImage.thumbnail.equals(chatImage.image)) {
                CloudFileUploader.asyncUpload(chatImage.thumbnail, new File(LvxinApplication.CACHE_DIR_IMAGE, chatImage.thumbnail), null);
            }

            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_MAP)) {
            ChatMap chatMap = new Gson().fromJson(message.content, ChatMap.class);
            CloudFileUploader.asyncUpload(chatMap.key, new File(LvxinApplication.CACHE_DIR_IMAGE, chatMap.key), this);
            return;
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VOICE)) {
            ChatVoice chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
            CloudFileUploader.asyncUpload(chatVoice.key, new File(LvxinApplication.CACHE_DIR_VOICE, chatVoice.key), this);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            SNSVideo chatVideo = new Gson().fromJson(message.content, SNSVideo.class);
            CloudFileUploader.asyncUpload(chatVideo.video, new File(LvxinApplication.CACHE_DIR_VIDEO, chatVideo.video), this);
            CloudFileUploader.asyncUpload(chatVideo.thumbnail, new File(LvxinApplication.CACHE_DIR_VIDEO, chatVideo.thumbnail), null);
        }
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        if (result.isSuccess() && result instanceof SendMessageResult) {
            MessageRepository.deleteById(message.mid);
            message.mid = ((SendMessageResult) result).id;
            message.timestamp = ((SendMessageResult) result).timestamp;
            message.status = Constant.MessageStatus.STATUS_SEND;
            MessageRepository.add(message);
        } else {
            message.status = Constant.MessageStatus.STATUS_SEND_FAILURE;
        }

        ChatItem chat = new ChatItem(message, MessageParserFactory.getFactory().getMessageParser(message.action).getMessageSource(message));

        //通知主页对话列表刷新
        Intent rencentIntent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        rencentIntent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(rencentIntent);

        //通知聊天窗口页面列表刷新
        Intent windowIntent = new Intent(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
        windowIntent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(windowIntent);
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {

        message.status = Constant.MessageStatus.STATUS_SEND_FAILURE;
        MessageRepository.updateStatus(message.mid, message.status);

        ChatItem chat = new ChatItem(message, MessageParserFactory.getFactory().getMessageParser(message.action).getMessageSource(message));
        Intent intent = new Intent();
        intent.putExtra(ChatItem.NAME, chat);

        //通知主页对话列表刷新
        intent.setAction(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        LvxinApplication.sendLocalBroadcast(intent);

        //通知聊天窗口页面列表刷新
        intent.setAction(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onUploadCompleted(FileResource resource) {
        SendMessageRequester.send(message, this);
    }

    @Override
    public void onUploadProgress(String key, float progress) {
        Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
        intent.putExtra("objectKey", key);
        intent.putExtra("progress", progress);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {
        onHttpRequestFailure(e, null);
    }

}
