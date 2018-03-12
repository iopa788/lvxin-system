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

import android.graphics.Bitmap;
import android.net.Uri;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.FileResource;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OSSFileUploadListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.model.Receiver;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.result.MessageForwardResult;
import com.farsunset.lvxin.util.BitmapUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;


/**
 * 消息转发
 */
public class MessageForwardHandler {

    public static void forward(final Message message, Uri imageUri, final HttpRequestListener responser, final ArrayList<MessageSource> reciverList) {
        if (message.format.equals(Constant.MessageFormat.FORMAT_IMAGE) && imageUri != null) {
            CloudImageLoaderFactory.get().downloadOnly(imageUri.toString(), new CloudImageLoadListener() {
                @Override
                public void onImageLoadFailure(Object ke) {
                }

                @Override
                public void onImageLoadSucceed(Object key, Bitmap resource) {
                    SNSImage snsImage = BitmapUtils.compressSNSImage(resource);
                    message.content = new Gson().toJson(snsImage);

                    if (!snsImage.thumbnail.equals(snsImage.image)) {
                        CloudFileUploader.asyncUpload(snsImage.thumbnail, new File(LvxinApplication.CACHE_DIR_IMAGE, snsImage.thumbnail), null);
                    }

                    CloudFileUploader.asyncUpload(snsImage.image, new File(LvxinApplication.CACHE_DIR_IMAGE, snsImage.image), new OSSFileUploadListener() {
                        @Override
                        public void onUploadCompleted(FileResource resource) {
                            performForwardMessage(message, reciverList, responser);
                        }

                        @Override
                        public void onUploadProgress(String key, float progress) {
                        }

                        @Override
                        public void onUploadFailured(FileResource resource, Exception e) {
                            responser.onHttpRequestFailure(e, URLConstant.MESSAGE_FORWARD_URL);
                        }
                    });
                }
            });


        } else {
            performForwardMessage(message, reciverList, responser);
        }
    }

    private static void performForwardMessage(Message message, ArrayList<MessageSource> reciverList, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MESSAGE_FORWARD_URL, MessageForwardResult.class);
        requestBody.addParameter("content", message.content);
        requestBody.addParameter("sender", message.sender);
        requestBody.addParameter("receiver", getReceiver(reciverList));
        requestBody.addParameter("format", message.format);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    public static String getReceiver(ArrayList<MessageSource> reciverList) {
        ArrayList<Receiver> list = new ArrayList<>(reciverList.size());
        for (MessageSource source : reciverList) {
            Receiver receiver = new Receiver();
            receiver.id = source.getId();
            receiver.type = source.getSourceType();
            list.add(receiver);
        }
        return new Gson().toJson(list);
    }
}
