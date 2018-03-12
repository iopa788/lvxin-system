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

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class FileRequestBody extends RequestBody {
    private final OnTransmitProgressListener listener;
    private File file;

    public FileRequestBody(File file, OnTransmitProgressListener listener) {
        this.listener = listener;
        this.file = file;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = Okio.source(file);
        Buffer buf = new Buffer();
        Long remaining = contentLength();
        for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
            sink.write(buf, readCount);
            remaining -= readCount;
            float progress = (contentLength() - remaining) * 100 / file.length();
            listener.onProgress(progress);
        }
        IOUtils.closeQuietly(source);
    }
}
