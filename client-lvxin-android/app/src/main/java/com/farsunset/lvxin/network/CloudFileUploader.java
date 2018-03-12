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
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.FileResource;
import com.farsunset.lvxin.listener.OSSFileUploadListener;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 全局的文件上传工具
 */
public class CloudFileUploader {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private CloudFileUploader() {
    }

    public static void asyncUpload(final String objectKey, final File file,
                                   final OSSFileUploadListener callback) {
        asyncUpload(FileURLBuilder.BUCKET_FILES, objectKey, file, callback);
    }



    public static void asyncUpload(final String bucket, final String objectKey, final File file, final OSSFileUploadListener callback) {
        FileResource rerource = new FileResource(objectKey,bucket,file);
        asyncUpload(rerource,callback);
    }

    public static void asyncUpload(final FileResource rerource, final OSSFileUploadListener callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient httpclient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                    MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    build.addFormDataPart("filename", rerource.key);
                    build.addFormDataPart("bucket", rerource.bucket);
                    build.addFormDataPart("file", rerource.file.getName(), new FileRequestBody(rerource.file, new OnTransmitProgressListener() {
                        @Override
                        public void onProgress(final float progress) {
                            if (callback != null) {
                                BackgroundThreadHandler.postUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onUploadProgress(rerource.key, progress);
                                    }
                                });
                            }
                        }
                    }));
                    Request request = new Request.Builder()
                            .url(URLConstant.FILE_UPLOAD_URL)
                            .header(HttpRequestBody.ACCESS_TOKEN, Global.getAccessToken())
                            .post(build.build())
                            .build();

                    httpclient.newCall(request).execute();

                    if (callback != null) {
                        BackgroundThreadHandler.postUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onUploadCompleted(rerource);
                            }
                        });
                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", 100f);
                    LvxinApplication.sendLocalBroadcast(intent);


                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        BackgroundThreadHandler.postUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onUploadFailured(rerource,e);
                            }
                        });
                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", -1f);
                    LvxinApplication.sendLocalBroadcast(intent);
                }
            }
        });
    }
}
