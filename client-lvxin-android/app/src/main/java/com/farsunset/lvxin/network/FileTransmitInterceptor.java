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

import com.farsunset.lvxin.listener.OnTransmitProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class FileTransmitInterceptor implements Interceptor {
    private final OnTransmitProgressListener progressListener;
    private ResponseBody responseBody;
    private long fullContentLength;
    private long startContentLength;

    public FileTransmitInterceptor(OnTransmitProgressListener progressListener, long fullContentLength, long startContentLength) {
        this.progressListener = progressListener;
        this.startContentLength = startContentLength;
        this.fullContentLength = fullContentLength;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        responseBody = originalResponse.body();
        return originalResponse.newBuilder()
                .body(new FileResponseBody())
                .build();
    }

    public class FileResponseBody extends ResponseBody {
        private BufferedSource bufferedSource;

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytes = startContentLength;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytes += bytesRead != -1 ? bytesRead : 0;
                    float progress = totalBytes * 100 / fullContentLength;
                    progressListener.onProgress(progress);
                    return bytesRead;
                }
            };
        }
    }
}
