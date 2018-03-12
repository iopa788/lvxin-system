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

import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.network.result.BaseResult;


public class HttpRequestBody {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";
    public static final String UPDATE = "UPDATE";
    public static final String JSON_MEDIATYPE = "application/json;charset=UTF-8";
    public static final String ACCESS_TOKEN = "access-token";
    //接口地址
    private String url;

    //post的 body内容
    private String content;
    //post的 body内容
    private String contentType;
    private String method = POST;
    //返回的结构
    private Class<? extends BaseResult> dataClass;
    //参数
    private ArrayMap<String, String> parameter = new ArrayMap<>();
    //header
    private ArrayMap<String, String> header = new ArrayMap<>();

    public HttpRequestBody(String method, String url, Class<? extends BaseResult> dataClass) {
        this.url = url;
        this.method = method;
        this.dataClass = dataClass;
        header.put(ACCESS_TOKEN, Global.getAccessToken());
    }

    public HttpRequestBody(String url, Class<? extends BaseResult> dataClass) {
        this(POST, url, dataClass);
    }

    public void addParameter(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        parameter.put(key, value);
    }

    public void removeParameter(String key) {
        parameter.remove(key);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public void removeHeader(String key) {
        header.remove(key);
    }

    public void clearParameter() {
        parameter.clear();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<? extends BaseResult> getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class<? extends BaseResult> dataClass) {
        this.dataClass = dataClass;
    }

    public String getParameterValue(String key) {
        return parameter.get(key);
    }


    public ArrayMap<String, String> getParameter() {
        return parameter;
    }

    public ArrayMap<String, String> getHeader() {
        return header;
    }

    public String getHeaderValue(String key) {
        return header.get(key);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
