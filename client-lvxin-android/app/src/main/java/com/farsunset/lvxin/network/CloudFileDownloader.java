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

import android.support.v4.util.ArrayMap;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.listener.OSSFileDownloadListener;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.MLog;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 全局的文件下载工具
 */
public class CloudFileDownloader {
    // 每个文件的大小 byte
    // 每个下载线程的进度大小(byte)
    private final static ArrayMap<String, Call> CALL_MAP = new ArrayMap<String, Call>();
    private static ExecutorService downloadExecutor = Executors.newCachedThreadPool();

    public static void asyncDownload(final String bucket, final String key, final String dir, final OSSFileDownloadListener fileCallback) {

        downloadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final File targetFile = new File(dir, key);
                    if (targetFile.exists()) {
                        if (fileCallback != null) {
                            BackgroundThreadHandler.postUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    fileCallback.onDownloadCompleted(targetFile, key);
                                }
                            });
                        }
                        return;
                    }

                    FileUtils.writeByteArrayToFile(targetFile, IOUtils.toByteArray(new URL(String.format(URLConstant.FILE_PATH_URL, bucket, key))));

                    notifyDownloadCompleted(key, targetFile, fileCallback);
                } catch (Exception e) {
                    MLog.e("downloader", "", e);

                    notifyDownloadFailured(key, null, fileCallback);
                }
            }
        });
    }

    private static void notifyDownloadCompleted(final String url, final File file, final OSSFileDownloadListener fileCallback) {
        if (fileCallback != null) {
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    fileCallback.onDownloadCompleted(file, url);
                }
            });
        }
    }

    private static void notifyDownloadFailured(final String url, final File file, final OSSFileDownloadListener fileCallback) {
        if (fileCallback != null) {
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    fileCallback.onDownloadFailured(file, url);
                }
            });
        }
    }

    private static void notifyDownloadProgress(final String url, final float progress, final OSSFileDownloadListener fileCallback) {
        if (fileCallback != null) {
            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    fileCallback.onDownloadProgress(url, progress);
                }
            });
        }
    }

    public static void asyncDownloadDatabase(final OSSFileDownloadListener fileCallback) {

        downloadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    File targetFile = new File(LvxinApplication.DATABASE_DIR, "base.db");
                    FileUtils.writeByteArrayToFile(targetFile, IOUtils.toByteArray(new URL(URLConstant.DATABASE_FILE_URL)));
                    fileCallback.onDownloadCompleted(targetFile, null);
                } catch (Exception e) {
                    MLog.e("downloader", "", e);
                    fileCallback.onDownloadFailured(null, null);
                }
            }
        });
    }

    public static void asyncDownload(final String url, final File file, final OSSFileDownloadListener fileCallback) {
        asyncDownload(file, url, fileCallback);
    }

    public static void asyncDownload(final File file, final String url, final OSSFileDownloadListener fileCallback) {
        downloadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                download(url, file, fileCallback);
            }
        });
    }


    private static long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                IOUtils.closeQuietly(response);
                return contentLength;
            }
        } catch (IOException e) {
            MLog.e("downloader", "", e);
        }
        return 0;
    }

    private static void download(final String fileUrl, final File file, final OSSFileDownloadListener downloadCallBack) {

        long startPoints = 0;
        long contentLength = getContentLength(fileUrl);
        if (file.exists()) {
            if (contentLength == file.length() && contentLength > 0) {
                notifyDownloadCompleted(fileUrl, file, downloadCallBack);
                return;
            } else if (contentLength < file.length()) {
                file.delete();
                AppTools.creatFileQuietly(file);
            } else {
                startPoints = file.length();
            }
        }

        /**
         * 当文件不存在时获得的contentLength为0
         */
        if (contentLength == 0){
            notifyDownloadFailured(fileUrl, file, downloadCallBack);
            return;
        }
        Request request = new Request.Builder().url(fileUrl).header("RANGE", "bytes=" + startPoints + "-").build();

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new FileTransmitInterceptor(new OnTransmitProgressListener() {
            @Override
            public void onProgress(float progress) {
                notifyDownloadProgress(fileUrl, progress, downloadCallBack);
                if (progress >= 100) {
                    notifyDownloadCompleted(fileUrl, file, downloadCallBack);
                }
            }
        }, contentLength, startPoints)).build();

        Call call = client.newCall(request);

        CALL_MAP.put(fileUrl, call);

        InputStream stream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            stream = body.byteStream();
            randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(startPoints);
            byte[] buffer = new byte[2048];
            int numread;
            while ((numread = stream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, numread);
            }
        } catch (Exception e) {
            MLog.e("downloader", "", e);
            if (!call.isCanceled()) {
                notifyDownloadFailured(fileUrl, file, downloadCallBack);
            }
        } finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(randomAccessFile);
        }
    }

    /**
     * 通过任务名称，停止下载 fileUrl download()返回的任务名
     */
    public static void stop(String fileUrl) {
        Call call = CALL_MAP.get(fileUrl);
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }
}
