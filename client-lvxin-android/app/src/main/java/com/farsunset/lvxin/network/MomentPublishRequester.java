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

import android.util.ArrayMap;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.FileResource;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OSSFileUploadListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.network.result.CommonResult;
import com.farsunset.lvxin.util.AppTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class MomentPublishRequester {
    private static final int TRY_COUNT = 3;
    private static final ArrayMap<String,Integer> COUNTER = new ArrayMap<>();
    private static final OSSFileUploadListener UPLOAD_LISTENER = new OSSFileUploadListener(){
        @Override
        public void onUploadCompleted(FileResource resource) {
            COUNTER.remove(resource.key);
        }
        @Override
        public void onUploadProgress(String key, float progress) {}

        @Override
        public void onUploadFailured(FileResource resource, Exception e) {
            Integer count =  COUNTER.get(resource.key);
            if (count < TRY_COUNT && AppTools.isNetworkConnected())
            {
                CloudFileUploader.asyncUpload(resource, UPLOAD_LISTENER);
                COUNTER.put(resource.key,++count);
            }
        }
    };
    public static void publish(Moment article, HttpRequestListener responser) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.ARTICLE_PUBLISH_URL, CommonResult.class);

        requestBody.addParameter("type", article.type);
        requestBody.addParameter("content", article.content);
        requestBody.addParameter("thumbnail", article.thumbnail);
        HttpRequestLauncher.execute(requestBody, responser);

        if (article.type.equals(Moment.FORMAT_TEXT_IMAGE) && article.thumbnail != null) {
            List<SNSImage> snsImageList = new Gson().fromJson(article.thumbnail, new TypeToken<List<SNSImage>>() {}.getType());

            for (SNSImage image : snsImageList) {
                CloudFileUploader.asyncUpload(image.thumbnail, new File(LvxinApplication.CACHE_DIR_IMAGE, image.thumbnail), UPLOAD_LISTENER);
                CloudFileUploader.asyncUpload(image.image, new File(LvxinApplication.CACHE_DIR_IMAGE, image.image), UPLOAD_LISTENER);
            }
        }
        if (article.type.equals(Moment.FORMAT_VIDEO)) {
            SNSVideo video = new Gson().fromJson(article.content, PublishObject.class).video;
            CloudFileUploader.asyncUpload(video.video, new File(LvxinApplication.CACHE_DIR_VIDEO, video.video), UPLOAD_LISTENER);
            CloudFileUploader.asyncUpload(video.thumbnail, new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail), UPLOAD_LISTENER);
        }
    }


}
