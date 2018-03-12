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
package com.farsunset.lvxin.message.handler;

import android.content.Context;

import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.network.CloudFileDownloader;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action000MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        if (!FriendRepository.isFriend(message.getSender())) {
            MessageRepository.deleteById(message.getMid());
            return false;
        }

        beforehandLoadFiles(message);

        return true;
    }

    protected void beforehandLoadFiles(Message message) {

        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_VOICE)) {
            ChatVoice chatVoice = new Gson().fromJson(message.getContent(), ChatVoice.class);
            CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_FILES, chatVoice.key, LvxinApplication.CACHE_DIR_VOICE, null);
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            SNSImage snsImage = new Gson().fromJson(message.getContent(), SNSImage.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getFileUrl(snsImage.thumbnail));
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_MAP)) {
            ChatMap chatMap = new Gson().fromJson(message.getContent(), ChatMap.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getFileUrl(chatMap.key));
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            SNSVideo chatVideo = new Gson().fromJson(message.getContent(), SNSVideo.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getFileUrl(chatVideo.thumbnail));
            if (LvxinApplication.getInstance().isConnectWifi()) {
                CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_FILES, chatVideo.video, LvxinApplication.CACHE_DIR_VIDEO, null);
            }
        }
    }

}
